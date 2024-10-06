package epam.uladzislau.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import epam.uladzislau.model.Subtask;
import epam.uladzislau.model.Task;
import epam.uladzislau.repository.SubtaskRepository;
import epam.uladzislau.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SubtaskRepository subtaskRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public void createTask(Scanner scanner){

        Task task = new Task();
        System.out.println("name:");
        task.setName(scanner.next());
        System.out.println("desc:");
        task.setDesc(scanner.next());
        System.out.println("due:");
        task.setDeadline(scanner.next());
        task.setDateOfCreation(LocalDate.now().toString());
        System.out.println("category:");
        task.setCategory(scanner.next());

        List<Subtask> subtasks = new ArrayList<>();
        int input = -1;
        while(input != 0) {
            System.out.println("""
                Add subtask?
                0 - no
                1 - yes""");
            input = scanner.nextInt();
            if(input == 1) {subtasks.add(createSubtask(scanner));}
        }

        task.setSubtasks(subtasks);

        taskRepository.save(task);
    }

    public Subtask createSubtask(Scanner scanner){

        Subtask subtask = new Subtask();
        System.out.println("name:");
        subtask.setName(scanner.next());
        System.out.println("desc:");
        subtask.setDesc(scanner.next());

        subtaskRepository.save(subtask);

        return subtask;
    }

    public void displayTasks(){
        System.out.println(
            taskRepository.findAll().stream()
                .map(Task::getName)
                .collect(Collectors.toList())
        );
    }

    public void displayOverdueTasks(){
        System.out.println(
            taskRepository.findAll().stream()
                .filter(task -> {
                    LocalDate date;
                    try {
                        date = LocalDate.parse(task.getDeadline());
                    } catch (Exception ex) {
                        date = LocalDate.parse("3000-01-01");
                    }
                        return date.isBefore(LocalDate.now());
                })
                .map(Task::getName)
                .collect(Collectors.toList())
        );
    }

    public void displayTasksOfCategory(Scanner scanner) {

        String category;
        System.out.println("category:");
        category =  scanner.next();

        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(category));
        List<Task> tasks = mongoTemplate.find(query, Task.class, "tasks");
        System.out.println(
            tasks.stream()
                .map(Task::getName)
                .collect(Collectors.toList())
        );

    }

    public void displaySubtasksOfCategory(Scanner scanner) {

        String category;
        System.out.println("category:");
        category =  scanner.next();

        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(category));
        List<Task> tasks = mongoTemplate.find(query, Task.class, "tasks");

        if (tasks.isEmpty()) { return; }
        System.out.println(
            tasks.stream()
                .flatMap(task -> task.getSubtasks() == null ? Stream.empty() : task.getSubtasks().stream())
                .toList()
                .stream().map(Subtask::getName)
                .collect(Collectors.toList())
        );

    }

    public void displayTasksWithWordInDescr(Scanner scanner) {
        String descShard;
        System.out.println("word in desc:");
        descShard = scanner.next();

        Query query = new Query();
        query.addCriteria(Criteria.where("desc").regex(".*" + descShard + ".*", "i"));
        List<Task> tasks = mongoTemplate.find(query, Task.class, "tasks");
        System.out.println(
            tasks.stream()
                .map(Task::getName)
                .collect(Collectors.toList())
        );
    }

    public void displaySubtasksWithWordInName(Scanner scanner) {
        String name;
        System.out.println("word in name:");
        name = scanner.next();

        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(".*" + name + ".*", "i"));
        List<Subtask> subtasks = mongoTemplate.find(query, Subtask.class, "subtasks");
        System.out.println(
            subtasks.stream()
                .map(Subtask::getName)
                .collect(Collectors.toList())
        );
    }

}
