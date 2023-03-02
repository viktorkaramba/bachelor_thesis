package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.minitaxi.entities.documents.User;
import com.unicyb.minitaxi.entities.userinterfaceenteties.LoginResponseMessage;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Message;
import com.unicyb.minitaxi.services.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Controller
public class UserController {
    @Autowired
    private WSService wsService;
    private UserDAOImpl userDAO;

    @GetMapping("/users")
    public ResponseEntity geUsers(){
        try {
            userDAO = new UserDAOImpl();
            return ResponseEntity.ok(userDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users");
        }
    }

    @PostMapping("/users")
    public void save(@RequestBody User user){
        userDAO = new UserDAOImpl();
        userDAO.add(user);
    }

    @MessageMapping("/user-registration")
    public Message getMessage(@Payload final  User user) {
        return wsService.notifyRegistryMessage(user);
    }

    @MessageMapping("/user-authorization")
    public LoginResponseMessage getAuthorizationMessage(@Payload final User user) {
        return wsService.notifyAuthorization(user);
    }

    @PutMapping("/users")
    public void update(@RequestBody User user){
        System.out.println("user: " + user);
        userDAO = new UserDAOImpl();
        userDAO.update(user);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        userDAO = new UserDAOImpl();
        userDAO.delete(id);
    }
}
