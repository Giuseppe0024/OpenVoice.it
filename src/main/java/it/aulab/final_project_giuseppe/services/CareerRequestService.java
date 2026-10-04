package it.aulab.final_project_giuseppe.services;

import it.aulab.final_project_giuseppe.models.CareerRequest;
import it.aulab.final_project_giuseppe.models.User;

public interface CareerRequestService {
    boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest);
    void save(CareerRequest careerRequest, User user);
    void careerAccept(Long requestId);
    CareerRequest find(Long id);
    void markAsViewed(Long id);
}