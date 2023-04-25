package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.userinterface.FavouriteDriverDAOImpl;
import com.unicyb.minitaxi.entities.indicators.FavouriteDriver;
import com.unicyb.minitaxi.entities.userinterfaceenteties.MyMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FavouriteDriverController {
    private FavouriteDriverDAOImpl favouriteDriverDAO;

    @GetMapping("/api/v1/indicators/favourite-drivers")
    public ResponseEntity getAllFavouriteDrivers(){
        try {
            favouriteDriverDAO = new FavouriteDriverDAOImpl();
            return ResponseEntity.ok(favouriteDriverDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get all favourite drivers");
        }
    }

    @GetMapping("/api/v1/indicators/favourite-drivers/{id}")
    public ResponseEntity getFavouriteDriversByUserId(@PathVariable("id") int id){
        try {
            favouriteDriverDAO = new FavouriteDriverDAOImpl();
            return ResponseEntity.ok(favouriteDriverDAO.getAllByUserId(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get favourite drivers");
        }
    }

    @PostMapping("/api/v1/user-app/favourite-drivers")
    public ResponseEntity save(@RequestBody FavouriteDriver favouriteDriver){
        System.out.println(favouriteDriver);
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        if(favouriteDriverDAO.add(favouriteDriver)){
            return ResponseEntity.ok(new MyMessage("Successfully added favourite driver"));
        }
        else {
            return ResponseEntity.badRequest().body(new MyMessage("Error added favourite driver"));
        }
    }

    @PutMapping("/api/v1/indicators/favourite-drivers")
    public void update(@RequestBody FavouriteDriver favouriteDriver){
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        favouriteDriverDAO.update(favouriteDriver);
    }


    @DeleteMapping("/api/v1/indicators/favourite-drivers/{id}")
    public void delete(@PathVariable("id") int id){
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        favouriteDriverDAO.delete(id);
    }

    @GetMapping("/api/v1/user-app/favourite-drivers/{id}")
    public ResponseEntity getFavouriteDriversByUserIdUserApp(@PathVariable("id") int id){
        System.out.println(id);
        try {
            favouriteDriverDAO = new FavouriteDriverDAOImpl();
            System.out.println(favouriteDriverDAO.getAllByUserId(id));
            return ResponseEntity.ok(favouriteDriverDAO.getAllByUserId(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get favourite drivers");
        }
    }

    @DeleteMapping("/api/v1/user-app/favourite-drivers-by-driverId-userId/{driverId}/{userId}")
    public ResponseEntity deleteByUserId(@PathVariable("driverId") int driverId, @PathVariable("userId") int userId){
        favouriteDriverDAO = new FavouriteDriverDAOImpl();
        if(favouriteDriverDAO.deleteByUserId(driverId, userId)){
            return ResponseEntity.ok(new MyMessage("Successfully delete favourite driver"));
        }
        else {
            return ResponseEntity.badRequest().body(new MyMessage("Error delete favourite driver"));
        }
    }
}
