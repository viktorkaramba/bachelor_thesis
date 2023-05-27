package com.unicyb.energytaxi.services.userinterface;

import com.unicyb.energytaxi.database.dao.userinterface.OrderInfoDAOImpl;
import com.unicyb.energytaxi.entities.userinterfaceenteties.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderInfoService {
    private final OrderInfoDAOImpl orderInfoDAO;
    @Autowired
    public OrderInfoService(OrderInfoDAOImpl orderInfoDAO) {
        this.orderInfoDAO = orderInfoDAO;
    }

    public List<OrderInfo> getByDriverId(int ID){
        return orderInfoDAO.getByDriverId(ID);
    }

}
