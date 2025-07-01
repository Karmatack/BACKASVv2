package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.AuthResponse;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.LoginRequest;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Usuario;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.RolRepository;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.UsuarioRepository;
import com.adoptasalvavidas.adoptasalvavidasBack.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }
        System.out.println("Buscando el usuario con el email: " + username);

        Optional<Usuario> usuarioDaoOptional = usuarioRespository.findByEmail(username);
        System.out.println("Usuario encontrado: " + usuarioDaoOptional);

//        if (usuarioDaoOptional.isEmpty()) {
//            throw new UsernameNotFoundException("Usuario no encontrado");
//        }

        Usuario usuario = usuarioDaoOptional.get();
        System.out.println("Usuario: " + usuario);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRol() != null) {
            authorities.add(new SimpleGrantedAuthority(usuario.getRol().getName()));
        }

        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }

    public AuthResponse loginUser(LoginRequest authLoginRequest) {

        String username = authLoginRequest.getEmail();
        String password = authLoginRequest.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        Optional<Usuario> usuarioDaoOptional = usuarioRespository.findByEmail(username);
        if (usuarioDaoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        Usuario usuario = usuarioDaoOptional.get();

        // Obtener el nombre del rol del usuario (ya que solo tiene un rol)
        // Asegurarse de que el rol tenga el prefijo ROLE_ si está presente
        String roleName = usuario.getRol() != null && usuario.getRol().getName() != null
                ? "ROLE_" + usuario.getRol().getName()  // Prefijo ROLE_ agregado al nombre del rol
                : null; // Si no tiene rol, retorna null o puedes lanzar una excepción si es necesario


        return new AuthResponse(username, "User logged successfully", roleName, accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        // Loguear las contraseñas antes de la comparación
        System.out.println("Contraseña proporcionada: " + password);
        System.out.println("Contraseña almacenada (cifrada): " + userDetails.getPassword());

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
