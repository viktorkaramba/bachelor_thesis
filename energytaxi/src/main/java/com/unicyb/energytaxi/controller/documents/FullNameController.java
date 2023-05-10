package com.unicyb.energytaxi.controller.documents;

import com.unicyb.energytaxi.database.dao.documents.FullNameDAOImpl;
import com.unicyb.energytaxi.entities.documents.FullName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FullNameController {
    private FullNameDAOImpl fullNameDAO;

    @GetMapping("/api/v1/documents/fullnames")
    public ResponseEntity getFullNames(){
        try {
            fullNameDAO = new FullNameDAOImpl();
            return ResponseEntity.ok(fullNameDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get fullnames");
        }
    }

    @PostMapping("/api/v1/documents/fullnames")
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

    @PutMapping("/api/v1/documents/fullnames")
    public void delete(@RequestBody FullName fullName){
        System.out.println("fullName: " + fullName);
        fullNameDAO = new FullNameDAOImpl();
        fullNameDAO.update(fullName);
    }

    @DeleteMapping("/api/v1/documents/fullnames/{id}")
    public void delete(@PathVariable("id") int id){
        System.out.println("id: " + id);
        fullNameDAO = new FullNameDAOImpl();
        fullNameDAO.delete(id);
    }
}
