package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.DriverCarRecommendationsDAOImpl;
import com.unicyb.energytaxi.entities.documents.Car;
import com.unicyb.energytaxi.entities.documents.CarClass;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.indicators.DriverCarRecommendations;
import com.unicyb.energytaxi.entities.indicators.DriverRating;
import com.unicyb.energytaxi.entities.indicators.NumberOfKilometers;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.services.documents.CarClassService;
import com.unicyb.energytaxi.services.documents.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DriverCarRecommendationsService {

    @Autowired
    private CarService carService;

    @Autowired
    private DriverRatingService driverRatingService;

    @Autowired
    private CarClassService carClassService;

    @Autowired
    private NumberOfKilometersService numberOfKilometersService;
    private final DriverCarRecommendationsDAOImpl driverCarRecommendationsDAO;
    @Autowired
    public DriverCarRecommendationsService(DriverCarRecommendationsDAOImpl driverCarRecommendationsDAO) {
        this.driverCarRecommendationsDAO = driverCarRecommendationsDAO;
    }

    public void isApproach(Driver driver, Timestamp date){
        Car car = carService.getOne(driver.getCarId());
        List<CarClass> classClassList = carClassService.getAll();
        int index = getIndex(car, classClassList) + 1;
        float driverRating = getAverageDriverRating(driver.getDriverId());
        float driverKilometers = getDriverNumbersOfKilometers(driver.getDriverId());
        for(int i = index; i < classClassList.size(); i++){
            if(driver.getExperience()>=classClassList.get(i).getMinExperience()
                    && driverRating>=classClassList.get(i).getMinRating() &&
                    driverKilometers>= classClassList.get(i).getMinNumberOfKilometers()){
                List<Car> approachCars = carService.getAllByCarClass(classClassList.get(i).getCcId());
                if(approachCars.size() != 0){
                    driverCarRecommendationsDAO.add(new DriverCarRecommendations(date, driver.getDriverId(),
                            approachCars.get(0).getCarId(), STATUS.WAITING));
                }
            }
        }
    }

    private int getIndex(Car car, List<CarClass> classClassList) {
        for(int i = 0; i < classClassList.size(); i++){
            if(car.getCarId() == classClassList.get(i).getCcId()){
                return i;
            }
        }
        return -1;
    }

    private float getAverageDriverRating(int ID){
        float result = 0;
        List<DriverRating> driverRatingList = driverRatingService.getAllByDriverId(ID);
        for (DriverRating driverRating: driverRatingList){
            result+=driverRating.getRating();
        }
        return result / driverRatingList.size();
    }
    private float getDriverNumbersOfKilometers(int ID){
        float result = 0;
        List<NumberOfKilometers> numberOfKilometersList = numberOfKilometersService.getAllByDriverId(ID);
        for (NumberOfKilometers numberOfKilometers: numberOfKilometersList){
            result+=numberOfKilometers.getNumbers();
        }
        return result;
    }

    public boolean add(DriverCarRecommendations driverCarRecommendations) {
       return driverCarRecommendationsDAO.add(driverCarRecommendations);
    }

    public List<DriverCarRecommendations> getAll() {
        return driverCarRecommendationsDAO.getAll();
    }

    public List<DriverCarRecommendations> getReport(Report report){
        return driverCarRecommendationsDAO.getReport(report);
    }

    public List<DriverCarRecommendations> getReportById(Report report){
       return driverCarRecommendationsDAO.getReportById(report);
    }

    public DriverCarRecommendations getOneByDriverId(int ID){
        return driverCarRecommendationsDAO.getOneByDriverId(ID);
    }

    public boolean update(DriverCarRecommendations driverCarRecommendations) {
        return driverCarRecommendationsDAO.update(driverCarRecommendations);
    }
}
