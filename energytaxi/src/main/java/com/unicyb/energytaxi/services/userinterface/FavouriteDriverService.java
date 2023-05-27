package com.unicyb.energytaxi.services.userinterface;

import com.unicyb.energytaxi.database.dao.userinterface.FavouriteDriverDAOImpl;
import com.unicyb.energytaxi.entities.indicators.FavouriteDriver;
import com.unicyb.energytaxi.entities.userinterfaceenteties.FavouriteDriverUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteDriverService {
    private final FavouriteDriverDAOImpl favouriteDriverDAO;
    @Autowired
    public FavouriteDriverService(FavouriteDriverDAOImpl favouriteDriverDAO) {
        this.favouriteDriverDAO = favouriteDriverDAO;
    }

    public boolean add(FavouriteDriver favouriteDriver) {
        return favouriteDriverDAO.add(favouriteDriver);
    }

    public List<FavouriteDriverUserInfo> getAllByUserId(int userId){
        return favouriteDriverDAO.getAllByUserId(userId);
    }

    public List<FavouriteDriver> getAll() {
        return favouriteDriverDAO.getAll();
    }

    public boolean update(FavouriteDriver favouriteDriver) {
        return favouriteDriverDAO.update(favouriteDriver);
    }

    public boolean delete(int ID) {
        return favouriteDriverDAO.delete(ID);
    }

    public boolean deleteByUserId(int driverId, int userId){
        return favouriteDriverDAO.deleteByUserId(driverId, userId);
    }
}
