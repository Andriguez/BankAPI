package nl.inholland.BankAPI.Security;

import nl.inholland.BankAPI.Security.Filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final JwtFilter jwtFilter;

    public WebSecurityConfiguration(JwtFilter filter){
        this.jwtFilter = filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(
                session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/login").permitAll());
        httpSecurity.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/h2-console").permitAll());

        httpSecurity.authorizeHttpRequests(
                requests ->
                        requests.requestMatchers("/users").authenticated());

        httpSecurity.addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
