package com.nana.dao;

import com.nana.model.BankAccount;

import java.util.List;

public interface BankAccountDao {
    List<BankAccount> findAll();
    BankAccount findById(int id);
    void save(BankAccount bankAccount);
    void deleteById(int id);

}
