package com.wudke.abwicklungsservice.persistence;

import com.wudke.abwicklungsservice.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RecipientRepositoryTest {

    @Autowired
    RecipientRepository recipientRepository;

    @BeforeEach
    void setUp() {
        recipientRepository.deleteAll();
    }

    @Test
    void thatRecipientRepo_persistWorks() {
        assertEquals(0, recipientRepository.count());

        RecipientEntity entity = new RecipientEntity("someName", new Address("Ladestr", "1", "22926", "Ahrensburg"));
        recipientRepository.save(entity);


        assertEquals(1, recipientRepository.count());
        assertEquals(entity, recipientRepository.findAll().getFirst());
    }
}