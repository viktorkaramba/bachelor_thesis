package com.unicyb.energytaxi.controller.ranksystem;

import com.unicyb.energytaxi.entities.ranksystem.UserRankAchievementInfo;
import com.unicyb.energytaxi.services.ranksystem.UserRankAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserRankAchievementInfoController {

    @Autowired
    private UserRankAchievementService userRankAchievementService;

    @GetMapping("/api/v1/bonuses/user-rank-achievements-info")
    public ResponseEntity getRanks(){
        try {
            return ResponseEntity.ok(userRankAchievementService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user rank achievements info");
        }
    }

    @GetMapping("/api/v1/bonuses/user-rank-achievements-info/{userId}/{rankId}")
    public ResponseEntity getRanksByUserIdAndRankId(@PathVariable("userId") Integer userId, @PathVariable("rankId") Integer rankId){
        try {
            return ResponseEntity.ok(userRankAchievementService.getOneByUserIdAndRankId(userId, rankId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user rank achievements info by user id and rank id");
        }
    }

    @PostMapping("/api/v1/bonuses/user-rank-achievements-info")
    public void save(@RequestBody UserRankAchievementInfo userRankAchievementInfo){
        userRankAchievementService.add(userRankAchievementInfo);
    }

    @PutMapping("/api/v1/bonuses/user-rank-achievements-info")
    public void update(@RequestBody UserRankAchievementInfo userRankAchievementInfo){
        userRankAchievementService.update(userRankAchievementInfo);
    }

    @DeleteMapping("/api/v1/bonuses/user-rank-achievements-info/{id}")
    public void delete(@PathVariable("id") int id){
        userRankAchievementService.delete(id);
    }

}
