package br.com.erudio.services;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

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

        return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!");

        return DozerMapper.parseObject(getEntity(id), PersonVO.class);
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);

        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating one person with V2!");

        var entity = mapper.convertVOToEntity(person);

        return mapper.convertEntityToVO(repository.save(entity));
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person!");

        var entity = getEntity(person.getId());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        repository.delete(getEntity(id));
    }

    private Person getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }
}
