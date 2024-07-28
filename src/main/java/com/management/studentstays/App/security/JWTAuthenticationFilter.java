package com.management.studentstays.App.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private com.management.studentstays.App.security.JWTTokenHelper jwtTokenHelper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("----------- Do filter --------------");

    String requestToken = request.getHeader("Authorization");

    String userName = null;
    String token = null;

    if (requestToken != null && requestToken.startsWith("Bearer")) {
      token = requestToken.substring(7);
      try {
        userName = this.jwtTokenHelper.getUserNameFromToken(token);
      } catch (IllegalArgumentException e) {
        log.info("Unable to get JWT token.");
      } catch (ExpiredJwtException e) {
        log.info("JWT token has expired.");
      } catch (MalformedJwtException e) {
        log.info("Invalid JWT.");
      }

    } else {
      System.out.println("JWT token does not begin with Bearer");
    }

    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

      if (this.jwtTokenHelper.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      } else {
        log.info("Invalid JWT token");
      }
    } else {
      log.info("Username is null or context is not null.");
    }

    filterChain.doFilter(request, response);
  }
}
