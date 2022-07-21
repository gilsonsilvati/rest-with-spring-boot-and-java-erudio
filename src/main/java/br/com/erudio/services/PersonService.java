package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String MESSAGE = "No records found for this ID!";

    @Autowired
    protected PersonRepository repository;

    @Autowired
    protected PersonMapper mapper;

    public List<PersonVO> findAll() {

        logger.info("Finding all people!");

        List<PersonVO> persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
        persons.forEach(personVO -> addLink(personVO.getKey(), personVO));

        return persons;
    }

    public PersonVO findById(Long id) {

        logger.info("Finding one person!");

        var personVO = DozerMapper.parseObject(getEntity(id), PersonVO.class);
        addLink(id, personVO);

        return personVO;
    }

    public PersonVO create(PersonVO person) {

        if (Objects.isNull(person)) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);

        var personVO = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        addLink(personVO.getKey(), personVO);

        return personVO;
    }

    public PersonVO update(PersonVO person) {

        if (Objects.isNull(person)) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating one person!");

        var entity = getEntity(person.getKey());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var personVO = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        addLink(person.getKey(), personVO);

        return personVO;
    }

    public void delete(Long id) {

        logger.info("Deleting one person!");

        repository.delete(getEntity(id));
    }

    private Person getEntity(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }

    private void addLink(Long id, PersonVO personVO) {

        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
    }
}
