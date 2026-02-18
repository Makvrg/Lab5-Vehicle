package ru.ifmo.se.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.repository.CollectionRepository;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectionServiceTest {

    @Mock
    private CollectionRepository collectionRepository;
    @Mock
    private OutputStringFormatter formatter;
    @InjectMocks
    private CollectionService collectionService;

    @Test
    void clear_getExistsCity() {
        when(collectionRepository.deleteAll()).thenReturn(true);
        Assertions.assertTrue(collectionService.clear());
    }
}
