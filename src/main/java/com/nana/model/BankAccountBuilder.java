package com.nana.model;

public class BankAccountBuilder {
    private int id;
    private String name;
    private double balance;

    public BankAccountBuilder id(int id){
        this.id = id;
        return this;
    }
    public BankAccountBuilder name(String name){
        this.name = name;
        return this;
    }
    public BankAccountBuilder balance(Double balance){
        this.balance = balance;
        return this;
    }
    public BankAccount build(){
        return new BankAccount(id,name,balance);
    }
}
