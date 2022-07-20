package br.com.erudio.unit.mapper.mocks;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }

    public PersonVO mockVO() {
        return mockVO(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }

        return persons;
    }

    public List<PersonVO> mockVOList() {
        List<PersonVO> persons = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }

        return persons;
    }

    public Person mockEntity(Integer number) {
        var person = new Person();
        person.setAddress("Address Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);

        return person;
    }

    public PersonVO mockVO(Integer number) {
        var person = new PersonVO();
        person.setAddress("Address Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setKey(number.longValue());
        person.setLastName("Last Name Test" + number);

        return person;
    }
}
