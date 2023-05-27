package com.unicyb.energytaxi.services.ranksystem;

import com.unicyb.energytaxi.database.dao.ranksystem.RankDAOImpl;
import com.unicyb.energytaxi.entities.ranksystem.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {

    private final RankDAOImpl rankDAO;
    @Autowired
    public RankService(RankDAOImpl rankDAO) {
        this.rankDAO = rankDAO;
    }

    public boolean add(Rank rank) {
        return rankDAO.add(rank);
    }

    public List<Rank> getAll() {
        return rankDAO.getAll();
    }

    public Rank getOne(int ID) {
        return rankDAO.getOne(ID);
    }

    public boolean update(Rank rank) {
        return rankDAO.update(rank);
    }

    public boolean delete(int ID) {
        return rankDAO.delete(ID);
    }
}
