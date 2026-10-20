package com.example.project.config;

import com.example.project.config.security.jwt.JwtTokenFilter;
import com.example.project.config.security.jwt.JwtUtil;
import com.example.project.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   private final JwtUtil jwtUtil;
   private final CustomUserDetailsService customUserDetailsService;

   @Bean
   public JwtTokenFilter jwtTokenFilter(){
      return new JwtTokenFilter(jwtUtil, customUserDetailsService);
   }
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // HttpSecurity 설정
      http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                  .requestMatchers(
                        "/auth/register",
                        "/auth/login",
                        "/auth/refresh",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/",
                        "/ws/**",
                        "/actuator/health"
                  ).permitAll()
                  .requestMatchers(HttpMethod.GET
                      ,"/destinations/popular"
                     ,"/naver/static-map").permitAll()
                  .requestMatchers("/actuator/**").hasRole("ADMIN")
                  .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                  .authenticationEntryPoint((req, res, e) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
            )
            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 인가 필터 추가

      return http.build();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource(){
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:8080",
            "http://localhost:5173",
            "https://traivel.p-e.kr"
      ));
      configuration.setAllowedMethods(List.of("*"));
      configuration.addAllowedHeader("*");
      configuration.setExposedHeaders(List.of("*"));
      configuration.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**",configuration);
      return source;
   }
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
      return authenticationConfiguration.getAuthenticationManager();
   }
}

