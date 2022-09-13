package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String MESSAGE = "No records found for this ID!";

    @Autowired
    protected BookRepository repository;

    public List<BookVO> findAll() {

        logger.info("Finding all book!");

        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books.forEach(bookVO -> addLink(bookVO.getKey(), bookVO));

        return books;
    }

    public BookVO findById(Long id) {

        logger.info("Finding one book!");

        var bookVO = DozerMapper.parseObject(getEntity(id), BookVO.class);
        addLink(id, bookVO);

        return bookVO;
    }

    public BookVO create(BookVO book) {

        if (Objects.isNull(book)) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(book, Book.class);

        var bookVO =  DozerMapper.parseObject(repository.save(entity), BookVO.class);
        addLink(bookVO.getKey(), bookVO);

        return bookVO;
    }

    public BookVO update(BookVO book) {

        if (Objects.isNull(book)) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating one book!");

        var entity = getEntity(book.getKey());

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var bookVO =  DozerMapper.parseObject(repository.save(entity), BookVO.class);
        addLink(book.getKey(), bookVO);

        return bookVO;
    }

    public void delete(Long id) {

        logger.info("Deleting one book!");

        repository.delete(getEntity(id));
    }

    private Book getEntity(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE));
    }

    private void addLink(Long id, BookVO bookVO) {

        bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
    }
}
