package com.unicyb.minitaxi.services;

import com.unicyb.minitaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.minitaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.minitaxi.database.dao.indicators.OrderDAOImpl;
import com.unicyb.minitaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.minitaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.minitaxi.entities.documents.ROLE;
import com.unicyb.minitaxi.entities.documents.User;
import com.unicyb.minitaxi.entities.userinterfaceenteties.*;
import com.unicyb.minitaxi.ranksystem.RankSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class WSService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private DriverDAOImpl driverDAO;
    private OrderDAOImpl orderDAO;
    private DriverRecommendationService driverRecommendationService;
    private UserDAOImpl userDAO;
    private UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;
    private UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;
    private RankSystem rankSystem;

    @Autowired
    public WSService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notifyFrontend(final String message){
        MyMessage responseMyMessage = new MyMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", responseMyMessage);
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


    public MyMessage notifyDriverRegistryMessage(final DriverResume driverResume){
        MyMessage myMessage = null;
//        try {
//            FullName fullName = new FullName(driverResume.getDriverFirstName(), driverResume.getDriverSurName(),
//                    driverResume.getDriverPatronymic());
//            FullNameDAOImpl fullNameDAO = new FullNameDAOImpl();
//            fullNameDAO.add(fullName);
//            int id = fullNameDAO.getIdByFullName(fullName);
////            Driver driver = new Driver(id, new Timestamp(new Date().getTime()), id, driverResume.getDriverTelephoneNumber(),
////                    driverResume.getDriverExperience(), SalaryService.getSalary(driverResume.getDriverExperience()));
//            DriverDAOImpl driverDAO = new DriverDAOImpl();
////            driverDAO.add(driver);
//            User user = new User(driverResume.getDriverUserName(), driverResume.getDriverPassword(), ROLE.DRIVER, 1);
//            userDAO = new UserDAOImpl();
//            userDAO.addWithID(user, id);
//            myMessage = new MyMessage(String.valueOf(id));
//            simpMessagingTemplate.convertAndSendToUser(driverResume.getDriverUserName(), "/driver-registration", myMessage);
//        }
//        catch (Exception e){
//            myMessage = new MyMessage("Sorry, there are no car for your work experience");
//            simpMessagingTemplate.convertAndSendToUser(driverResume.getDriverUserName(), "/driver-registration", myMessage);
//        }
        return myMessage;
    }

    public MyMessage notifyRegistryMessage(final  User user){
        MyMessage myMessage = null;
        try {
            userDAO = new UserDAOImpl();
            userDAO.add(user);
            User newUser = userDAO.getOneByUserName(user.getUsername());
            myMessage =  new MyMessage(String.valueOf(newUser.getUserId()));
            simpMessagingTemplate.convertAndSendToUser(newUser.getUsername(), "/registration",
                    myMessage);

        }
        catch (Exception e){
            myMessage =  new MyMessage("User with this username already exist");
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/registration",
                    myMessage);
        }
        return myMessage;
    }

    public LoginResponseMessage notifyAuthorization(final User user){
        userDAO = new UserDAOImpl();
        User loginUser = userDAO.getOneByUserName(user.getUsername());
        LoginResponseMessage loginResponseMessage = null;
        if (loginUser != null) {
            if(user.getPassword().equals(loginUser.getPassword())) {
                if(user.getRole().equals(loginUser.getRole())){
                    rankSystem = new RankSystem(userDAO.getUserStats(loginUser.getUserId()), loginUser);
                    int newRank = rankSystem.getNewRank();
                    if (newRank != loginUser.getRankId()){
                        User newUser = new User(loginUser.getUserId(), loginUser.getUsername(), loginUser.getPassword(),
                                loginUser.getRole(), newRank);
                        userDAO.update(newUser);
                        loginUser = newUser;
                    }
                    loginResponseMessage = new LoginResponseMessage(
                            String.valueOf(loginUser.getUserId()), loginUser.getRole(), loginUser.getRankId());
                    simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/authorization", loginResponseMessage);
                }
                else {
                    if(user.getRole().equals(ROLE.DRIVER)){
                        loginResponseMessage = new LoginResponseMessage(
                                HtmlUtils.htmlEscape("User is not driver"), user.getRole(), user.getRankId());
                        simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/authorization", loginResponseMessage);
                    }
                    else {
                        loginResponseMessage = new LoginResponseMessage(
                                HtmlUtils.htmlEscape(String.valueOf(loginUser.getUserId())), user.getRole(), user.getRankId());
                        simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/authorization", loginResponseMessage);
                    }
                }
            }
            else {
                loginResponseMessage = new LoginResponseMessage(
                        HtmlUtils.htmlEscape(HtmlUtils.htmlEscape("Incorrect password")), user.getRole(), user.getRankId());
                simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/authorization", loginResponseMessage);
            }
        }
        else {
            loginResponseMessage = new LoginResponseMessage(
                    HtmlUtils.htmlEscape(HtmlUtils.htmlEscape("Need registry")), user.getRole(), user.getRankId());
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/authorization", loginResponseMessage);
        }
        return loginResponseMessage;
    }
}
