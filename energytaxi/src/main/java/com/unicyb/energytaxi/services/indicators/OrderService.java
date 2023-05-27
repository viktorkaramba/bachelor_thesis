package com.unicyb.energytaxi.services.indicators;

import com.unicyb.energytaxi.database.dao.indicators.OrderDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.entities.documents.CAR_CLASSES;
import com.unicyb.energytaxi.entities.documents.Driver;
import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.entities.indicators.Order;
import com.unicyb.energytaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import com.unicyb.energytaxi.entities.userinterfaceenteties.MyMessage;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import com.unicyb.energytaxi.entities.userinterfaceenteties.SendOrder;
import com.unicyb.energytaxi.entities.userinterfaceenteties.UserOrderInfo;
import com.unicyb.energytaxi.ranksystem.RankSystem;
import com.unicyb.energytaxi.services.documents.DriverService;
import com.unicyb.energytaxi.services.documents.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.unicyb.energytaxi.services.other.ExperienceService.getUpdateExperience;

@Service
public class OrderService {

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    private DriverCarRecommendationsService driverRecommendationService;
    private UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;
    private UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;
    private RankSystem rankSystem;

    private final OrderDAOImpl orderDAO;
    @Autowired
    public OrderService(OrderDAOImpl orderDAO) {
        this.orderDAO = orderDAO;
    }
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
        orderDAO.add(order);
        Driver driver = driverService.getOne(sendOrder.getDriverId());
        driver.setExperience(driver.getExperience() + getUpdateExperience(driver.getDate(), order.getDate()));
        driverRecommendationService.isApproach(driver, order.getDate());
        driverService.update(driver);
    }

    private void updateUserRankStats(SendOrder sendOrder){
        int carId = getCarId(sendOrder.getCarClass());
        System.out.println(carId);
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
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
        User user =  userService.getOne(sendOrder.getUserId());
        rankSystem = new RankSystem(userService.getUserStats(sendOrder.getUserId()), user);
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

    public boolean add(Order order) {
        return orderDAO.add(order);
    }

    public List<Order> getAll() {
        return orderDAO.getAll();
    }

    public List<Order> getReport(Report report){
        return orderDAO.getReport(report);
    }

    public List<Order> getReportById(Report report){
      return orderDAO.getReportById(report);
    }

    public List<UserOrderInfo> getUserOrderInfo(int ID){
        return orderDAO.getUserOrderInfo(ID);
    }
}
