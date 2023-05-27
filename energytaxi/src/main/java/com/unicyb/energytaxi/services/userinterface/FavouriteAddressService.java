package com.unicyb.energytaxi.services.userinterface;

import com.unicyb.energytaxi.database.dao.userinterface.FavouriteAddressDAOImpl;
import com.unicyb.energytaxi.entities.indicators.FavouriteAddress;
import com.unicyb.energytaxi.entities.userinterfaceenteties.FavouriteAddressUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteAddressService {
    private final FavouriteAddressDAOImpl favouriteAddressDAO;
    @Autowired
    public FavouriteAddressService(FavouriteAddressDAOImpl favouriteAddressDAO) {
        this.favouriteAddressDAO = favouriteAddressDAO;
    }

    public boolean add(FavouriteAddress favouriteAddress) {
        return favouriteAddressDAO.add(favouriteAddress);
    }

    public List<FavouriteAddressUserInfo> getAllByUserId(int userId){
        return favouriteAddressDAO.getAllByUserId(userId);
    }

    public List<FavouriteAddress> getAll() {
        return favouriteAddressDAO.getAll();
    }

    public boolean update(FavouriteAddress favouriteAddress) {
        return favouriteAddressDAO.update(favouriteAddress);
    }

    public boolean delete(int ID) {
        return favouriteAddressDAO.delete(ID);
    }

    public boolean deleteByUserIdAndAddress(int userId, String address) {
        return favouriteAddressDAO.deleteByUserIdAndAddress(userId, address);
    }
}
