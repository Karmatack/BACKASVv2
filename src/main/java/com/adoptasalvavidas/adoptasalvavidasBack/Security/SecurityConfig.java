package com.adoptasalvavidas.adoptasalvavidasBack.Security;

import com.adoptasalvavidas.adoptasalvavidasBack.Security.Filter.JwtTokenValidator;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.UserDetailsServiceImpl;
import com.adoptasalvavidas.adoptasalvavidasBack.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        private JwtUtils jwtUtils;

        @Bean
        @Order(1)
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/perro/**")
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults())
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .authorizeHttpRequests(auth -> auth
                                                // Permitir publicamente el endpoint de disponibles sin JWT
                                                .requestMatchers("/perro/disponibles").permitAll()
                                                // El resto de rutas bajo /perro/** requieren JWT
                                                .anyRequest().authenticated())
                                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults())
                                .exceptionHandling(ex -> ex
                                                .defaultAuthenticationEntryPointFor(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                                                new AntPathRequestMatcher("/perro/**")))
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers(HttpMethod.OPTIONS, "/perro/**").permitAll()
                                                .requestMatchers(
                                                                "/login/**", "/login/local", "/logout",
                                                                "/actual-usuario",
                                                                "/enviarCodigo", "/validarCodigo", "/forgot-password",
                                                                "/reset-password",
                                                                "/perro/disponibles",
                                                                "/imagenes/perro/*", "/imagenes/*", "/api/contacto")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/perro/listar", "/perro/*")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.POST, "/perro/registrar").authenticated()
                                                .requestMatchers(HttpMethod.DELETE, "/perro/*").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/imagenes/guardar").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/imagenes/editar/*").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/imagenes/eliminar/*")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/contacto").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/contacto/*").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/contacto/*").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/formulario").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/formulario").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/formulario/usuario/*")
                                                .authenticated()
                                                .requestMatchers(HttpMethod.GET, "/formulario/*").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/formulario/*/aprobar")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/formulario/*/rechazar")
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login/google")
                                                .defaultSuccessUrl("/loginSuccess", true)
                                                .failureUrl("/loginFailure"))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login/local")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());

                return httpSecurity.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(passwordEncoder());
                provider.setUserDetailsService(userDetailsService);
                return provider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("https://adoptasalvavidas-front.vercel.app")); // o tu dominio
                                                                                                       // frontend
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
