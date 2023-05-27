package com.unicyb.energytaxi.services.userinterface;

import com.unicyb.energytaxi.database.dao.userinterface.DriverProfileInfoDAOImpl;
import com.unicyb.energytaxi.entities.userinterfaceenteties.DriverProfileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverProfileInfoService {
    private final DriverProfileInfoDAOImpl driverProfileInfoDAO;
    @Autowired
    public DriverProfileInfoService(DriverProfileInfoDAOImpl driverProfileInfoDAO) {
        this.driverProfileInfoDAO = driverProfileInfoDAO;
    }

    public DriverProfileInfo getByDriverId(int ID){
        return driverProfileInfoDAO.getByDriverId(ID);
    }
}
