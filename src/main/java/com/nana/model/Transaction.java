package com.nana.model;

import com.nana.dao.BankAccountDao;
import com.nana.dao.BankAccountDaoImp;
import com.nana.dao.TransactionDao;
import com.nana.dao.TransactionDaoImp;


public class Transaction implements Runnable {
    private  int  id;
    private BankAccount from;
    private BankAccount to;
    private double amount;
    private BankAccountDao accountDao = new BankAccountDaoImp();
    private TransactionDao transactionDao = new TransactionDaoImp();

    public Transaction(int id, BankAccount from, BankAccount to, double amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BankAccount getFrom() {
        return from;
    }

    public void setFrom(BankAccount from) {
        this.from = from;
    }

    public BankAccount getTo() {
        return to;
    }

    public void setTo(BankAccount to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0 ; i<20 ; i++) {
            if (amount < 0 && from.getId() == to.getId()) {
                withdraw();
            } else if (amount > 0 && from.getId() == to.getId()) {
                deposit();
            } else if (amount > 0 && from.getId() != to.getId()) {
                transact();
            }
        }
        }

    private synchronized void withdraw() {
            if (from.getBalance() >= Math.abs(amount)) {
                from.setBalance(from.getBalance() - Math.abs(amount));
                accountDao.save(from);
                transactionDao.save(this);
                System.out.println("Withdrawal of " + amount + " from " + from.getName() + " successful");
            } else {
                System.out.println("Insufficient funds in " + from.getName() + " to withdraw " + amount);
            }
    }
    private synchronized void deposit() {
            from.setBalance(from.getBalance() + amount);
            accountDao.save(from);
            transactionDao.save(this);
            System.out.println("Deposit of " + amount + " to " + from.getName() + " successful");
        }
    private synchronized void transact() {
            if (from.getBalance() >= amount) {
                from.setBalance(from.getBalance() - amount);
                to.setBalance(to.getBalance() + amount);
                    accountDao.save(from);
                    accountDao.save(to);
                    transactionDao.save(this);
                System.out.println("Transaction of " + amount + " from " + from.getName() + " to " + to.getName() + " successful");
            } else {
                System.out.println("Insufficient funds in " + from.getName() + " to transfer " + amount);
            }
        }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                " Transacted " +
                " From= " + from.getName() +
                " To= " + to.getName() +
                '}';
    }
}
