package com.nana.dao;


import com.nana.model.BankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDaoImp implements BankAccountDao {
    @Override
    public List<BankAccount> findAll() {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return null;
        }
        List<BankAccount> bankAccounts = new ArrayList<>();

        String query = "SELECT * FROM accounts;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                BankAccount bankAccount = new BankAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("balance")
                );
                bankAccounts.add(bankAccount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bankAccounts;
    }

    @Override
    public BankAccount findById(int id) {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return null;
        }
        BankAccount bankAccount = null;
        String query = "SELECT * FROM accounts WHERE id=?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                bankAccount=  BankAccount.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .balance(resultSet.getDouble("balance"))
                        .build();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bankAccount;
    }

    @Override
    public void save(BankAccount bankAccount) {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return;
        }
        if(bankAccount.getId() > 0){//update
            String query = "UPDATE accounts SET name=? , balance=? WHERE id=?;";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1,bankAccount.getName());
                preparedStatement.setDouble(2,bankAccount.getBalance());
                preparedStatement.setInt(3,bankAccount.getId());
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else{//create
            String query = "INSERT INTO accounts (name,balance) VALUES(?,?);";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1,bankAccount.getName());
                preparedStatement.setDouble(2,bankAccount.getBalance());
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void deleteById(int id) {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return;
        }
        String query = "DELETE FROM accounts WHERE id = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
