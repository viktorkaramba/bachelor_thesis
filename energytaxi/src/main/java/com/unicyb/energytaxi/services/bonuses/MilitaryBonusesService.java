package com.unicyb.energytaxi.services.bonuses;

import com.unicyb.energytaxi.database.dao.bonuses.MilitaryBonusesDAOImpl;
import com.unicyb.energytaxi.entities.bonuses.MilitaryBonuses;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilitaryBonusesService {
    private final MilitaryBonusesDAOImpl militaryBonusesDAO;
    @Autowired
    public MilitaryBonusesService(MilitaryBonusesDAOImpl militaryBonusesDAO) {
        this.militaryBonusesDAO = militaryBonusesDAO;
    }

    public boolean add(MilitaryBonuses militaryBonuses) {
        return militaryBonusesDAO.add(militaryBonuses);
    }

    public List<MilitaryBonuses> getAll() {
        return militaryBonusesDAO.getAll();
    }

    public List<MilitaryBonuses> getAllByStatus(STATUS status) {
        return militaryBonusesDAO.getAllByStatus(status);
    }

    public MilitaryBonuses getOne(int ID) {
        return militaryBonusesDAO.getOneByUserIdWithoutPhoto(ID);
    }

    public MilitaryBonuses getOneByUserIdWithoutPhoto(int ID) {
        return militaryBonusesDAO.getOneByUserIdWithoutPhoto(ID);
    }

    public boolean update(MilitaryBonuses militaryBonuses) {
        return militaryBonusesDAO.update(militaryBonuses);
    }

    public boolean delete(int ID) {
        return militaryBonusesDAO.delete(ID);
    }
}
