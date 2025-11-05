package com.IlyaTr.movie_catalog.handler;

import jakarta.ws.rs.ClientErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    public String handleClientErrorException(ClientErrorException e, RedirectAttributes redirectAttributes){
        if (e.getResponse().getStatus() == 409){
            log.error("Failed to return response", e);
            redirectAttributes.addFlashAttribute("exception",
                    "Пользователь с таким username или email уже существует ");
            return "redirect:/users/profile";
        }
        return "redirect:/users/profile";
    }
}
