package com.unicyb.energytaxi.services.ranksystem;

import com.unicyb.energytaxi.database.dao.ranksystem.EliteRankDAOImpl;
import com.unicyb.energytaxi.entities.ranksystem.EliteRank;
import com.unicyb.energytaxi.entities.ranksystem.EliteRankUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EliteRankService {

    private final EliteRankDAOImpl eliteRankDAO;
    @Autowired
    public EliteRankService(EliteRankDAOImpl eliteRankDAO) {
        this.eliteRankDAO = eliteRankDAO;
    }

    public boolean add(EliteRank eliteRank) {
        return eliteRankDAO.add(eliteRank);
    }

    public List<EliteRank> getAll() {
        return eliteRankDAO.getAll();
    }

    public List<EliteRankUserInfo> getAllUserInfo() {
        return eliteRankDAO.getAllUserInfo();
    }

    public List<EliteRank> getAllByRankId(int ID){
        return eliteRankDAO.getAllByRankId(ID);
    }

    public boolean update(EliteRank eliteRank) {
        return eliteRankDAO.update(eliteRank);
    }

    public boolean delete(int ID) {
        return eliteRankDAO.delete(ID);
    }
}
