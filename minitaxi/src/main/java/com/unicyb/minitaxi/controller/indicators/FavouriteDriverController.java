package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.userinterface.FavouriteDriverDAOImpl;
import com.unicyb.minitaxi.entities.usersinfo.FavouriteDriver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FavouriteDriverController {
    private FavouriteDriverDAOImpl favouriteDriverDAO;

    @GetMapping("/favourite_drivers/{id}")
    public ResponseEntity getFavouriteDriversByUserId(@PathVariable("id") int id){
        try {
            favouriteDriverDAO = new FavouriteDriverDAOImpl();
            return ResponseEntity.ok(favouriteDriverDAO.getAllByUserId(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get favourite drivers");
        }
    }


    @PostMapping("/favourite_drivers")
    public void save(@RequestBody FavouriteDriver favouriteDriver){
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        favouriteDriverDAO.add(favouriteDriver);
    }

    @PutMapping("/favourite_drivers")
    public void update(@RequestBody FavouriteDriver favouriteDriver){
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        favouriteDriverDAO.update(favouriteDriver);
    }


    @DeleteMapping("/favourite_drivers/{id}")
    public void delete(@PathVariable("id") int id){
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        favouriteDriverDAO.delete(id);
    }
}
