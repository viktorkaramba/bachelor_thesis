package com.unicyb.minitaxi.controller.documents;

import com.unicyb.minitaxi.database.dao.documents.RankDAOImpl;
import com.unicyb.minitaxi.ranksystem.Rank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RankController {

    private RankDAOImpl rankDAO;

    @GetMapping("/ranks")
    public ResponseEntity getRanks(){
        try {
            rankDAO = new RankDAOImpl();
            return ResponseEntity.ok(rankDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get ranks");
        }
    }

    @PostMapping("/ranks")
    public void save(@RequestBody Rank rank){
        rankDAO = new RankDAOImpl();
        rankDAO.add(rank);
    }

    @PutMapping("/ranks")
    public void update(@RequestBody Rank rank){
        rankDAO = new RankDAOImpl();
        rankDAO.update(rank);
    }

    @DeleteMapping("/ranks/{id}")
    public void delete(@PathVariable("id") int id){
        rankDAO = new RankDAOImpl();
        rankDAO.delete(id);
    }
}
