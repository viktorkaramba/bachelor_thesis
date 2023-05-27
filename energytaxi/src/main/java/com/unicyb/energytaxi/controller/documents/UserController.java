package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.services.other.WSService;
import com.unicyb.energytaxi.services.documents.UserService;
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

    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/documents/users")
    public ResponseEntity getUsers(){
        try {
            return ResponseEntity.ok(userService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users");
        }
    }

    @PostMapping("/api/v1/documents/users")
    public void save(@RequestBody User user){
        userService.add(user);
    }


    @PutMapping("/api/v1/documents/users")
    public void update(@RequestBody User user){
        userService.update(user);
    }

    @DeleteMapping("/api/v1/documents/users/{id}")
    public void delete(@PathVariable("id") int id){
        userService.delete(id);
    }

    @GetMapping("/api/v1/user-app/user-stats/{userId}")
    public ResponseEntity getUsersStats(@PathVariable("userId") int userId){
        try {
            return ResponseEntity.ok(userService.getUserStats(userId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users stats");
        }
    }

    @GetMapping("/api/v1/user-app/user-profile-info/{userId}")
    public ResponseEntity getUsersProfileInfo(@PathVariable("userId") int userId){
        try {
            return ResponseEntity.ok(userService.getUserProfileInfo(userId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get users profile info");
        }
    }

}
