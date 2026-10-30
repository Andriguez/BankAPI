package nl.inholland.BankAPI.Security;

import nl.inholland.BankAPI.Security.Filter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final List<String> allowedOrigins;

    public WebSecurityConfiguration(JwtFilter filter,
                                        @Value("${app.cors.allowed-origins:http://localhost:5173}") List<String> allowedOrigins){
        this.jwtFilter = filter;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // F6 Fix: only yhe configured frontend origin instead of all (*)
        httpSecurity.cors(config -> {
            CorsConfiguration corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOrigins(allowedOrigins);
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));

            config.configurationSource(request -> corsConfig);
        });

        httpSecurity.sessionManagement(
                session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //F4 and F8 Fixes: no public database console, and setting a deny default for new routes;
        httpSecurity.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/login", "/register", "/error").permitAll()
                        .anyRequest().authenticated());
        //F4 Fix: frame protection back on.
        httpSecurity.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        httpSecurity.addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
