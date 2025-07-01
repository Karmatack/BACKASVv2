package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Rol;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Usuario;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.AuthProvider;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServices {
    @Autowired
    private  UsuarioRepository usersRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;


    public Optional<Usuario> obtenerUsuario(String email) {
        return usersRepository.findByEmail(email);
    }

    public Usuario Guardar(Usuario usuario) {
        return usersRepository.save(usuario);
    }



        public Usuario registerUserLocal(Usuario user){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAuthProvide(AuthProvider.LOCAL);
            Rol rol = new Rol();

            rol.setId(user.getRol().getId());
            user.setRol(rol);
            return  usersRepository.save(user);

        }

    public Usuario loginUserLocal(Usuario user){
        Usuario existingUser = usersRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser != null){
            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                throw new RuntimeException("User pasowrd does  ot match");
            }
            return existingUser;
        }
        throw new RuntimeException("User not found");
    }

    public  Usuario loginRegisterByGoogleOAuth2(OAuth2AuthenticationToken auth2AuthenticationToken){

        OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("USER Email FROM GOOGLE  IS {}",email );
        log.info("USER Name from GOOGLE IS {}",name );

        Usuario user = usersRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new Usuario();
            user.setName(name);
            user.setEmail(email);
            user.setAuthProvide(AuthProvider.GOOGLE);
            Rol rol = new Rol(); // Asumimos que Rol es una entidad y debe ser asignado correctamente
            rol.setId(2); // O cualquier valor válido
            user.setRol(rol);
            return usersRepository.save(user);
        }
        return user;
    } 
    public java.util.List<Usuario> obtenerTodos() {
        return usersRepository.findAll();
    }

    public void eliminarPorId(Long id) {
        usersRepository.deleteById(id);
    }
}
