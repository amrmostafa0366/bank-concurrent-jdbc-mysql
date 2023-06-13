package com.nana;

import com.nana.dao.BankAccountDao;
import com.nana.dao.BankAccountDaoImp;
import com.nana.model.BankAccount;
import com.nana.model.Transaction;

import java.util.List;
import java.util.Scanner;

public class Bank {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        BankAccountDao accountDao = new BankAccountDaoImp();
        BankAccount account;
        Transaction transaction;

        while (true) {
            System.out.println(
                    "Welcome to the Bank System\n"+
                    "1. Create Account      " + "2. View Account\n"+
                    "3. Deposit             " + "4. Withdraw\n"+
                    "5. Transact            " + "6. Concurrency\n"+
                    "7. Set All Same Amount " + "8. Exit"
                    );
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Creating Account...");
                    System.out.print("Enter account name: ");
                    String accountName = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    account = new BankAccount(0,accountName, initialBalance);
                    accountDao.save(account);
                    System.out.println("Account created successfully!");
                    break;
                case 2:
                    System.out.println("Viewing Account Details...");
                    System.out.print("Enter account ID: ");
                    int accountId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                     account = accountDao.findById(accountId);
                        System.out.println("Account Details:");
                        System.out.println(account);
                    break;
                case 3:
                    System.out.println("Depositing...");
                    System.out.print("Enter account ID: ");
                    int depositAccountId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    transaction = new Transaction(0,depositAccountId,depositAmount);
                    transaction.run();
                    System.out.println("Deposit successful!");
                    break;
                case 4:
                    System.out.println("Withdrawing...");
                    System.out.print("Enter account ID: ");
                    int withdrawAccountId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    transaction = new Transaction(0,withdrawAccountId,-withdrawAmount);
                    transaction.run();
                    break;
                case 5:
                    System.out.println("Transact...");
                    System.out.print("Enter your account ID: ");
                    int fromId = scanner.nextInt();
                    System.out.print("Enter receiver's account ID: ");
                    int toId = scanner.nextInt();
                    System.out.print("Enter sending amount : ");
                    double amount = scanner.nextDouble();
                    transaction = new Transaction(0,fromId,toId,amount);
                    transaction.run();
                    break;
                case 6:System.out.println("Concurrency...");
                    Thread t1 = new Thread(new Transaction(0, 1,2, 1d)); //initial balance of all of them is 100
                    Thread t2 = new Thread(new Transaction(0, 2,3, 1d));
                    Thread t3 = new Thread(new Transaction(0, 3,5, 1d));
                    Thread t4 = new Thread(new Transaction(0, 5,8, 1d));
                    t1.start();
                    t2.start();
                    t3.start();
                    t4.start();
                    t1.join();
                    t2.join();
                    t3.join();
                    t4.join();
                    System.out.println(
                            accountDao.findById(1) +
                                    "\n" + accountDao.findById(2) +
                                    "\n" + accountDao.findById(3) +
                                    "\n" + accountDao.findById(5) +
                                    "\n" + accountDao.findById(8));
                    break;

                case 7:System.out.println("Set All The Same Amount...");
                    List<BankAccount> accounts = accountDao.findAll();
                    System.out.print("Enter the amount : ");
                    double a = scanner.nextDouble();
                    for(BankAccount acc : accounts){
                        acc.setBalance(a);
                        accountDao.save(acc);
                    }
                    break;
                case 8:System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }
}
