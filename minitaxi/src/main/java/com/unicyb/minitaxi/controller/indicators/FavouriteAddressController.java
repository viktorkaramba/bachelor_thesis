package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.userinterface.FavouriteAddressDAOImpl;
import com.unicyb.minitaxi.entities.indicators.FavouriteAddress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FavouriteAddressController {
    private FavouriteAddressDAOImpl favouriteAddressDAO;

    @GetMapping("/favourite-addresses/{id}")
    public ResponseEntity getFavouriteAddressesByUserId(@PathVariable("id") int id){
        try {
            favouriteAddressDAO = new FavouriteAddressDAOImpl();
            return ResponseEntity.ok(favouriteAddressDAO.getAllByUserId(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get favourite addresses");
        }
    }


    @PostMapping("/favourite-addresses")
    public void save(@RequestBody FavouriteAddress favouriteAddress){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.add(favouriteAddress);
    }

    @PutMapping("/favourite-addresses")
    public void update(@RequestBody FavouriteAddress favouriteAddress){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.update(favouriteAddress);
    }

    @DeleteMapping("/favourite-addresses/{id}")
    public void delete(@PathVariable("id") int id){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.delete(id);
    }

    @DeleteMapping("/favourite-addresses-by-userId-address/{userId}/{address}")
    public void deleteByUserIdAndAddress(@PathVariable("userId") int id, @PathVariable("address") String address){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.deleteByUserIdAndAddress(id, address);
    }
}
