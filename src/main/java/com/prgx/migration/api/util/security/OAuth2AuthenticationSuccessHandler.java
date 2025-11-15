package com.prgx.migration.api.util.security;

import com.prgx.migration.api.util.model.User;
import com.prgx.migration.api.util.service.JwtService;
import com.prgx.migration.api.util.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Handler for successful OAuth2 authentication
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtService jwtService;
    private final UserService userService;

    @Value("${app.oauth2.redirect-uri:http://localhost:3000/oauth2/redirect}")
    private String defaultRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = getRegistrationId(request);

        // Process OAuth user and get/create User entity
        User user = userService.processOAuthUser(registrationId, oAuth2User);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Get redirect URI from session or use default
        String redirectUri = getRedirectUri(request);

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    private String getRegistrationId(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String[] parts = requestUri.split("/");
        return parts[parts.length - 1];
    }

    private String getRedirectUri(HttpServletRequest request) {
        // Get from session or use default redirect URI
        return defaultRedirectUri;
    }
}

