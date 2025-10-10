    package com.integrador.gym.Controller.Config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.web.server.ServerHttpSecurity;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.server.SecurityWebFilterChain;

    @Configuration
    @EnableWebSecurity
    public class Segurity {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            // 🔓 Rutas públicas que no necesitan autenticación
                            .requestMatchers(
                                    "/", "/login", "/registro/**", "/olvide",
                                    "/Recursos/**", "/Css/**", "/js/**", "/Imagenes/**",
                                    "/h2-console/**"
                            ).permitAll()
                            // 🔒 Todo lo demás requiere autenticación
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/login") // Thymeleaf renderiza login.html
                            .defaultSuccessUrl("/dashboard", true)
                            .failureUrl("/login?error=true")
                            .permitAll() // 🔓 Permitir acceso a la página de login
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/login?logout=true")
                            .permitAll()
                    )
                    .csrf(csrf -> csrf.disable()) // Desactiva CSRF solo en desarrollo
                    .headers(headers -> headers.frameOptions().disable());;


            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

            // Agregar usuarios de prueba
            manager.createUser(User.withDefaultPasswordEncoder()
                    .username("12345678")
                    .password("admin123")
                    .roles("ADMIN")
                    .build());

            manager.createUser(User.withDefaultPasswordEncoder()
                    .username("reception")
                    .password("reception123")
                    .roles("RECEPTION")
                    .build());

            return manager;
        }
    }
