package com.unicyb.minitaxi.services;

import com.unicyb.minitaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.CarsDAOImpl;
import com.unicyb.minitaxi.database.dao.indicators.DriverCarRecommendationsDAOImpl;
import com.unicyb.minitaxi.database.dao.indicators.DriverRatingDAOImpl;
import com.unicyb.minitaxi.database.dao.indicators.NumberOfKilometersDAOImpl;
import com.unicyb.minitaxi.entities.documents.Car;
import com.unicyb.minitaxi.entities.documents.CarClass;
import com.unicyb.minitaxi.entities.documents.Driver;
import com.unicyb.minitaxi.entities.indicators.DriverCarRecommendations;
import com.unicyb.minitaxi.entities.indicators.DriverRating;
import com.unicyb.minitaxi.entities.indicators.NumberOfKilometers;
import com.unicyb.minitaxi.entities.indicators.STATUS;

import java.sql.Timestamp;
import java.util.List;

public class DriverRecommendationService {
    private CarClassDAOImpl carClassDAO = new CarClassDAOImpl();
    private CarsDAOImpl carsDAO = new CarsDAOImpl();
    private DriverRatingDAOImpl driverRatingDAO = new DriverRatingDAOImpl();
    private NumberOfKilometersDAOImpl numberOfKilometersDAO = new NumberOfKilometersDAOImpl();
    private DriverCarRecommendationsDAOImpl driverCarRecommendationsDAO = new DriverCarRecommendationsDAOImpl();

    public void isApproach(Driver driver, Timestamp date){
        Car car = carsDAO.getOne(driver.getCarId());
        List<CarClass> classClassList = carClassDAO.getAll();
        int index = getIndex(car, classClassList) + 1;
        float driverRating = getAverageDriverRating(driver.getDriverId());
        float driverKilometers = getDriverNumbersOfKilometers(driver.getDriverId());
        for(int i = index; i < classClassList.size(); i++){
            if(driver.getExperience()>=classClassList.get(i).getMinExperience()
                    && driverRating>=classClassList.get(i).getMinRating() &&
                    driverKilometers>= classClassList.get(i).getMinNumberOfKilometers()){
                List<Car> approachCars = carsDAO.getAllByCarClass(classClassList.get(i).getCcId());
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
        List<DriverRating> driverRatingList = driverRatingDAO.getAllByDriverId(ID);
        for (DriverRating driverRating: driverRatingList){
            result+=driverRating.getRating();
        }
        return result / driverRatingList.size();
    }
    private float getDriverNumbersOfKilometers(int ID){
        float result = 0;
        List<NumberOfKilometers> numberOfKilometersList = numberOfKilometersDAO.getAllByDriverId(ID);
        for (NumberOfKilometers numberOfKilometers: numberOfKilometersList){
            result+=numberOfKilometers.getNumbers();
        }
        return result;
    }
}
