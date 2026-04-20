package com.shreya.user_management_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        String path = request.getServletPath();
//
//        if (path.startsWith("/auth")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        // 1. get authorization header......
        final String authHeader = request.getHeader("Authorization");

        // 2. check if header exist......
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // 3. extract token....
        final String jwt = authHeader.substring(7);

        // 4. extract username..
        final String username = jwtService.extractUsername(jwt);

        // 5. check if already authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load user details....
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 7. validate token
            if(jwtService.isTokenValid(jwt, userDetails.getUsername())){

                // 8. extract roles....
                List<String> roles = jwtService.extractRoles(jwt);

                var authorities = roles.stream().map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role)).collect(Collectors.toList());

                // 9. create authentication object
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                // 10. attach request details
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 11. Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // 12. continue filter chain
        filterChain.doFilter(request, response);
    }
}
