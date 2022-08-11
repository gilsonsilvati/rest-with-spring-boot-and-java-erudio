package br.com.erudio.controllers;

import br.com.erudio.controllers.swagger.PersonSwagger;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.PersonService;
import br.com.erudio.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController implements PersonSwagger {

    @Autowired
    protected PersonService service;

    @Override
    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML } )
    public List<PersonVO> findAll() {
        return service.findAll();
    }

    @Override
    @GetMapping(value = "{id}",
            produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML } )
    @CrossOrigin(origins = "http://localhost:8082")
    public PersonVO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Override
    @PostMapping(
            consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML },
            produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML }
    )
    @CrossOrigin(origins = { "http://localhost:8082", "https://erudio.com.br" })
    public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
        var entity = service.create(person);

        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Override
    @PutMapping(
            consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML },
            produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML }
    )
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }

    @Override
    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
