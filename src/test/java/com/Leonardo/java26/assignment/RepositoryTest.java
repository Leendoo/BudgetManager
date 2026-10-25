package com.Leonardo.java26.assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class RepositoryTest {

    private Repository<String> repository;

    @BeforeEach
    void setUp() {
        repository = new Repository<>();
    }

    @Test
    void newRepositoryIsEmpty() {
        // Arrange – empty repository (from setUp)

        // Act
        int size = repository.size();
        List<String> all = repository.findAll();

        // Assert
        Assertions.assertEquals(0, size);
        Assertions.assertTrue(all.isEmpty());
    }

    @Test
    void addIncreasesSize() {
        // Arrange
        String item = "Mat";

        // Act
        repository.add(item);

        // Assert
        Assertions.assertEquals(1, repository.size());
    }

    @Test
    void findAllReturnsAllAddedItemsInOrder() {
        // Arrange
        repository.add("Mat");
        repository.add("Hyra");
        repository.add("Lön");

        // Act
        List<String> all = repository.findAll();

        // Assert
        Assertions.assertEquals(List.of("Mat", "Hyra", "Lön"), all);
    }

    @Test
    void addNullThrowsIllegalArgumentException() {
        // Arrange
        String item = null;

        // Act + Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.add(item));
    }

    @Test
    void addNullDoesNotChangeSize() {
        // Arrange
        repository.add("Mat");

        // Act
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.add(null));

        // Assert
        Assertions.assertEquals(1, repository.size());
    }
}
