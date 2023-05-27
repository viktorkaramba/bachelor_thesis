package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.DriverRatingDAOImpl;
import com.unicyb.energytaxi.entities.indicators.DriverRating;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverRatingService {

    private final DriverRatingDAOImpl driverRatingDAO;
    @Autowired
    public DriverRatingService(DriverRatingDAOImpl driverRatingDAO) {
        this.driverRatingDAO = driverRatingDAO;
    }

    public List<DriverRating> getAll() {
        return driverRatingDAO.getAll();
    }

    public List<DriverRating> getReport(Report report){
        return driverRatingDAO.getReport(report);
    }

    public List<DriverRating> getReportById(Report report){
        return driverRatingDAO.getReportById(report);
    }


    public List<DriverRating> getAllByDriverId(int ID){
        return driverRatingDAO.getAllByDriverId(ID);
    }
}
