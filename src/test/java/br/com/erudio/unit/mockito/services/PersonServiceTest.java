package br.com.erudio.unit.mockito.services;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.repositories.PersonRepository;
import br.com.erudio.services.PersonService;
import br.com.erudio.unit.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    PersonService service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();

        MockitoAnnotations.openMocks(input);
    }

    @Test
    void findById() {
        var entity = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);

        valid(result);
    }

    @Test
    void create() {
        var entity = input.mockEntity(1);
        var personVO = input.mockVO(1);

        when(repository.save(entity)).thenReturn(entity);

        var result = service.create(personVO);

        valid(result);
    }

    @Test
    void createWithNullPerson() {
        var exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        var expectedMessage = "It is not allowed to persist a null object!";
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        var entity = input.mockEntity(1);
        var personVO = input.mockVO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        var result = service.update(personVO);

        valid(result);
    }

    @Test
    void updateWithNullPerson() {
        var exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        var expectedMessage = "It is not allowed to persist a null object!";
        var actualMessage = exception.getMessage();

        assertTrue(expectedMessage.contains(actualMessage));
    }

    @Test
    void delete() {
        var entity = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

    @Test
    void findAll() {
        fail("TODO: Implement...");
    }

    private void valid(PersonVO result) {
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));

        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }
}
