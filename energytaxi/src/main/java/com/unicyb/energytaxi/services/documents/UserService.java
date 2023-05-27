package com.unicyb.energytaxi.services.documents;

import com.unicyb.energytaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.entities.usersinfo.UserProfileInfo;
import com.unicyb.energytaxi.entities.usersinfo.UserStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAOImpl userDAO;
    @Autowired
    public UserService(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public boolean add(User user) {
      return userDAO.add(user);
    }

    public boolean addWithID(User user, int ID){
      return userDAO.addWithID(user, ID);
    }

    public List<User> getAll() {
       return userDAO.getAll();
    }

    public User getOne(int ID) {
       return userDAO.getOne(ID);
    }

    public UserStats getUserStats(int ID){
      return userDAO.getUserStats(ID);
    }

    public User getOneByUserName(String userName) {
       return userDAO.getOneByUserName(userName);
    }

    public UserProfileInfo getUserProfileInfo(int ID) {
       return userDAO.getUserProfileInfo(ID);
    }

    public boolean update(User user) {
        return userDAO.update(user);
    }

    public boolean delete(int ID) {
        return userDAO.delete(ID);
    }

    public boolean deleteByUserName(String userName) {
      return userDAO.deleteByUserName(userName);
    }
}
