package com.unicyb.minitaxi.controller.userinterface;

import com.unicyb.minitaxi.database.dao.userinterface.UserPickCarDAOImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/userpickcar")
public class UserPickCarController {
    private UserPickCarDAOImpl userPickCarDAO;
    @GetMapping()
    public ResponseEntity getUserPickCar(){
        try {
            userPickCarDAO = new UserPickCarDAOImpl();
            return ResponseEntity.ok(userPickCarDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
