package com.Leonardo.java26.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    private static final String CSV_DELIMITER = ";";

    private final Path filePath;

    public FileStorageService(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all transactions to the file.
     */
    public void save(List<Transaction> transactions) throws IOException {
        log.info("Sparar {} transaktioner till {}", transactions.size(), filePath);

        // Säkerställ att överordnade mappar finns
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Transaction t : transactions) {
                String line = String.join(CSV_DELIMITER,
                        t.category(),
                        t.date().toString(),
                        String.valueOf(t.amount()),
                        t.type().name());
                writer.write(line);
                writer.newLine();
            }
            log.debug("Sparning slutförd.");
        } catch (IOException e) {
            log.error("Kunde inte skriva till filen {}: {}", filePath, e.getMessage());
            throw e;
        }
    }

    /**
     * Loads transactions from the file. If the file is missing, an empty list is created without crashing.
     */
    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        if (!Files.exists(filePath)) {
            log.warn("Filen {} existerar inte. Startar med tom transaktionslista.", filePath);
            return transactions;
        }

        log.info("Läser in transaktioner från {}", filePath);
        int lineNumber = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    Transaction transaction = parseLine(line);
                    transactions.add(transaction);
                } catch (FileFormatException e) {
                    log.error("Fel på rad {} i filen: {}", lineNumber, e.getMessage());
                }
            }
            log.debug("Inläsning klar. {} giltiga transaktioner inlästa.", transactions.size());
        } catch (IOException e) {
            log.error("I/O-fel vid läsning av {}: {}", filePath, e.getMessage());
        }

        return transactions;
    }

    /**
     * Parses a single CSV row into a Transaction.
     */
    public Transaction parseLine(String line) throws FileFormatException {
        String[] parts = line.split(CSV_DELIMITER);
        if (parts.length != 4) {
            throw new FileFormatException("Förväntade 4 fält (kategori;datum;belopp;typ), fick: " + parts.length);
        }

        try {
            String category = parts[0].trim();
            LocalDate date = LocalDate.parse(parts[1].trim());
            double amount = Double.parseDouble(parts[2].trim());
            TransactionType type = TransactionType.valueOf(parts[3].trim().toUpperCase());

            return new Transaction(category, date, amount, type);
        } catch (DateTimeParseException e) {
            throw new FileFormatException("Ogiltigt datumformat (kräver YYYY-MM-DD): " + parts[1], e);
        } catch (NumberFormatException e) {
            throw new FileFormatException("Ogiltigt belopp: " + parts[2], e);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException("Ogiltig transaktionstyp (INCOME/EXPENSE): " + parts[3], e);
        }
    }
}
