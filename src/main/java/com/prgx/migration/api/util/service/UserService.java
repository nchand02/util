package com.prgx.migration.api.util.service;

import com.prgx.migration.api.util.exception.ResourceNotFoundException;
import com.prgx.migration.api.util.model.User;
import com.prgx.migration.api.util.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for User entity operations
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Transactional
    public User processOAuthUser(String registrationId, OAuth2User oAuth2User) {
        User.AuthProvider provider = User.AuthProvider.valueOf(registrationId.toUpperCase());
        String providerId = getProviderId(registrationId, oAuth2User);
        String email = getEmail(registrationId, oAuth2User);
        String name = getName(registrationId, oAuth2User);
        String avatarUrl = getAvatarUrl(registrationId, oAuth2User);

        return userRepository.findByProviderAndProviderId(provider, providerId)
                .map(existingUser -> updateExistingUser(existingUser, email, name, avatarUrl))
                .orElseGet(() -> createNewUser(provider, providerId, email, name, avatarUrl));
    }

    @Transactional
    protected User updateExistingUser(User user, String email, String name, String avatarUrl) {
        boolean updated = false;

        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
            updated = true;
        }
        if (!user.getName().equals(name)) {
            user.setName(name);
            updated = true;
        }
        if (avatarUrl != null && !avatarUrl.equals(user.getAvatarUrl())) {
            user.setAvatarUrl(avatarUrl);
            updated = true;
        }

        return updated ? userRepository.save(user) : user;
    }

    @Transactional
    protected User createNewUser(User.AuthProvider provider, String providerId, String email, String name, String avatarUrl) {
        User newUser = User.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .name(name)
                .avatarUrl(avatarUrl)
                .build();

        return userRepository.save(newUser);
    }

    private String getProviderId(String registrationId, OAuth2User oAuth2User) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> String.valueOf(oAuth2User.getAttribute("id"));
            case "microsoft" -> oAuth2User.getAttribute("id");
            case "facebook" -> oAuth2User.getAttribute("id");
            default -> throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        };
    }

    private String getEmail(String registrationId, OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("email");
    }

    private String getName(String registrationId, OAuth2User oAuth2User) {
        return switch (registrationId.toLowerCase()) {
            case "google", "microsoft", "facebook" -> oAuth2User.getAttribute("name");
            case "github" -> {
                String name = oAuth2User.getAttribute("name");
                yield (name != null) ? name : oAuth2User.getAttribute("login");
            }
            default -> "Unknown";
        };
    }

    private String getAvatarUrl(String registrationId, OAuth2User oAuth2User) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("picture");
            case "github" -> oAuth2User.getAttribute("avatar_url");
            case "facebook" -> {
                Object picture = oAuth2User.getAttribute("picture");
                if (picture instanceof java.util.Map) {
                    Object data = ((java.util.Map<?, ?>) picture).get("data");
                    if (data instanceof java.util.Map) {
                        yield (String) ((java.util.Map<?, ?>) data).get("url");
                    }
                }
                yield null;
            }
            default -> null;
        };
    }
}

