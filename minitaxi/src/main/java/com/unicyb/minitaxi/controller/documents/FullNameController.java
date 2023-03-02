package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.FullNameDAOImpl;
import com.unicyb.minitaxi.entities.documents.FullName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FullNameController {
    private FullNameDAOImpl fullNameDAO;

    @GetMapping("/fullnames")
    public ResponseEntity getFullNames(){
        try {
            fullNameDAO = new FullNameDAOImpl();
            return ResponseEntity.ok(fullNameDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get fullnames");
        }
    }

    @PostMapping("/fullnames")
    public ResponseEntity save(@RequestBody FullName fullName){
        try {
            fullNameDAO = new FullNameDAOImpl();
            fullNameDAO.add(fullName);
            return ResponseEntity.ok("Fullname successfully added");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to add fullname");
        }
    }

    @PutMapping("/fullnames")
    public void delete(@RequestBody FullName fullName){
        System.out.println("fullName: " + fullName);
        fullNameDAO = new FullNameDAOImpl();
        fullNameDAO.update(fullName);
    }

    @DeleteMapping("/fullnames/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        fullNameDAO = new FullNameDAOImpl();
        fullNameDAO.delete(id);
    }
}
