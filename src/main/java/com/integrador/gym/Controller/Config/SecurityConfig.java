package com.integrador.gym.Controller.Config;

import com.integrador.gym.Service.Impl.UsuarioDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioDetailsServiceImpl usuarioDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers("/", "/index", "/ubicacion/**", "/registro/**", "/login", "/Css/**", "/JS/**", "/Imagenes/**", "/error")
                        .permitAll()

                        // Roles específicos
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/entrenador/**").hasRole("ENTRENADOR")
                        .requestMatchers("/recepcionista/**").hasRole("RECEPCIONISTA")
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        .requestMatchers("/dashboard").authenticated()

                        .anyRequest().authenticated()
                )
                // Configuración del login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("dni") // <== LOGIN CON DNI
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                // Configuración del logout
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

    // ✅ Define el servicio de detalles de usuario para Spring Security
    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioDetailsService;
    }

    @Bean
    public CustomAuthSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

}
