package com.Leonardo.java26.assignment;


import java.time.LocalDate;

public record Transaction(String category, LocalDate date, double amount, TransactionType type) {

    public Transaction {
        if (date == null) {
            throw new InvalidTransactionException("Datum saknas");
        }
        if (category == null || category.isBlank()) {
            throw new InvalidTransactionException("Kategori får inte vara tom");
        }
        if (amount <= 0) {
            throw new InvalidTransactionException("Beloppet måste vara större än 0, var: " + amount);
        }
        if (type == null) {
            throw new InvalidTransactionException("Typ saknas");
        }
        category = category.trim();
    }

    // Positivt för inkomst, negativt för utgift
    public double signedAmount() {
        return type == TransactionType.INCOME ? amount : -amount;
    }

    public String toString() {
        return category + " " + amount + " " + type;
    }
}
