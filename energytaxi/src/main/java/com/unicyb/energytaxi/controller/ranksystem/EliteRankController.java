package com.unicyb.energytaxi.controller.ranksystem;

import com.unicyb.energytaxi.entities.ranksystem.EliteRank;
import com.unicyb.energytaxi.services.ranksystem.EliteRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class EliteRankController {

    @Autowired
    private EliteRankService eliteRankService;

    @GetMapping("/api/v1/bonuses/elite-ranks")
    public ResponseEntity getEliteRanks(){
        try {
            return ResponseEntity.ok(eliteRankService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get elite ranks");
        }
    }

    @GetMapping("/api/v1/bonuses/elite-ranks-user-info")
    public ResponseEntity getEliteRanksUserInfo(){
        try {
            return ResponseEntity.ok(eliteRankService.getAllUserInfo());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get elite ranks");
        }
    }

    @PostMapping("/api/v1/bonuses/elite-ranks")
    public void save(@RequestBody EliteRank eliteRank){
        eliteRankService.add(eliteRank);
    }

    @PutMapping("/api/v1/bonuses/elite-ranks")
    public void update(@RequestBody EliteRank eliteRank){
        eliteRankService.update(eliteRank);
    }

    @DeleteMapping("/api/v1/bonuses/elite-ranks/{id}")
    public void delete(@PathVariable("id") int id){
        eliteRankService.delete(id);
    }
}
