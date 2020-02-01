package com.aayushman.garbager.model;

public class Transaction {

    boolean received;
    String name;
    double amount;

    public Transaction(String name, double amount, boolean received) {
        this.name = name;
        this.amount = amount;
        this.received = received;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }


}
