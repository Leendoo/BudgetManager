package com.Leonardo.java26.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetServiceTest {

    private Repository<Transaction> repository;
    private BudgetService service;

    @BeforeEach
    void setUp() {
        repository = new Repository<>();
        service = new BudgetService(repository);
    }

    @Test
    void balanceIsZeroWhenNoTransactions() {
        // Arrange
        // (Empty repository)

        // Act
        double balance = service.getBalance();

        // Assert
        assertEquals(0.0, balance, 0.001);
    }

    @Test
    void balanceSubtractsExpensesFromIncome() {
        // Arrange
        repository.add(new Transaction("Lön", LocalDate.of(2026, 10, 1), 25000, TransactionType.INCOME));
        repository.add(new Transaction("Mat", LocalDate.of(2026, 10, 2), 3000, TransactionType.EXPENSE));

        // Act
        double balance = service.getBalance();

        // Assert
        assertEquals(22000.0, balance, 0.001);
    }

    @Test
    void sumPerCategoryGroupsAndCalculatesCorrectly() {
        // Arrange
        repository.add(new Transaction("Mat", LocalDate.of(2026, 10, 1), 500, TransactionType.EXPENSE));
        repository.add(new Transaction("Mat", LocalDate.of(2026, 10, 2), 300, TransactionType.EXPENSE));
        repository.add(new Transaction("Lön", LocalDate.of(2026, 10, 1), 20000, TransactionType.INCOME));

        // Act
        Map<String, Double> sumMap = service.getSumPerCategory();

        // Assert
        assertEquals(-800.0, sumMap.get("Mat"), 0.001);
        assertEquals(20000.0, sumMap.get("Lön"), 0.001);
    }

    @Test
    void filterByTypeReturnsOnlyMatchingType() {
        // Arrange
        repository.add(new Transaction("Lön", LocalDate.of(2026, 10, 1), 25000, TransactionType.INCOME));
        repository.add(new Transaction("Hyra", LocalDate.of(2026, 10, 1), 8000, TransactionType.EXPENSE));

        // Act
        List<Transaction> expenses = service.filterByType(TransactionType.EXPENSE);

        // Assert
        assertEquals(1, expenses.size());
        assertEquals("Hyra", expenses.get(0).category());
    }

    @Test
    void filterByDateRangeIncludesBoundaries() {
        // Arrange
        LocalDate d1 = LocalDate.of(2026, 10, 1);
        LocalDate d2 = LocalDate.of(2026, 10, 5);
        LocalDate d3 = LocalDate.of(2026, 10, 10);

        repository.add(new Transaction("T1", d1, 100, TransactionType.EXPENSE));
        repository.add(new Transaction("T2", d2, 200, TransactionType.EXPENSE));
        repository.add(new Transaction("T3", d3, 300, TransactionType.EXPENSE));

        // Act
        List<Transaction> result = service.filterByDateRange(d1, d2);

        // Assert
        assertEquals(2, result.size());
    }
}