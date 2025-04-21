package com.mynotes.OAuth2Handler;

import com.mynotes.entities.User;
import com.mynotes.services.UserService;
import com.mynotes.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private TemporaryTokenService tempTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();
        for(var i: attributes.keySet()){
            System.out.println(i);
        }
        String email = oauthUser.getAttribute("email");
        String avatar = oauthUser.getAttribute("picture");
        User user = userService.findByEmail(email);
        if (user!=null) {
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            String redirectUrl = "http://localhost:5173/?jwtToken=" + jwtToken;
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            String jwtToken = tempTokenService.createToken(email);
            tempTokenService.setAvatar(avatar);
            String redirectUrl = "http://localhost:5173/complete-signup?tempToken=" + jwtToken;
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}

