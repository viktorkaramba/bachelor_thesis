package com.unicyb.minitaxi.controller.ranksystem;

import com.unicyb.minitaxi.database.dao.ranksystem.EliteRankDAOImpl;
import com.unicyb.minitaxi.entities.ranksystem.EliteRank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class EliteRankController {

    private EliteRankDAOImpl eliteRankDAO;

    @GetMapping("/api/v1/bonuses/elite-ranks")
    public ResponseEntity getEliteRanks(){
        try {
            eliteRankDAO = new EliteRankDAOImpl();
            return ResponseEntity.ok(eliteRankDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get elite ranks");
        }
    }

    @GetMapping("/api/v1/bonuses/elite-ranks-user-info")
    public ResponseEntity getEliteRanksUserInfo(){
        try {
            eliteRankDAO = new EliteRankDAOImpl();
            return ResponseEntity.ok(eliteRankDAO.getAllUserInfo());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get elite ranks");
        }
    }

    @PostMapping("/api/v1/bonuses/elite-ranks")
    public void save(@RequestBody EliteRank eliteRank){
        eliteRankDAO = new EliteRankDAOImpl();
        eliteRankDAO.add(eliteRank);
    }

    @PutMapping("/api/v1/bonuses/elite-ranks")
    public void update(@RequestBody EliteRank eliteRank){
        eliteRankDAO = new EliteRankDAOImpl();
        eliteRankDAO.update(eliteRank);
    }

    @DeleteMapping("/api/v1/bonuses/elite-ranks/{id}")
    public void delete(@PathVariable("id") int id){
        eliteRankDAO = new EliteRankDAOImpl();
        eliteRankDAO.delete(id);
    }
}
