package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.NumberOfKilometersDAOImpl;
import com.unicyb.energytaxi.entities.indicators.NumberOfKilometers;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NumberOfKilometersService {
    private final NumberOfKilometersDAOImpl numberOfKilometersDAO;
    @Autowired
    public NumberOfKilometersService(NumberOfKilometersDAOImpl numberOfKilometersDAO) {
        this.numberOfKilometersDAO = numberOfKilometersDAO;
    }

    public List<NumberOfKilometers> getAll() {
        return numberOfKilometersDAO.getAll();
    }

    public List<NumberOfKilometers> getReport(Report report){
        return numberOfKilometersDAO.getReport(report);
    }

    public List<NumberOfKilometers> getReportById(Report report){
      return numberOfKilometersDAO.getReportById(report);
    }

    public List<NumberOfKilometers> getAllByDriverId(int ID){
        return numberOfKilometersDAO.getAllByDriverId(ID);
    }
}
