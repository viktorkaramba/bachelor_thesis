package com.unicyb.minitaxi.controller.userinterface;


import com.unicyb.minitaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.minitaxi.entities.documents.ROLE;
import com.unicyb.minitaxi.entities.documents.User;
import com.unicyb.minitaxi.entities.userinterfaceenteties.LoginInfo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginInfoController {

    private UserDAOImpl userDAO;

    @PostMapping("/login")
    public LoginInfo save(@RequestBody LoginInfo loginInfo){
        System.out.println(loginInfo);
        userDAO = new UserDAOImpl();
        User user = userDAO.getOneByUserName(loginInfo.getUsername());
        if(user != null){
            if(user.getRole().equals(ROLE.SUPER_ADMIN) || user.getRole().equals(ROLE.ADMIN)) {
                if (user.getPassword().equals(loginInfo.getPassword())) {
                    loginInfo.setRole(user.getRole().name());
                    return loginInfo;
                } else {
                    return new LoginInfo("unknown", "error","unknown");
                }
            }
            else {
                return new LoginInfo("unknown", "error", "unknown");
            }
        }
        else {
            return new LoginInfo("unknown", "error", "unknown");
        }
    }

}
