package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.documents.FullNameDAOImpl;
import com.unicyb.energytaxi.entities.documents.FullName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullNameService {
    private final FullNameDAOImpl fullNameDAO;
    @Autowired
    public FullNameService(FullNameDAOImpl fullNameDAO) {
        this.fullNameDAO = fullNameDAO;
    }

    public boolean add(FullName fullName) {
      return fullNameDAO.add(fullName);
    }

    public List<FullName> getAll() {
      return fullNameDAO.getAll();
    }

    public FullName getOne(int ID) {
        return fullNameDAO.getOne(ID);
    }

    public boolean update(FullName fullName) {
        return fullNameDAO.update(fullName);
    }

    public boolean delete(int ID) {
      return fullNameDAO.delete(ID);
    }
}
