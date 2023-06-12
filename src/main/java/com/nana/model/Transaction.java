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
    private BankAccountDao accountDao = new BankAccountDaoImp();
    private TransactionDao transactionDao = new TransactionDaoImp();

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
    public void run() {
        for (int i = 0; i < 20; i++) {
            if (amount < 0 && fromId == toId) {
                withdraw();
            } else if (amount > 0 && fromId == toId) {
                deposit();
            } else if (amount > 0 && fromId != toId) {
                transact();
            }
        }
    }

    private synchronized void withdraw() {
        BankAccount from = accountDao.findById(fromId);
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
        BankAccount from = accountDao.findById(fromId);
        from.setBalance(from.getBalance() + amount);
        accountDao.save(from);
        transactionDao.save(this);
        System.out.println("Deposit of " + amount + " to " + from.getName() + " successful");
    }

    private synchronized void transact() {
        BankAccount from = accountDao.findById(fromId);
        BankAccount to = accountDao.findById(toId);
        synchronized (from) {
            synchronized (to) {
                if (from.getBalance() >= amount) {
                    from.setBalance(from.getBalance() - amount);
                    to.setBalance(to.getBalance() + amount);
                    accountDao.save(from);
                    accountDao.save(to);
                    transactionDao.save(this);
                    System.out.println("Transaction of " + amount + " from " + from.getName() + " to " + to.getName()
                            + " successful");
                } else {
                    System.out.println("Insufficient funds in " + from.getName() + " to transfer " + amount);
                }
            }
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
