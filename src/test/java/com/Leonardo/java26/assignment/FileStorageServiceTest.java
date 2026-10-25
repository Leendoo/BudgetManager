package com.Leonardo.java26.assignment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAndLoadPreservesTransactions() throws IOException {
        // Arrange
        Path file = tempDir.resolve("test_transactions.csv");
        FileStorageService storage = new FileStorageService(file);
        Transaction t1 = new Transaction("Lön", LocalDate.of(2026, 10, 1), 25000, TransactionType.INCOME);
        Transaction t2 = new Transaction("Mat", LocalDate.of(2026, 10, 2), 500, TransactionType.EXPENSE);

        // Act
        storage.save(List.of(t1, t2));
        List<Transaction> loaded = storage.load();

        // Assert
        assertEquals(2, loaded.size());
        assertEquals(t1, loaded.get(0));
        assertEquals(t2, loaded.get(1));
    }

    @Test
    void loadNonExistingFileReturnsEmptyListWithoutCrashing() {
        // Arrange
        Path missing = tempDir.resolve("non_existent.csv");
        FileStorageService storage = new FileStorageService(missing);

        // Act
        List<Transaction> result = storage.load();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void parseLineThrowsFileFormatExceptionOnCorruptData() {
        // Arrange
        FileStorageService storage = new FileStorageService(tempDir.resolve("dummy.csv"));

        // Act & Assert
        assertThrows(FileFormatException.class, () -> storage.parseLine("Mat;2026-10-01;INTE_ETT_TAL;EXPENSE"));
        assertThrows(FileFormatException.class, () -> storage.parseLine("FörFåFält;100"));
    }
}