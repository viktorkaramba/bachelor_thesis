package com.unicyb.energytaxi.services.other;

import com.unicyb.energytaxi.token.Token;
import com.unicyb.energytaxi.token.TokenDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    private final TokenDAOImpl tokenDAO;
    @Autowired
    public TokenService(TokenDAOImpl tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    public boolean add(Token token) {
        return tokenDAO.add(token);
    }

    public List<Token> getAll() {
        return tokenDAO.getAll();
    }

    public Token getByToken(String token){
        return tokenDAO.getByToken(token);
    }

    public boolean update(Token token) {
        return tokenDAO.update(token);
    }

    public boolean revokeAllUserTokens(int ID) {
        return tokenDAO.revokeAllUserTokens(ID);
    }

    public boolean revokeOneToken(String token) {
        return tokenDAO.revokeOneToken(token);
    }

    public boolean delete(int ID) {
        return tokenDAO.delete(ID);
    }

    public boolean deleteByToken(String jwt) {
      return tokenDAO.deleteByToken(jwt);
    }
}
