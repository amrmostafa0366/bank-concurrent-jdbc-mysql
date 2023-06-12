package com.nana;

import com.nana.dao.BankAccountDao;
import com.nana.dao.BankAccountDaoImp;
import com.nana.model.Transaction;

public class Bank {
    public static void main(String[] args) throws InterruptedException {


        BankAccountDao dao = new BankAccountDaoImp();

        Thread t1 = new Thread(new Transaction(0, 1,2, 1d));
        Thread t2 = new Thread(new Transaction(0, 2,3, 1d));

        t1.start();
        t2.start();
        t1.join();
        t2.join();


        System.out.println(dao.findById(1) + "\n" + dao.findById(2) + "\n" + dao.findById(3));



    }
}