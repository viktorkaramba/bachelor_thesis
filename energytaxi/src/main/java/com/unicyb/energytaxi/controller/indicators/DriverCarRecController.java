package com.unicyb.energytaxi.controller.indicators;

import com.unicyb.energytaxi.entities.documents.CAR_CLASSES;
import com.unicyb.energytaxi.entities.documents.Car;
import com.unicyb.energytaxi.entities.documents.CarClass;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.indicators.DriverCarRecommendations;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.entities.userinterfaceenteties.CarRecommendationInfo;
import com.unicyb.energytaxi.entities.userinterfaceenteties.DriverRecAnswer;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.services.other.SalaryService;
import com.unicyb.energytaxi.services.documents.CarClassService;
import com.unicyb.energytaxi.services.documents.CarService;
import com.unicyb.energytaxi.services.documents.DriverService;
import com.unicyb.energytaxi.services.indicators.DriverCarRecommendationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin
public class DriverCarRecController {

    @Autowired
    private DriverCarRecommendationsService driverCarRecommendationsService;

    @Autowired
    private CarService carService;

    @Autowired
    private CarClassService carClassService;

    @Autowired
    private DriverService driverService;

    @GetMapping("/api/v1/indicators/drivers-cars-recommendations")
    public ResponseEntity getDriverCarRec(){
        try {
            return ResponseEntity.ok(driverCarRecommendationsService.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver car recommendations");
        }
    }

    @PostMapping("/api/v1/indicators/drivers-cars-recommendations-report")
    public ResponseEntity getReportDriverCarRec(@RequestBody Report report){
        try {
            return ResponseEntity.ok(driverCarRecommendationsService.getReport(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report driver car recommendations");
        }
    }

    @PostMapping("/api/v1/indicators/drivers-cars-recommendations-report-by-id")
    public ResponseEntity getReportByIdDriverCarRec(@RequestBody Report report){
        try {
            return ResponseEntity.ok(driverCarRecommendationsService.getReportById(report));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get report by id driver car recommendations");
        }
    }

    @GetMapping("/api/v1/driver-app/drivers-cars-recommendations/{id}")
    public ResponseEntity getCarRecommendationInfoDriver(@PathVariable String id){
        try {
            DriverCarRecommendations driverCarRecommendations = driverCarRecommendationsService.getOneByDriverId(Integer.parseInt(id));
            int driverId = driverCarRecommendations.getDriverId();
            Driver driver = driverService.getOne(driverId);
            Car car = carService.getOne(driverCarRecommendations.getCarId());
            CarClass carClass = carClassService.getOne(car.getCcID());
            CarRecommendationInfo carRecommendationInfo = new CarRecommendationInfo(driverCarRecommendations.getDcrId(), driverId, car.getCarId(), car.getProducer(),
                    car.getBrand(), carClass.getPrice(), CAR_CLASSES.valueOf(carClass.getName()), SalaryService.getNewSalary(driver.getSalary()));
            return ResponseEntity.ok(carRecommendationInfo);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get driver car recommendations");
        }
    }

    @PostMapping("/api/v1/driver-app/drivers-cars-recommendations-driver-answer")
    public MyMessage addDriverAnswer(@RequestBody DriverRecAnswer driverRecAnswer){
        DriverCarRecommendations driverCarRecommendations = null;
        if (driverRecAnswer.getAnswer().equals("Yes")){
            driverCarRecommendations = new DriverCarRecommendations(driverRecAnswer.getDcrId(),
                    new Timestamp(new Date().getTime()), driverRecAnswer.getDriverId(), driverRecAnswer.getCarId(),
                    STATUS.COMPLETE);
            driverCarRecommendationsService.update(driverCarRecommendations);
            Driver driver = driverService.getOne(driverRecAnswer.getDriverId());
            Car oldCar = carService.getOne(driver.getCarId());
            Car newCar = carService.getOne(driverRecAnswer.getCarId());
            oldCar.setInUse("NO");
            newCar.setInUse("YES");
            carService.update(oldCar);
            carService.update(newCar);
            driver.setCarId(driverRecAnswer.getCarId());
            driver.setSalary(driverRecAnswer.getNewSalary());
            driverService.update(driver);
        }else {
            driverCarRecommendations = new DriverCarRecommendations(driverRecAnswer.getDcrId(),
                    new Timestamp(new Date().getTime()), driverRecAnswer.getDriverId(), driverRecAnswer.getCarId(),
                    STATUS.REJECT);
            driverCarRecommendationsService.update(driverCarRecommendations);
        }
        return new MyMessage("Successfully update");
    }
}
