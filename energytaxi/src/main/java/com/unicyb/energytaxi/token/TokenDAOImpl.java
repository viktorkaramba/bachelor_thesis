package com.unicyb.energytaxi.token;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TokenDAOImpl implements DAO<Token> {

    @Override
    public boolean add(Token token) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_TOKEN);
            preparedStatement.setString(1, token.getToken());
            preparedStatement.setString(2, token.getTokenType().name());
            preparedStatement.setString(3, String.valueOf(token.isRevoked()));
            preparedStatement.setString(4, String.valueOf(token.isExpired()));
            preparedStatement.setInt(5, token.getUserId());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Token> getAll() {
        List<Token> tokenList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_TOKENS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Token token = getToken(resultSet);
                tokenList.add(token);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tokenList;
    }



    private Token getToken(ResultSet resultSet) throws SQLException {
        return new Token(resultSet.getInt(1), resultSet.getString(2),
                TokenType.valueOf(resultSet.getString(3)), Boolean.valueOf(resultSet.getString(4)),
                Boolean.valueOf(resultSet.getString(5)), resultSet.getInt(6));
    }
    @Override
    public Token getOne(int ID) {
        return null;
    }

    public Token getByToken(String token){
        Token resultToken = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_BY_TOKEN);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                resultToken = getToken(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultToken;
    }

    @Override
    public boolean update(Token token) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_TOKENS);
            statement.setString(1, token.getToken());
            statement.setString(2, token.getTokenType().name());
            statement.setString(3, String.valueOf(token.isRevoked()));
            statement.setString(4, String.valueOf(token.isExpired()));
            statement.setInt(5, token.getUserId());
            statement.setInt(7, token.getTokeId());
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean revokeAllUserTokens(int ID) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.UPDATE_ALL_TOKENS_BY_USER_ID);
            preparedStatement.setString(1, "true");
            preparedStatement.setString(2, "true");
            preparedStatement.setInt(3, ID);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean revokeOneToken(String token) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.UPDATE_ONE_TOKEN);
            preparedStatement.setString(1, "true");
            preparedStatement.setString(2, "true");
            preparedStatement.setString(3, token);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int ID) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_TOKEN);
            statement.setInt(1, ID);
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByToken(String jwt) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_TOKEN_BY_TOKEN);
            statement.setString(1, jwt);
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }
}
