package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.energytaxi.entities.documents.CarClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarClassService {

    private final CarClassDAOImpl carClassDAO;
    @Autowired
    public CarClassService(CarClassDAOImpl carClassDAO) {
        this.carClassDAO = carClassDAO;
    }

    public boolean add(CarClass carClass) {
        return carClassDAO.add(carClass);
    }

    public List<CarClass> getAll() {
        return carClassDAO.getAll();
    }

    public CarClass getOne(int ID) {
        return carClassDAO.getOne(ID);
    }

    public boolean update(CarClass carClass) {
        return carClassDAO.update(carClass);
    }

    public boolean delete(int ID) {
        return carClassDAO.delete(ID);
    }
}
