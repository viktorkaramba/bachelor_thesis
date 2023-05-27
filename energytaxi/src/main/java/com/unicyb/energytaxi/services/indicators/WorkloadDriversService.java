package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.WorkloadDriversDAOImpl;
import com.unicyb.energytaxi.entities.indicators.WorkloadDrivers;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkloadDriversService {
    private final WorkloadDriversDAOImpl workloadDriversDAO;
    @Autowired
    public WorkloadDriversService(WorkloadDriversDAOImpl workloadDriversDAO) {
        this.workloadDriversDAO = workloadDriversDAO;
    }
    
    public List<WorkloadDrivers> getAll() {
        return workloadDriversDAO.getAll();
    }

    public List<WorkloadDrivers> getReport(Report report){
        return workloadDriversDAO.getReport(report);
    }

    public List<WorkloadDrivers> getReportById(Report report){
        return workloadDriversDAO.getReportById(report);
    }
}
