package com.adoptasalvavidas.adoptasalvavidasBack.Controller;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.AuthResponse;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.AuthValidateCodRequest;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.LoginRequest;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.PasswordResetToken;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Usuario;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.UsuarioRepository;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.PasswordResetTokenRepository;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.AuthenticateService;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.EmailService;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.UserDetailsServiceImpl;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.UsuarioServices;
import com.adoptasalvavidas.adoptasalvavidasBack.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.AuthProvider;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticateService authenticate;

    @Autowired
    private UsuarioServices usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService mailManager;


    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Usuario> pendingUsers = new HashMap<>();


    @PostMapping("/enviarCodigo")
    public ResponseEntity<Object> enviarCodigo(@RequestBody Usuario usuario) {
        String email = usuario.getEmail();

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo no puede estar vacío."));
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Formato de correo inválido."));
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo ya está registrado."));
        }

        // Enviar código al correo (debes implementar el método sendMessageUser)
        String code = authenticate.sendMessageUser(email);
        verificationCodes.put(email, code);
        pendingUsers.put(email, usuario); // Guardar el usuario temporalmente

        return ResponseEntity.ok(Map.of("message", "Código enviado al correo."));
    }

    @PostMapping("/validarCodigo")
    public ResponseEntity<Object> validarCodigo(@RequestBody AuthValidateCodRequest request) {
        String email = request.email();
        String code = request.code();

        if (email == null || code == null || email.isEmpty() || code.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Correo y código son obligatorios."));
        }

        if (!verificationCodes.containsKey(email) || !verificationCodes.get(email).equals(code)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Código incorrecto."));
        }

        Usuario usuario = pendingUsers.get(email);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        // Asegúrate de encriptar la contraseña si es registro local
        usuario.setPassword(usuario.getPassword()); 
        usuario.setAuthProvide(AuthProvider.LOCAL); 
        usuarioService.registerUserLocal(usuario); 

        verificationCodes.remove(email);
        pendingUsers.remove(email);

        return ResponseEntity.ok(Map.of("message", "Código validado. Usuario registrado exitosamente."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuario(username);
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();
        Optional<PasswordResetToken> existingToken = tokenRepository.findByUsuario(usuario);
        existingToken.ifPresent(tokenRepository::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        mailManager.enviarCorreoCambioPassword(username, token);
        return ResponseEntity.ok(Map.of("message", "Correo de recuperación enviado"));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String nuevaContrasena) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        }

        PasswordResetToken resetToken = optionalToken.get();
        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(new BCryptPasswordEncoder().encode(nuevaContrasena));
        usuarioService.Guardar(usuario);

        tokenRepository.delete(resetToken);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

    // @PostMapping("/register")
    // public ResponseEntity<Usuario> register(@RequestBody @Validated Usuario user){
    //     return ResponseEntity.ok(usuarioService.registerUserLocal(user));
    // }

    //    @PostMapping("/login/local")
    //    public ResponseEntity<Usuario> loginLocal(@RequestBody Usuario user){
    //        return ResponseEntity.ok(usuarioService.loginUserLocal(user));
    //    }

    @GetMapping("/login/google")
    public ResponseEntity<String > loginGoogleAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
        return ResponseEntity.ok("Redirecting ..");
    }
    @GetMapping("/loginSuccess")
    public ResponseEntity<? > handleGoogleSuccess(OAuth2AuthenticationToken oAuth2AuthenticationToken){
        Usuario user = usuarioService.loginRegisterByGoogleOAuth2(oAuth2AuthenticationToken);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/home")).build();
    }
    
    @GetMapping("/actual-usuario")
    public ResponseEntity<?> obtenerUsuarioActual(Principal principal) {
        try {
            if (principal == null) {
                System.out.println("Principal es null: no estás autenticado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
            }

            System.out.println("Principal name: " + principal.getName());

            UserDetails userDetails = userDetailService.loadUserByUsername(principal.getName());

            System.out.println("UserDetails obtenido: " + userDetails.getUsername());

            Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(userDetails.getUsername());

            if (optionalUsuario.isPresent()) {
                return ResponseEntity.ok(optionalUsuario.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PostMapping("/login/local")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            AuthResponse response = this.userDetailService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            AuthResponse errorResponse = new AuthResponse(
                null,
                "Credenciales inválidas: " + e.getMessage(),
                null,
                null,
                false
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuarioPorId(@PathVariable Long id) {
        usuarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }


}
