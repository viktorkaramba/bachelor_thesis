package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.IncomeCarsDAOImpl;
import com.unicyb.energytaxi.entities.indicators.IncomeCars;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeCarsService {
    private final IncomeCarsDAOImpl incomeCarsDAO;
    @Autowired
    public IncomeCarsService(IncomeCarsDAOImpl incomeCarsDAO) {
        this.incomeCarsDAO = incomeCarsDAO;
    }

    public List<IncomeCars> getAll() {
        return incomeCarsDAO.getAll();
    }

    public List<IncomeCars> getReport(Report report){
        return incomeCarsDAO.getReport(report);
    }

    public List<IncomeCars> getReportById(Report report){
        return incomeCarsDAO.getReportById(report);
    }
}
