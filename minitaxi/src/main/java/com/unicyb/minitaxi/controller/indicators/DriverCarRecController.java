package com.unicyb.minitaxi.controller.indicators;

import com.unicyb.minitaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.CarsDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.minitaxi.database.dao.indicators.DriverCarRecommendationsDAOImpl;
import com.unicyb.minitaxi.entities.documents.Car;
import com.unicyb.minitaxi.entities.documents.CarClass;
import com.unicyb.minitaxi.entities.documents.Driver;
import com.unicyb.minitaxi.entities.indicators.DriverCarRecommendations;
import com.unicyb.minitaxi.entities.indicators.STATUS;
import com.unicyb.minitaxi.entities.userinterfaceenteties.*;
import com.unicyb.minitaxi.services.SalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class DriverCarRecController {

    private DriverCarRecommendationsDAOImpl driverCarRecDAO;
    private CarsDAOImpl carsDAO;
    private CarClassDAOImpl carClassDAO;
    private DriverDAOImpl driverDAO;

    @GetMapping("/drivers-cars-recommendations")
    public ResponseEntity getDriverCarRec(){
        try {
            driverCarRecDAO = new DriverCarRecommendationsDAOImpl();
            return ResponseEntity.ok(driverCarRecDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver car recommendations");
        }
    }

    @PostMapping("/drivers-cars-recommendations-report")
    public ResponseEntity getReportDriverCarRec(@RequestBody Report report){
        try {
            driverCarRecDAO = new DriverCarRecommendationsDAOImpl();
            return ResponseEntity.ok(driverCarRecDAO.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report driver car recommendations");
        }
    }

    @PostMapping("/drivers-cars-recommendations-report-by-id")
    public ResponseEntity getReportByIdDriverCarRec(@RequestBody Report report){
        try {
            driverCarRecDAO = new DriverCarRecommendationsDAOImpl();
            return ResponseEntity.ok(driverCarRecDAO.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id driver car recommendations");
        }
    }

    @GetMapping("/drivers-cars-recommendations/{id}")
    public ResponseEntity getCarRecommendationInfoDriver(@PathVariable String id){
        try {
            driverCarRecDAO = new DriverCarRecommendationsDAOImpl();
            driverDAO = new DriverDAOImpl();
            carsDAO = new CarsDAOImpl();
            carClassDAO = new CarClassDAOImpl();
            DriverCarRecommendations driverCarRecommendations = driverCarRecDAO.getOneByDriverId(Integer.parseInt(id));
            System.out.println(driverCarRecommendations);
            CarRecommendationInfo carRecommendationInfo = null;
            int driverId = driverCarRecommendations.getDriverId();
            Driver driver = driverDAO.getOne(driverId);
            Car car = carsDAO.getOne(driverCarRecommendations.getCarId());
            CarClass carClass = carClassDAO.getOne(car.getCcID());
            carRecommendationInfo = new CarRecommendationInfo(driverCarRecommendations.getDcrId(), driverId, car.getCarId(), car.getProducer(),
                    car.getBrand(), carClass.getPrice(), carClass.getName(), SalaryService.getNewSalary(driver.getSalary()));
            return ResponseEntity.ok(carRecommendationInfo);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver car recommendations");
        }
    }

    @PostMapping("/drivers-cars-recommendations-driver-answer")
    public Message addDriverAnswer(@RequestBody DriverRecAnswer driverRecAnswer){
        DriverCarRecommendations driverCarRecommendations = null;
        driverCarRecDAO = new DriverCarRecommendationsDAOImpl();
        driverDAO = new DriverDAOImpl();
        carsDAO = new CarsDAOImpl();
        carClassDAO = new CarClassDAOImpl();
        if (driverRecAnswer.getAnswer().equals("Yes")){
            driverCarRecommendations = new DriverCarRecommendations(driverRecAnswer.getDcrId(),
                    new Timestamp(new Date().getTime()), driverRecAnswer.getDriverId(), driverRecAnswer.getCarId(),
                    STATUS.COMPLETE);
            driverCarRecDAO.update(driverCarRecommendations);
            Driver driver = driverDAO.getOne(driverRecAnswer.getDriverId());
            Car oldCar = carsDAO.getOne(driver.getCarId());
            Car newCar = carsDAO.getOne(driverRecAnswer.getCarId());
            oldCar.setInUse("NO");
            newCar.setInUse("YES");
            carsDAO.update(oldCar);
            carsDAO.update(newCar);
            driver.setCarId(driverRecAnswer.getCarId());
            driver.setSalary(driverRecAnswer.getNewSalary());
            driverDAO.update(driver);
        }else {
            driverCarRecommendations = new DriverCarRecommendations(driverRecAnswer.getDcrId(),
                    new Timestamp(new Date().getTime()), driverRecAnswer.getDriverId(), driverRecAnswer.getCarId(),
                    STATUS.REJECT);
            driverCarRecDAO.update(driverCarRecommendations);
        }
        return new Message("Successfully update");
    }
}
