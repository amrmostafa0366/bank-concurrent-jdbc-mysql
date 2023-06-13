package com.nana.dao;


import com.nana.model.BankAccount;

import java.io.*;
import java.sql.*;
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
            while (resultSet.next()) {
                // Deserialize the object
                byte[] serializedData = resultSet.getBytes("serialized_object");
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData))) {
                    BankAccount bankAccount = (BankAccount) ois.readObject();
                    bankAccounts.add(bankAccount);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
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
                // Deserialize the object
                byte[] serializedData = resultSet.getBytes("serialized_object");
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedData))) {
                     bankAccount = (BankAccount) ois.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bankAccount;
    }
    @Override
    public void save(BankAccount bankAccount) {
        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            return;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(bankAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] serializedData = bos.toByteArray();
        String query;
        if (bankAccount.getId() > 0) { // update
            query = "UPDATE accounts SET serialized_object=? WHERE id=?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBytes(1, serializedData);
                preparedStatement.setInt(2, bankAccount.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else { // create
            query = "INSERT INTO accounts (serialized_object) VALUES(?);";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setBytes(1, serializedData);
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    bankAccount.setId(resultSet.getInt(1));
                    save(bankAccount);
                }
            } catch (SQLException e) {
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
