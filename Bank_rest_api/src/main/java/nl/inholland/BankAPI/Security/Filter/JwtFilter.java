package nl.inholland.BankAPI.Security.Filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.inholland.BankAPI.Security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider provider){
        this.jwtProvider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);

        if (token == null){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            //F7 Fix: log details, send generic message to client
            log.warning("Rejected JWT: " + e.getMessage());
            PrintWriter writer = response.getWriter();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.write("Invalid or Expired token");
            writer.flush();
            return;
        } catch (Exception e) {
            log.severe("Error while authenticating request: " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }
}
