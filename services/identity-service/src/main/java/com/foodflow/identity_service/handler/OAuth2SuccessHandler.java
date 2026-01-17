package com.foodflow.identity_service.handler;


import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.exceptions.BadRequestException;
import com.foodflow.identity_service.jwt.JwtService;
import com.foodflow.identity_service.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Value("${frontend.redirect.url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)  authentication;
        DefaultOAuth2User oAuthUser = (DefaultOAuth2User) token.getPrincipal();
        String email = oAuthUser.getAttribute("email");
        if (email == null) {
            throw new BadRequestException("Email not available from OAuth provider");
        }
        User user = userService.getUserByEmail(email).orElse(null);

        if(user == null){
            User newUser = User.builder()
                    .email(email)
                    .name(oAuthUser.getAttribute("name"))
                    .profileImageUrl(oAuthUser.getAttribute("picture"))
                    .roles(Set.of(UserRole.USER))
                    .build();

            user = userService.saveUser(newUser);
        }

        String accessToken = jwtService.generateToken(user);
        log.info("Token: {}",accessToken);
        String frontEndUrl = redirectUrl+"?token="+accessToken;
        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);
    }
}
