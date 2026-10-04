package it.aulab.final_project_giuseppe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.final_project_giuseppe.models.CareerRequest;
import it.aulab.final_project_giuseppe.models.Role;
import it.aulab.final_project_giuseppe.models.User;
import it.aulab.final_project_giuseppe.repositories.CareerRequestRepository;
import it.aulab.final_project_giuseppe.repositories.RoleRepository;
import it.aulab.final_project_giuseppe.repositories.UserRepository;

import java.util.List;

@Service
public class CareerRequestServiceImpl implements CareerRequestService{

    @Autowired 
    private CareerRequestRepository careerRequestRepository;

    @Autowired 
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private RoleRepository roleRepository;

    @Transactional
    public boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest) {
        List<Long> allUserIds = careerRequestRepository.findAllUserIds();

        if (!allUserIds.contains(user.getId())) {
            return false;
        }

        List<Long> requests = careerRequestRepository.findByUserId(user.getId());

        return requests.stream().anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
    }

    public void save(CareerRequest careerRequest, User user){
        careerRequest.setUser(user);
        careerRequest.setIsChecked(false);
        careerRequest.setIsViewed(false);
        careerRequestRepository.save(careerRequest);

        //Invio mail di richiesta del ruolo, all'admin
        emailService.sendSimpleEmail("adminAulabpost@admin.com", "Richiesta per ruolo: " + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di " + user.getUsername());
    }

    @Override
    public void careerAccept(Long requestId) {
        //Recupero la richiesta
        CareerRequest request = careerRequestRepository.findById(requestId).get();

        //Dalla richiesta estraggo l'utente richiedente ed il ruolo richiesto
        User user = request.getUser();
        Role role = request.getRole();

        //Recupero tutti i ruoli che l'utente già possiede ed aggiunto quello nuovo
        List<Role> roleUser = user.getRoles();
        Role newRole = roleRepository.findByName(role.getName());
        roleUser.add(newRole);

        //Salvo tutte le nuove modifiche
        user.setRoles(roleUser);
        userRepository.save(user);
        request.setIsChecked(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail(user.getEmail(), "Ruolo abilitato", "Ciao, la tua richiesta di collaborazione è stata accettata dalla nostra amministrazione");
    }

    @Override
    public CareerRequest find(Long id) {
        return careerRequestRepository.findById(id).get();
    }

    @Override
    public void markAsViewed(Long id) {
    careerRequestRepository.findById(id).ifPresent(request -> {
        request.setIsViewed(true);
        careerRequestRepository.save(request);
    });
}
}