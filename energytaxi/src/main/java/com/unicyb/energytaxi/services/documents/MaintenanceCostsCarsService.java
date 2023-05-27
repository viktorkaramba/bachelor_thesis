package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.indicators.MaintenanceCostsCarsDAOImpl;
import com.unicyb.energytaxi.entities.documents.MaintenanceCostsCars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceCostsCarsService {

    private final MaintenanceCostsCarsDAOImpl maintenanceCostsCarsDAO;
    @Autowired
    public MaintenanceCostsCarsService(MaintenanceCostsCarsDAOImpl maintenanceCostsCarsDAO) {
        this.maintenanceCostsCarsDAO = maintenanceCostsCarsDAO;
    }

    public boolean add(MaintenanceCostsCars maintenanceCostsCars) {
        return maintenanceCostsCarsDAO.add(maintenanceCostsCars);
    }

    public List<MaintenanceCostsCars> getAll() {
      return maintenanceCostsCarsDAO.getAll();
    }

    public boolean update(MaintenanceCostsCars maintenanceCostsCars) {
        return maintenanceCostsCarsDAO.update(maintenanceCostsCars);
    }

    public boolean delete(int ID) {
      return maintenanceCostsCarsDAO.delete(ID);
    }
}
