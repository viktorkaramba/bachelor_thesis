package com.unicyb.minitaxi.services;

import com.unicyb.minitaxi.database.dao.documents.CarClassDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.FullNameDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.minitaxi.database.dao.userinterface.DriverInfoDAOImpl;
import com.unicyb.minitaxi.entities.documents.Driver;
import com.unicyb.minitaxi.entities.documents.FullName;
import com.unicyb.minitaxi.entities.documents.ROLE;
import com.unicyb.minitaxi.entities.documents.User;
import com.unicyb.minitaxi.entities.userinterfaceenteties.*;
import com.unicyb.minitaxi.entities.usersinfo.DriverInfo;
import com.unicyb.minitaxi.entities.usersinfo.DriverSendInfoMessage;
import com.unicyb.minitaxi.ranksystem.RankSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class WSService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private UserDAOImpl userDAO;
    private RankSystem rankSystem;
    private CarClassDAOImpl carClassDAO;
    private DriverInfoDAOImpl driverInfoDAO;

    @Autowired
    public WSService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notifyFrontend(final String message){
        Message responseMessage = new Message(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", responseMessage);
    }

    public void notifyDriver(final UserSendDate message){
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getDriverId()), "/driver", message);
    }

    public void notifyOrderAccept(final ResponseMessage responseMessage){
        simpMessagingTemplate.convertAndSendToUser(responseMessage.getUserId(), "/order-accept", responseMessage);
    }

    public void notifyOrderMessage(final UserSendDate userSendDate){
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(userSendDate.getUserId()), "/order-message", userSendDate);
    }


    public Message notifyDriverRegistryMessage(final DriverResume driverResume){
        Message message = null;
        try {
            FullName fullName = new FullName(driverResume.getDriverFirstName(), driverResume.getDriverSurName(),
                    driverResume.getDriverPatronymic());
            FullNameDAOImpl fullNameDAO = new FullNameDAOImpl();
            fullNameDAO.add(fullName);
            int id = fullNameDAO.getIdByFullName(fullName);
            Driver driver = new Driver(id, new Timestamp(new Date().getTime()), id, driverResume.getDriverTelephoneNumber(),
                    driverResume.getDriverExperience(), SalaryService.getSalary(driverResume.getDriverExperience()));
            DriverDAOImpl driverDAO = new DriverDAOImpl();
            driverDAO.add(driver);
            User user = new User(driverResume.getDriverUserName(), driverResume.getDriverPassword(), ROLE.DRIVER, 1);
            userDAO = new UserDAOImpl();
            userDAO.addWithID(user, id);
            message = new Message(String.valueOf(id));
            simpMessagingTemplate.convertAndSendToUser(driverResume.getDriverUserName(), "/driver-registration", message);
        }
        catch (Exception e){
            message = new Message("Sorry, there are no car for your work experience");
            simpMessagingTemplate.convertAndSendToUser(driverResume.getDriverUserName(), "/driver-registration",message);
        }
        return message;
    }

    public Message notifyRegistryMessage(final  User user){
        Message message = null;
        try {
            userDAO = new UserDAOImpl();
            userDAO.add(user);
            User newUser = userDAO.getOneByUserName(user.getUserName());
            message =  new Message(String.valueOf(newUser.getUserId()));
            simpMessagingTemplate.convertAndSendToUser(newUser.getUserName(), "/registration",
                   message);

        }
        catch (Exception e){
            message =  new Message("User with this username already exist");
            simpMessagingTemplate.convertAndSendToUser(user.getUserName(), "/registration",
                   message);
        }
        return message;
    }

    public LoginResponseMessage notifyAuthorization(final User user){
        userDAO = new UserDAOImpl();
        User loginUser = userDAO.getOneByUserName(user.getUserName());
        LoginResponseMessage loginResponseMessage = null;
        if (loginUser != null) {
            if(user.getPassword().equals(loginUser.getPassword())) {
                if(user.getRole().equals(loginUser.getRole())){
                    rankSystem = new RankSystem(userDAO.getUserStats(loginUser.getUserId()), loginUser);
                    int newRank = rankSystem.getNewRank();
                    if (newRank != loginUser.getRankId()){
                        User newUser = new User(loginUser.getUserId(), loginUser.getUserName(), loginUser.getPassword(),
                                loginUser.getRole(), newRank);
                        userDAO.update(newUser);
                        loginUser = newUser;
                    }
                    loginResponseMessage = new LoginResponseMessage(
                            String.valueOf(loginUser.getUserId()), loginUser.getRole(), loginUser.getRankId());
                    simpMessagingTemplate.convertAndSendToUser(user.getUserName(), "/authorization", loginResponseMessage);
                }
                else {
                    if(user.getRole().equals(ROLE.DRIVER)){
                        loginResponseMessage = new LoginResponseMessage(
                                HtmlUtils.htmlEscape("User is not driver"), user.getRole(), user.getRankId());
                        simpMessagingTemplate.convertAndSendToUser(user.getUserName(), "/authorization", loginResponseMessage);
                    }
                    else {
                        loginResponseMessage = new LoginResponseMessage(
                                HtmlUtils.htmlEscape(String.valueOf(loginUser.getUserId())), user.getRole(), user.getRankId());
                        simpMessagingTemplate.convertAndSendToUser(user.getUserName(), "/authorization", loginResponseMessage);
                    }
                }
            }
            else {
                loginResponseMessage = new LoginResponseMessage(
                        HtmlUtils.htmlEscape(HtmlUtils.htmlEscape("Incorrect password")), user.getRole(), user.getRankId());
                simpMessagingTemplate.convertAndSendToUser(user.getUserName(), "/authorization", loginResponseMessage);
            }
        }
        else {
            loginResponseMessage = new LoginResponseMessage(
                    HtmlUtils.htmlEscape(HtmlUtils.htmlEscape("Need registry")), user.getRole(), user.getRankId());
            simpMessagingTemplate.convertAndSendToUser(user.getUserName(), "/authorization", loginResponseMessage);
        }
        return loginResponseMessage;
    }

    public ResponseEntity notifyOrderComplete(SendOrder sendOrder){
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(sendOrder.getUserId()), "/order-complete",
                ResponseEntity.ok("Order successfully complete"));
        return ResponseEntity.ok("Order successfully complete");
    }

    public void notifyUserRequestDriversInfoMessage(String userId){
        System.out.println("user: " + userId);

        simpMessagingTemplate.convertAndSend("/users-request-drivers", userId);
    }

    synchronized public DriverInfo notifyDriverSendInfoMessage(DriverSendInfoMessage driverSendInfoMessage, int userCount){
        driverInfoDAO = new DriverInfoDAOImpl();
        DriverInfo driverInfo;
        if(userCount == 0){
            System.out.println("Disconnect");
            driverInfo = new DriverInfo();
            driverInfo.setDriverId(0);
            simpMessagingTemplate.convertAndSend("/topic/users-request-drivers", driverInfo);
        }
        else {
            System.out.println(driverSendInfoMessage.getLatitude() + " " + driverSendInfoMessage.getLongitude());
            driverInfo = driverInfoDAO.getOne(driverSendInfoMessage.getDriverId());
            driverInfo.setLatitude(driverSendInfoMessage.getLatitude());
            driverInfo.setLongitude(driverSendInfoMessage.getLongitude());
            System.out.println(driverInfo);
            simpMessagingTemplate.convertAndSend("/topic/users-request-drivers-info", driverInfo);
        }
        return driverInfo;
    }
}
