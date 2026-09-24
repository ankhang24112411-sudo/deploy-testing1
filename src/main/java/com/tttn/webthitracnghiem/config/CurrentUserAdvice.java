package com.tttn.webthitracnghiem.config;

import com.tttn.webthitracnghiem.model.User;
import com.tttn.webthitracnghiem.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class CurrentUserAdvice {
    @Autowired
    private IUserService userService;

    @ModelAttribute
    public void addCurrentUserToSession(Authentication authentication, HttpSession session) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            return;
        }

        Object currentAdmin = session.getAttribute("admin");
        if (currentAdmin instanceof User && authentication.getName().equals(((User) currentAdmin).getId())) {
            return;
        }

        User user = userService.findById(authentication.getName());
        if (user != null) {
            session.setAttribute("admin", user);
        }
    }
}
