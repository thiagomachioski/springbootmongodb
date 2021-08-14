package in.thiago.springbootmongodb.controller;

import in.thiago.springbootmongodb.exception.TodoCollectionException;
import in.thiago.springbootmongodb.model.TodoDTO;
import in.thiago.springbootmongodb.repository.TodoRepository;
import in.thiago.springbootmongodb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestController
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoService todoService;

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> get(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(todoService.get(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getAll() {
        var todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, todos.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/todos")
    public ResponseEntity<?> create(@RequestBody TodoDTO todoDTO) {
        try {
            todoService.createTodo(todoDTO);
            return new ResponseEntity<>(todoDTO, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody TodoDTO todoDTO) {
        try {
            todoService.update(id, todoDTO);
            return new ResponseEntity<>("Update Todo with id " + id, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            todoService.delete(id);
            return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
