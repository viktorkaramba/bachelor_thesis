package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.database.dao.userinterface.FavouriteAddressDAOImpl;
import com.unicyb.energytaxi.entities.indicators.FavouriteAddress;
import com.unicyb.energytaxi.entities.userinterfaceenteties.FavouriteAddressUserInfo;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class FavouriteAddressController {
    private FavouriteAddressDAOImpl favouriteAddressDAO;

    @GetMapping("/api/v1/indicators/favourite-addresses")
    public ResponseEntity getAllFavouriteAddresses(){
        try {
            favouriteAddressDAO = new FavouriteAddressDAOImpl();
            return ResponseEntity.ok(favouriteAddressDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get all favourite addresses");
        }
    }
    @GetMapping("/api/v1/indicators/favourite-addresses/{id}")
    public ResponseEntity getFavouriteAddressesByUserId(@PathVariable("id") int id){
        try {
            favouriteAddressDAO = new FavouriteAddressDAOImpl();
            return ResponseEntity.ok(favouriteAddressDAO.getAllByUserId(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get favourite addresses");
        }
    }

    @GetMapping("/api/v1/user-app/favourite-addresses/{id}")
    public ResponseEntity getFavouriteAddressesByUserIdUserApp(@PathVariable("id") int id){
        System.out.println(id);
        try {
            favouriteAddressDAO = new FavouriteAddressDAOImpl();
            List<FavouriteAddressUserInfo> favouriteAddressUserInfo = favouriteAddressDAO.getAllByUserId(id);
            System.out.println(favouriteAddressUserInfo);
            return ResponseEntity.ok(favouriteAddressUserInfo);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get favourite addresses");
        }
    }

    @PostMapping("/api/v1/indicators/favourite-addresses")
    public void save(@RequestBody FavouriteAddress favouriteAddress){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.add(favouriteAddress);
    }

    @PostMapping("/api/v1/user-app/favourite-addresses")
    public ResponseEntity saveUserApp(@RequestBody FavouriteAddress favouriteAddress){
        System.out.println(favouriteAddress);
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        if(favouriteAddressDAO.add(favouriteAddress)){
            return ResponseEntity.ok(new MyMessage("Successfully added favourite address"));
        }
        else {
            return ResponseEntity.badRequest().body(new MyMessage("Error added favourite address"));
        }
    }

    @PutMapping("/api/v1/user-app/favourite-addresses")
    public void update(@RequestBody FavouriteAddress favouriteAddress){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.update(favouriteAddress);
    }

    @DeleteMapping("/api/v1/indicators/favourite-addresses/{id}")
    public void delete(@PathVariable("id") int id){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        favouriteAddressDAO.delete(id);
    }

    @DeleteMapping("/api/v1/user-app/favourite-addresses-by-userId-address/{userId}/{address}")
    public ResponseEntity deleteByUserIdAndAddress(@PathVariable("userId") int id,
                                                   @PathVariable("address") String address){
        favouriteAddressDAO = new FavouriteAddressDAOImpl();
        if(favouriteAddressDAO.deleteByUserIdAndAddress(id, address)){
            return ResponseEntity.ok(new MyMessage("Successfully delete favourite address"));
        }
        else{
            return ResponseEntity.badRequest().body(new MyMessage("Error delete favourite address"));
        }
    }
}
