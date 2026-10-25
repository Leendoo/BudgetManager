package com.Leonardo.java26.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BudgetService {
    private static final Logger log = LoggerFactory.getLogger(BudgetService.class);
    private final Repository<Transaction> repository;

    public BudgetService(Repository<Transaction> repository) {
        this.repository = repository;
    }

    /**
     * dds a new transaction to the repository
     */
    public void addTransaction(Transaction transaction) {
        repository.add(transaction);
    }

    /**
     *Gets all transactions without sorting them.
     */
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    /**
     * Gets all transactions sorted by date.
     *@param ascending true for ascending order (oldest first), false for descending order (newest first).
     */
    public List<Transaction> getAllSortedByDate(boolean ascending) {
        return repository.findAll().stream()
                .sorted((t1, t2) -> ascending
                        ? t1.date().compareTo(t2.date())
                        : t2.date().compareTo(t1.date()))
                .toList();
    }

    public double getBalance() {
        double balance = repository.findAll().stream()
                .mapToDouble(Transaction::signedAmount)
                .sum();
        log.debug("Beräknat saldo: {}", balance);
        return balance;
    }

    public Map<String, Double> getSumPerCategory() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        TreeMap::new,                       // sorterat på kategori
                        Collectors.summingDouble(Transaction::signedAmount)));
    }

    /**
     * Filters transactions based on transaction type (INCOME or EXPENSE).
     */
    public List<Transaction> filterByType(TransactionType type) {
        return repository.findWhere(t -> t.type() == type);
    }

    /**
     * Filters transactions within a date range, including the from and to dates.
     */
    public List<Transaction> filterByDateRange(LocalDate from, LocalDate to) {
        return repository.findWhere(t ->
                !t.date().isBefore(from) && !t.date().isAfter(to)
        );
    }
}