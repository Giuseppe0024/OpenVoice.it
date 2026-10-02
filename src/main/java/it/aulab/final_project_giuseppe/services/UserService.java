package it.aulab.final_project_giuseppe.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.final_project_giuseppe.dtos.UserDto;
import it.aulab.final_project_giuseppe.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response);
    User findUserByEmail(String email);
    User find(Long id);
}
