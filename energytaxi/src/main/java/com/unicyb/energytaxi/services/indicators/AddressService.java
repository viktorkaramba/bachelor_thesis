package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.AddressDAOImpl;
import com.unicyb.energytaxi.entities.indicators.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressDAOImpl addressDAO;
    @Autowired
    public AddressService(AddressDAOImpl addressDAO) {
        this.addressDAO = addressDAO;
    }

    public List<Address> getAll() {
      return addressDAO.getAll();
    }
}
