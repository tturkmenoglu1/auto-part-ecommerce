package com.ape.security;
import com.ape.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)

@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/login",
                        "/register",
                        "/confirm",
                        "/image/**",
                        "/categories/**",
                        "/products/**",
                        "/basket/**",
                        "/users/**",
                        "/brands/**",
                        "/database/**").permitAll().
                anyRequest().authenticated());

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").
                        allowedHeaders("*").
                        allowedMethods("*");
            }
        };
    }

    private static final String [] AUTH_WHITE_LIST= {
            "/v3/api-docs/**", // swagger
            "swagger-ui.html", //swagger
            "/swagger-ui/**", // swagger
            "/",
            "index.html",
            "/images/**",
            "/css/**",
            "/js/**"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(AUTH_WHITE_LIST);
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;

    }

    @Bean
    public AuthenticationManager authManager( HttpSecurity http) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class).
                authenticationProvider(authProvider() ).
                build();

    }
}
