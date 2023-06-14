package com.nana;

import com.nana.dao.BankAccountDao;
import com.nana.dao.BankAccountDaoImp;
import com.nana.dao.TransactionDao;
import com.nana.dao.TransactionDaoImp;
import com.nana.model.BankAccount;
import com.nana.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankGUI extends JFrame {
    private final BankAccountDao accountDao;
    private final TransactionDao TRANSACTION_DAO = new TransactionDaoImp();
    private BankAccount account;
    private Transaction transaction;
    private boolean isAdminMode;

    public BankGUI() {
        accountDao = new BankAccountDaoImp();
        isAdminMode = false;

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

        addButton(buttonPanel, "Admin", this::adminMode, constraints, 0, 0);
        addButton(buttonPanel, "User", this::userMode, constraints, 0, 1);

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

    private void adminMode(ActionEvent e) {
        isAdminMode = true;
        showAdminMenu();
    }

    private void userMode(ActionEvent e) {
        isAdminMode = false;
        showUserMenu();
    }

    private void showAdminMenu() {
        while (isAdminMode) {
            String[] adminOptions = {"Create Account", "Delete Account", "Display Account", "Display All Accounts", "Display Transactions", "Display Transaction by ID", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Select an option:", "Admin Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, adminOptions, adminOptions[0]);

            switch (choice) {
                case 0:
                    createAccount();
                    break;
                case 1:
                    deleteAccount();
                    break;
                case 2:
                    displayAccount();
                    break;
                case 3:
                    displayAllAccounts();
                    break;
                case 4:
                    displayTransactions();
                    break;
                case 5:
                    displayTransactionById();
                    break;
                case 6:
                    isAdminMode = false;
                    break;
            }
        }
    }

    private void showUserMenu() {
        while (!isAdminMode) {
            String[] userOptions = {"Withdraw", "Deposit", "Transact", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Select an option:", "User Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

            switch (choice) {
                case 0:
                    withdraw();
                    break;
                case 1:
                    deposit();
                    break;
                case 2:
                    transact();
                    break;
                case 3:
                    isAdminMode = true;
                    break;
            }
        }
    }



    private void createAccount() {
        String accountName = JOptionPane.showInputDialog("Enter account name:");
        double initialBalance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial balance:"));

        account = new BankAccount(0, accountName, initialBalance);
        accountDao.save(account);

        JOptionPane.showMessageDialog(null, "Account created successfully!");
    }

    private void deleteAccount() {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        accountDao.deleteById(accountId);

        JOptionPane.showMessageDialog(null, "Account deleted successfully!");
    }

    private void displayAccount() {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        account = accountDao.findById(accountId);

        JOptionPane.showMessageDialog(null, "Account Details:\n" + account);
    }

    private void displayAllAccounts() {
        StringBuilder sb = new StringBuilder("All Accounts:\n");
        for (BankAccount acc : accountDao.findAll()) {
            sb.append(acc).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void displayTransactions() {
        StringBuilder sb = new StringBuilder("All Transactions:\n");
        for (Transaction tra : TRANSACTION_DAO.findAll()) {
            sb.append(tra).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void displayTransactionById() {
        int transactionId = Integer.parseInt(JOptionPane.showInputDialog("Enter transaction ID:"));
        transaction = TRANSACTION_DAO.findById(transactionId);

        JOptionPane.showMessageDialog(null, "Account Details:\n" + transaction);
    }

    private void withdraw() {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter withdrawal amount:"));

        transaction = new Transaction(0, accountId, -withdrawAmount);
        transaction.run();
    }

    private void deposit() {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter account ID:"));
        double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter deposit amount:"));

        transaction = new Transaction(0, accountId, depositAmount);
        transaction.run();

        JOptionPane.showMessageDialog(null, "Deposit successful!");
    }

    private void transact() {
        int fromId = Integer.parseInt(JOptionPane.showInputDialog("Enter your account ID:"));
        int toId = Integer.parseInt(JOptionPane.showInputDialog("Enter receiver's account ID:"));
        double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter sending amount:"));

        transaction = new Transaction(0, fromId, toId, amount);
        transaction.run();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankGUI::new);
    }
}
