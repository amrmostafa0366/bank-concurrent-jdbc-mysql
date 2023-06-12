package com.nana.dao;

import com.nana.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TransactionDaoImp implements TransactionDao{
    @Override
    public List<Transaction> findAll() {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return null;
        }
        List<Transaction> transactions = new LinkedList<Transaction>();
        String query = "SELECT * FROM transactions;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Transaction transaction = new Transaction(
                  resultSet.getInt("id"),
                        resultSet.getInt("from_id"),
                        resultSet.getInt("to_id"),
                        resultSet.getDouble("amount"));
                transactions.add(transaction);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public Transaction findById(int id) {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return null;
        }
        Transaction transaction = null;
        String query = "SELECT * FROM transactions;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                 transaction = new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getInt("from_id"),
                        resultSet.getInt("to_id"),
                        resultSet.getDouble("amount")
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public void save(Transaction transaction) {
        Connection connection = DBConnection.getConnection();
        if(connection == null){
            return;
        }
        if(transaction.getId() > 0){//update
            String query = "UPDATE transactions SET from_id=?, to_id=? , amount=? WHERE id=?;";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1,transaction.getFromId());
                preparedStatement.setInt(2,transaction.getToId());
                preparedStatement.setDouble(3,transaction.getAmount());
                preparedStatement.setInt(4,transaction.getId());
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else{//create
            String query = "INSERT INTO transactions  (from_id, to_id , amount) VALUES (?,?,?);";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1,transaction.getFromId());
                preparedStatement.setInt(2,transaction.getToId());
                preparedStatement.setDouble(3,transaction.getAmount());
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
        String query = "DELETE FROM transactions WHERE id = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
