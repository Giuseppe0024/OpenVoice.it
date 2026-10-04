package it.aulab.final_project_giuseppe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import it.aulab.final_project_giuseppe.repositories.ArticleRepository;
import it.aulab.final_project_giuseppe.repositories.CareerRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component 
public class NotificationInterceptor implements HandlerInterceptor {

    @Autowired 
    CareerRequestRepository careerRequestRepository;

    @Autowired 
    ArticleRepository articleRepository;

    @Override 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
                            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && request.isUserInRole("ROLE_ADMIN")) {
            long careerCount = careerRequestRepository.countByIsCheckedFalseAndIsViewedFalse();
            modelAndView.addObject("careerRequests", careerCount);
        }

        if (modelAndView != null && request.isUserInRole("ROLE_REVISOR")) {
            int revisedCount = articleRepository.findByIsAcceptedNull().size();
            modelAndView.addObject("articlesToBeRevised", revisedCount);
        }
    }
}
