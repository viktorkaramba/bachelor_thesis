package com.unicyb.energytaxi.controller.ranksystem;

import com.unicyb.energytaxi.entities.ranksystem.Rank;
import com.unicyb.energytaxi.services.ranksystem.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RankController {

    @Autowired
    private RankService rankService;

    @GetMapping("/api/v1/bonuses/ranks")
    public ResponseEntity getRanks(){
        try {
            return ResponseEntity.ok(rankService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get ranks");
        }
    }

    @PostMapping("/api/v1/bonuses/ranks")
    public void save(@RequestBody Rank rank){
        rankService.add(rank);
    }

    @PutMapping("/api/v1/bonuses/ranks")
    public void update(@RequestBody Rank rank){
        rankService.update(rank);
    }

    @DeleteMapping("/api/v1/bonuses/ranks/{id}")
    public void delete(@PathVariable("id") int id){
        rankService.delete(id);
    }
}
