package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.services.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Controller
public class UserController {
    @Autowired
    private WSService wsService;
    private UserDAOImpl userDAO;

    @GetMapping("/api/v1/documents/users")
    public ResponseEntity getUsers(){
        try {
            userDAO = new UserDAOImpl();
            return ResponseEntity.ok(userDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users");
        }
    }

    @PostMapping("/api/v1/documents/users")
    public void save(@RequestBody User user){
        userDAO = new UserDAOImpl();
        userDAO.add(user);
    }


    @PutMapping("/api/v1/documents/users")
    public void update(@RequestBody User user){
        System.out.println("user: " + user);
        userDAO = new UserDAOImpl();
        userDAO.update(user);
    }

    @DeleteMapping("/api/v1/documents/users/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        userDAO = new UserDAOImpl();
        userDAO.delete(id);
    }

    @GetMapping("/api/v1/user-app/user-stats/{userId}")
    public ResponseEntity getUsersStats(@PathVariable("userId") int userId){
        try {
            userDAO = new UserDAOImpl();
            return ResponseEntity.ok(userDAO.getUserStats(userId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users stats");
        }
    }

    @GetMapping("/api/v1/user-app/user-profile-info/{userId}")
    public ResponseEntity getUsersProfileInfo(@PathVariable("userId") int userId){
        try {
            userDAO = new UserDAOImpl();
            return ResponseEntity.ok(userDAO.getUserProfileInfo(userId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users profile info");
        }
    }

}
