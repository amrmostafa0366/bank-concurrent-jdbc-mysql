package com.nana.model;

import com.nana.dao.BankAccountDao;
import com.nana.dao.BankAccountDaoImp;
import com.nana.dao.TransactionDao;
import com.nana.dao.TransactionDaoImp;

public class Transaction implements Runnable {
    private int id;
    private int fromId;
    private int toId;
    private double amount;
    private final BankAccountDao ACCOUNT_DAO = new BankAccountDaoImp();
    private final TransactionDao TRANSACTION_DAO = new TransactionDaoImp();

    public Transaction(int id, int fromId, int toId, double amount) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public  void run() {
            if (amount < 0 && fromId == toId) {
                withdraw(fromId);
            } else if (amount > 0 && fromId == toId) {
                deposit(fromId);
            } else if (amount > 0 && fromId != toId) {
                transact();
            }
        }

    private synchronized void withdraw(int id) {
        BankAccount from = ACCOUNT_DAO.findById(id);
        if (from.getBalance() >= Math.abs(amount)) {
            from.setBalance(from.getBalance() - Math.abs(amount));
            ACCOUNT_DAO.save(from);
            System.out.println("Withdrawal of " + amount + " from " + from.getName() + " successful");
        } else {
            System.out.println("Insufficient funds in " + from.getName() + " to withdraw " + amount);
        }
    }

    private synchronized void deposit(int id) {
        BankAccount from = ACCOUNT_DAO.findById(id);
        from.setBalance(from.getBalance() + amount);
        ACCOUNT_DAO.save(from);
        System.out.println("Deposit of " + amount + " to " + from.getName() + " successful");
    }

    private synchronized void transact() {
        BankAccount from = ACCOUNT_DAO.findById(fromId);
        BankAccount to = ACCOUNT_DAO.findById(toId);
            if (from.getBalance() >= amount) {
                withdraw(fromId);
                deposit(toId);
                TRANSACTION_DAO.save(this);
                System.out.println("Transaction of " + amount + " from " + from.getName() + " to " + to.getName()
                        + " successful");
            } else {
                System.out.println("Insufficient funds in " + from.getName() + " to transfer " + amount);
            }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                " Transacted " +
                " From= " + fromId +
                " To= " + toId +
                '}';
    }
}
