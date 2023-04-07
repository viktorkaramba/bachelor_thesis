package com.unicyb.minitaxi.controller.bonuses;

import com.unicyb.minitaxi.database.dao.bonuses.MilitaryBonusesDAOImpl;
import com.unicyb.minitaxi.entities.bonuses.MilitaryBonuses;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class MilitaryBonusesController {
    private MilitaryBonusesDAOImpl militaryBonusesDAO;

    @GetMapping("/military-bonuses")
    public ResponseEntity getMilitaryBonuses(){
        try {
            militaryBonusesDAO = new MilitaryBonusesDAOImpl();
            return ResponseEntity.ok(militaryBonusesDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get military bonuses");
        }
    }

    @GetMapping("/military-bonuses/{userId}")
    public ResponseEntity getMilitaryBonusesByUserId(@PathVariable("userId") Integer userId){
        try {
            militaryBonusesDAO = new MilitaryBonusesDAOImpl();
            MilitaryBonuses militaryBonuses = militaryBonusesDAO.getOneByUserIdWithoutPhoto(userId);
            if(militaryBonuses == null) {
                militaryBonuses = new MilitaryBonuses();
                militaryBonuses.setMilitaryBonusesId(0);
                militaryBonuses.setSaleValue(5);
                return ResponseEntity.ok(militaryBonuses);
            }
            return ResponseEntity.ok(militaryBonuses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get military bonuses by user Id");
        }
    }

    @PostMapping("/military-bonuses")
    public ResponseEntity save(@RequestBody MilitaryBonuses militaryBonuses){
        try {
            militaryBonuses.setDate(new Timestamp(new Date().getTime()));
            militaryBonusesDAO = new MilitaryBonusesDAOImpl();
            militaryBonusesDAO.add(militaryBonuses);
            return ResponseEntity.ok(new Message("success"));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to add military bonuses");
        }
    }

    @PutMapping("/military-bonuses")
    public void update(@RequestBody MilitaryBonuses militaryBonuses){
        militaryBonusesDAO = new MilitaryBonusesDAOImpl();
        militaryBonusesDAO.update(militaryBonuses);
    }

    @DeleteMapping("/military-bonuses/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id){
        try {
            militaryBonusesDAO = new MilitaryBonusesDAOImpl();
            militaryBonusesDAO.delete(id);
            return ResponseEntity.ok(new Message("success"));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to delete military bonuses");
        }
    }
}
