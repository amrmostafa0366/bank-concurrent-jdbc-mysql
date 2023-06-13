package com.nana;

import com.nana.dao.BankAccountDao;
import com.nana.dao.BankAccountDaoImp;
import com.nana.model.BankAccount;
import com.nana.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BankGUI extends JFrame {
    private final BankAccountDao accountDao;
    private BankAccount account;
    private Transaction transaction;

    public BankGUI() {
        accountDao = new BankAccountDaoImp();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bank System");

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Welcome to the Bank System");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(label);
        headerPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        addButton(buttonPanel, "Create Account", this::createAccount, constraints, 0, 0);
        addButton(buttonPanel, "View Account", this::viewAccount, constraints, 0, 1);
        addButton(buttonPanel, "Deposit", this::deposit, constraints, 1, 0);
        addButton(buttonPanel, "Withdraw", this::withdraw, constraints, 1, 1);
        addButton(buttonPanel, "Transact", this::transact, constraints, 2, 0);
        addButton(buttonPanel, "Concurrency", this::concurrency, constraints, 2, 1);
        addButton(buttonPanel, "Set All Same Amount", this::setAllSameAmount, constraints, 3, 0);
        addButton(buttonPanel, "Exit", e -> System.exit(0), constraints, 3, 1);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addButton(JPanel panel, String buttonText, ActionListener listener, GridBagConstraints constraints, int gridX, int gridY) {
        JButton button = new JButton(buttonText);
        button.addActionListener(listener);
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        panel.add(button, constraints);
    }

    private void createAccount(ActionEvent e) {
        String accountName = JOptionPane.showInputDialog("Enter account name:");
        double initialBalance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial balance:"));

        account = new BankAccount(0, accountName, initialBalance);
        accountDao.save(account);

        JOptionPane.showMessageDialog(null, "Account created successfully!");
    }

    private void viewAccount(ActionEvent e) {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        account = accountDao.findById(accountId);

        JOptionPane.showMessageDialog(null, "Account Details:\n" + account);
    }

    private void deposit(ActionEvent e) {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter deposit amount:"));

        transaction = new Transaction(0, accountId, depositAmount);
        transaction.run();

        JOptionPane.showMessageDialog(null, "Deposit successful!");
    }

    private void withdraw(ActionEvent e) {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter withdrawal amount:"));

        transaction = new Transaction(0, accountId, -withdrawAmount);
        transaction.run();
    }

    private void transact(ActionEvent e) {
        int fromId = Integer.parseInt(JOptionPane.showInputDialog("Enter your account ID:"));
        int toId = Integer.parseInt(JOptionPane.showInputDialog("Enter receiver's account ID:"));
        double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter sending amount:"));

        transaction = new Transaction(0, fromId, toId, amount);
        transaction.run();
    }

    private void concurrency(ActionEvent e) {
        System.out.println("Concurrency...");
        Thread t1 = new Thread(new Transaction(0, 1, 2, 1d)); // initial balance of all of them is 100
        Thread t2 = new Thread(new Transaction(0, 2, 3, 1d));
        Thread t3 = new Thread(new Transaction(0, 3, 5, 1d));
        Thread t4 = new Thread(new Transaction(0, 5, 8, 1d));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "Account 1:\n" + accountDao.findById(1) + "\nAccount 2:\n" + accountDao.findById(2) + "\nAccount 3:\n" + accountDao.findById(3) + "\nAccount 5:\n" + accountDao.findById(5) + "\nAccount 8:\n" + accountDao.findById(8));
    }

    private void setAllSameAmount(ActionEvent e) {
        List<BankAccount> accounts = accountDao.findAll();
        double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter the amount:"));

        for (BankAccount acc : accounts) {
            acc.setBalance(amount);
            accountDao.save(acc);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankGUI::new);
    }
}
