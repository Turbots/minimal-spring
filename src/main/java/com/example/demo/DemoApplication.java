package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
class TodoItem {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime completedAt;
}

@Repository
interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

}

@RestController
@RequestMapping("items")
class ItemsController {

    private final TodoItemRepository repository;

    ItemsController(TodoItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TodoItem> getItems() {
        return this.repository.findAll();
    }
}

@Component
class DataGenerator implements ApplicationRunner {

    private final TodoItemRepository repository;

    DataGenerator(TodoItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Arrays.asList("Clean the house", "Mow the lawn").forEach((todo) -> {
            OffsetDateTime time = OffsetDateTime.now();
            this.repository.save(new TodoItem(null, todo, time, null));
        });
    }
}