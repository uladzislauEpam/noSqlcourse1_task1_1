package epam.uladzislau.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String name;
    private String desc;
    private String dateOfCreation;
    private String deadline;
    @DBRef
    private List<Subtask> subtasks;
    private String category;
}
