package com.unicyb.energytaxi.services;

import com.unicyb.energytaxi.database.dao.documents.DriverDAOImpl;
import com.unicyb.energytaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.energytaxi.database.dao.indicators.OrderDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.entities.documents.CAR_CLASSES;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.entities.indicators.Order;
import com.unicyb.energytaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.SendOrder;
import com.unicyb.energytaxi.ranksystem.RankSystem;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.unicyb.energytaxi.services.ExperienceService.getUpdateExperience;

public class OrderService {

    private OrderDAOImpl orderDAO;
    private DriverDAOImpl driverDAO;
    private UserDAOImpl userDAO;
    private DriverRecommendationService driverRecommendationService;
    private UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;
    private UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;
    private RankSystem rankSystem;

    public ResponseEntity completeOrder(SendOrder sendOrder) {
        try {
            updateDriverExperience(sendOrder);
            updateUserRankStats(sendOrder);
            return ResponseEntity.ok(new MyMessage("Order successfully complete"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MyMessage("Error"));
        }
    }

    private void updateDriverExperience(SendOrder sendOrder) {
        Order order = getNewOrder(sendOrder);
        orderDAO = new OrderDAOImpl();
        orderDAO.add(order);
        driverDAO = new DriverDAOImpl();
        Driver driver = driverDAO.getOne(sendOrder.getDriverId());
        driver.setExperience(driver.getExperience() + getUpdateExperience(driver.getDate(), order.getDate()));
        driverRecommendationService = new DriverRecommendationService();
        driverRecommendationService.isApproach(driver, order.getDate());
        driverDAO.update(driver);
    }

    private void updateUserRankStats(SendOrder sendOrder){
        int carId = getCarId(sendOrder.getCarClass());
        System.out.println(carId);
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
        userDAO = new UserDAOImpl();
        if(sendOrder.getIsUseSale()){
            userRankAchievementInfoDAO.updateNumbersOfUsesSales(sendOrder.getUserId());
        }
        if(sendOrder.getPrice() == 0){
            List<UserEliteRankAchievementInfo> userEliteRankAchievementInfos =
                    userEliteRankAchievementInfoDAO.getByUserIdAndRankId(sendOrder.getUserId(), sendOrder.getRankId());
            System.out.println(userEliteRankAchievementInfos);
            for (UserEliteRankAchievementInfo userEliteRankAchievementInfo: userEliteRankAchievementInfos){
                System.out.println(userEliteRankAchievementInfo.getCarClassId());
                if(userEliteRankAchievementInfo.getCarClassId() == carId){
                    userEliteRankAchievementInfo.setNumberOfUsesFreeOrder(
                            userEliteRankAchievementInfo.getNumberOfUsesFreeOrder() - 1);
                    userEliteRankAchievementInfoDAO.update(userEliteRankAchievementInfo);
                }
            }
        }
        User user =  userDAO.getOne(sendOrder.getUserId());
        rankSystem = new RankSystem(userDAO.getUserStats(sendOrder.getUserId()), user);
        rankSystem.updateUserRank();
    }

    private int getCarId(CAR_CLASSES carClass) {
        if(carClass.equals(CAR_CLASSES.STANDARD)){
            return 1;
        }
        else if(carClass.equals(CAR_CLASSES.COMFORT)){
            return 2;
        }
        else if(carClass.equals(CAR_CLASSES.ELITE)){
            return 3;
        }
        else {
            return 0;
        }
    }

    public Order getNewOrder(SendOrder sendOrder){
        return new Order(sendOrder.getDriverId(), sendOrder.getAddressCustomer(), sendOrder.getAddressDelivery(),
                sendOrder.getTelephoneNumber(), sendOrder.getPrice(), new Timestamp(new Date().getTime()), sendOrder.getRating(),
                sendOrder.getDistance(), sendOrder.getCustomerName(), sendOrder.getUserId(), sendOrder.getIsUseSale(),
                sendOrder.getCarClass(), sendOrder.getUserComment());
    }
}
