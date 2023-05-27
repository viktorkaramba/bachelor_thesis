package com.unicyb.energytaxi.controller.ranksystem;

import com.unicyb.energytaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import com.unicyb.energytaxi.services.ranksystem.UserEliteRankAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserEliteRankAchievementInfoController {

    @Autowired
    private UserEliteRankAchievementService userEliteRankAchievementService;

    @GetMapping("/api/v1/bonuses/user-elite-rank-achievements-info")
    public ResponseEntity getRanks(){
        try {
            return ResponseEntity.ok(userEliteRankAchievementService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user elite rank achievements info");
        }
    }

    @GetMapping("/api/v1/bonuses/user-elite-rank-achievements-info/{userId}/{rankId}")
    public ResponseEntity getRanksByUserIdAndRankId(@PathVariable("userId") int userId, @PathVariable("rankId") int rankId){
        try {
            return ResponseEntity.ok(userEliteRankAchievementService.getByUserIdAndRankId(userId, rankId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user rank achievements info by user id and rank id");
        }
    }

    @GetMapping("/api/v1/bonuses/user-elite-rank-achievements-info-by-driver/{userId}/{rankId}/{driverId}")
    public ResponseEntity getRanksByUserIdAndRankId(@PathVariable("userId") int userId,
                                                    @PathVariable("rankId") int rankId,
                                                    @PathVariable("driverId") int driverId){
        try {
            return ResponseEntity.ok(userEliteRankAchievementService.getByUserIdAndRankIdDriverId(userId, rankId, driverId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().
                    body("Error to get user rank achievements info by user id and rank id and driver id");
        }
    }
    @PostMapping("/api/v1/bonuses/user-elite-rank-achievements-info")
    public void save(@RequestBody UserEliteRankAchievementInfo userEliteRankAchievementInfo){
        userEliteRankAchievementService.add(userEliteRankAchievementInfo);
    }

    @PutMapping("/api/v1/bonuses/user-elite-rank-achievements-info")
    public void update(@RequestBody UserEliteRankAchievementInfo userEliteRankAchievementInfo){
        userEliteRankAchievementService.update(userEliteRankAchievementInfo);
    }

    @DeleteMapping("/api/v1/bonuses/user-elite-rank-achievements-info/{id}")
    public void delete(@PathVariable("id") int id){
        userEliteRankAchievementService.delete(id);
    }
}
