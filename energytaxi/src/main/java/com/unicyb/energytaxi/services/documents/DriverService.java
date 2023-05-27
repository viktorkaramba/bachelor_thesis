package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.entities.usersinfo.DriverInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    private final DriverDAOImpl driverDAO;
    @Autowired
    public DriverService(DriverDAOImpl driverDAO) {
        this.driverDAO = driverDAO;
    }

    public boolean add(Driver driver) {
       return driverDAO.add(driver);
    }

    public List<Driver> getAll() {
       return driverDAO.getAll();
    }

    public List<Driver> getAllByResumeStatus(STATUS driverStatus) {
       return driverDAO.getAllByResumeStatus(driverStatus);
    }

    public Driver getOne(int ID) {
        return driverDAO.getOne(ID);
    }

    public boolean update(Driver driver) {
        return driverDAO.update(driver);
    }

    public boolean updateByUsername(String oldUserName, String newUserName) {
       return driverDAO.updateByUsername(oldUserName, newUserName);
    }

    public boolean delete(int ID) {
      return driverDAO.delete(ID);
    }

    public Driver getByDriverUserName(String username){
        return driverDAO.getByDriverUserName(username);
    }

    public DriverInfo getDriverInfo(int ID){
        return driverDAO.getDriverInfo(ID);
    }

}
