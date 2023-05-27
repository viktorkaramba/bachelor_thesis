package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.documents.CarsDAOImpl;
import com.unicyb.energytaxi.entities.documents.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarsDAOImpl carsDAO;
    @Autowired
    public CarService(CarsDAOImpl carsDAO) {
        this.carsDAO = carsDAO;
    }

    public boolean add(Car car) {
        return carsDAO.add(car);
    }

    public List<Car> getAll() {
        return carsDAO.getAll();
    }

    public List<Car> getAllByCarClass(int ID) {
      return carsDAO.getAllByCarClass(ID);
    }

    public Car getOne(int ID) {
        return carsDAO.getOne(ID);
    }

    public boolean update(Car car) {
        return carsDAO.update(car);
    }

    public boolean updateCarUse(int carID, String status) {
       return carsDAO.updateCarUse(carID, status);
    }

    public boolean delete(int ID) {
        return carsDAO.delete(ID);
    }
}
