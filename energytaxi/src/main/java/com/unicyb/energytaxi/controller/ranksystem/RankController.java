package com.unicyb.energytaxi.controller.ranksystem;

import com.unicyb.energytaxi.database.dao.ranksystem.RankDAOImpl;
import com.unicyb.energytaxi.entities.ranksystem.Rank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RankController {

    private RankDAOImpl rankDAO;

    @GetMapping("/api/v1/bonuses/ranks")
    public ResponseEntity getRanks(){
        try {
            rankDAO = new RankDAOImpl();
            return ResponseEntity.ok(rankDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get ranks");
        }
    }

    @PostMapping("/api/v1/bonuses/ranks")
    public void save(@RequestBody Rank rank){
        rankDAO = new RankDAOImpl();
        rankDAO.add(rank);
    }

    @PutMapping("/api/v1/bonuses/ranks")
    public void update(@RequestBody Rank rank){
        rankDAO = new RankDAOImpl();
        rankDAO.update(rank);
    }

    @DeleteMapping("/api/v1/bonuses/ranks/{id}")
    public void delete(@PathVariable("id") int id){
        rankDAO = new RankDAOImpl();
        rankDAO.delete(id);
    }
}