package com.nana.dao;

import com.nana.model.BankAccount;
import com.nana.model.Transaction;

import java.util.List;

public interface TransactionDao {

    List<Transaction> findAll();
    Transaction findById(int id);
    void save(Transaction Transaction);
    void deleteById(int id);
}
