package nl.inholland.BankAPI.Security.Filter;

import jakarta.servlet.*;
import lombok.extern.java.Log;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log
@Component
@Order(1)
public class LargeRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        int size = servletRequest.getContentLength();
        log.info("Request size: " + size);
        if (size > 1000) {
            log.severe( "request with size " + size + " was rejected");
            throw new ServletException("Request too large");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
