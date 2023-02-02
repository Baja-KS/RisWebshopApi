package com.bajaks.RisWebshopApi.security.filter;

import com.bajaks.RisWebshopApi.security.UserDetailsServiceImplementation;
import com.bajaks.RisWebshopApi.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImplementation userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.atInfo().log("Enter the filter");
        Optional<String> maybeToken = jwtUtil.getToken(request);
        if (maybeToken.isEmpty()){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"No token provided");
            filterChain.doFilter(request,response);
            return;
        }
        String token = maybeToken.get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
        if (!jwtUtil.validateToken(token,userDetails)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token invalid");
            filterChain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        authenticate.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticate);


        filterChain.doFilter(request,response);
    }
}
