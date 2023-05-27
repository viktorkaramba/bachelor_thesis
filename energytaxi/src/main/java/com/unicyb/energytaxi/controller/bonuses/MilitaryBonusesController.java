package com.unicyb.energytaxi.controller.bonuses;

import com.unicyb.energytaxi.entities.bonuses.MILITARY_BONUS_STATUS;
import com.unicyb.energytaxi.entities.bonuses.MilitaryBonuses;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.services.bonuses.MilitaryBonusesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class MilitaryBonusesController {

    @Autowired
    private MilitaryBonusesService militaryBonusesService;

    @GetMapping("/api/v1/bonuses/military-bonuses")
    public ResponseEntity getMilitaryBonuses(){
        try {
            return ResponseEntity.ok(militaryBonusesService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get military bonuses");
        }
    }

    @GetMapping("/api/v1/bonuses/military-bonuses-waiting")
    public ResponseEntity getMilitaryBonusesWaiting(){
        try {
            return ResponseEntity.ok(militaryBonusesService.getAllByStatus(STATUS.WAITING));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get military bonuses");
        }
    }

    @GetMapping("/api/v1/bonuses/military-bonuses/{userId}")
    public ResponseEntity getMilitaryBonusesByUserId(@PathVariable("userId") Integer userId){
        try {
            MilitaryBonuses militaryBonuses = militaryBonusesService.getOneByUserIdWithoutPhoto(userId);
            if(militaryBonuses == null) {
                militaryBonuses = new MilitaryBonuses();
                militaryBonuses.setMilitaryBonusesId(0);
                militaryBonuses.setSaleValue(5);
                militaryBonuses.setMilitaryBonusStatus(MILITARY_BONUS_STATUS.REJECT);
                return ResponseEntity.ok(militaryBonuses);
            }
            return ResponseEntity.ok(militaryBonuses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get military bonuses by user Id");
        }
    }

    @PostMapping("/api/v1/bonuses/military-bonuses")
    public ResponseEntity save(@RequestBody MilitaryBonuses militaryBonuses){
        try {
            militaryBonuses.setDate(new Timestamp(new Date().getTime()));
            militaryBonusesService.add(militaryBonuses);
            return ResponseEntity.ok(new MyMessage("success"));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to add military bonuses");
        }
    }

    @PutMapping("/api/v1/bonuses/military-bonuses")
    public void update(@RequestBody MilitaryBonuses militaryBonuses){
        militaryBonusesService.update(militaryBonuses);
    }

    @DeleteMapping("/api/v1/bonuses/military-bonuses/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id){
        try {
            militaryBonusesService.delete(id);
            return ResponseEntity.ok(new MyMessage("success"));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to delete military bonuses");
        }
    }
}
