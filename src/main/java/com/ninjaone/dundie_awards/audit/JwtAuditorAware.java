package com.ninjaone.dundie_awards.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtAuditorAware implements AuditorAware<String> {

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.isAuthenticated()) {
            return Optional.of("system");
        }

        if(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            var jwt = jwtAuthenticationToken.getToken();
            String user = jwt.getClaimAsString("user");
            if(user == null || user.isBlank()) {
                user = jwt.getSubject();
            }

            if(user == null || user.isBlank()) {
                user = jwtAuthenticationToken.getName();
            }

            return Optional.ofNullable(user).or(() -> Optional.of("unknown"));
        }

        return Optional.empty();
    }
}
