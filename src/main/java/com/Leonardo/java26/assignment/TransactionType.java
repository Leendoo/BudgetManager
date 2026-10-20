package com.Leonardo.java26.assignment;

public enum TransactionType {
    INCOME(1), EXPENSE(2);


    private final int type;
    TransactionType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
