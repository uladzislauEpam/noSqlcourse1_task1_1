package epam.uladzislau;

import java.util.Scanner;
import epam.uladzislau.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TaskManagerConsole implements CommandLineRunner {

    @Autowired
    TaskService taskService;
    Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String[] args) throws Exception {
        int input = -1;

        while(input != 0) {
            displayBanner();

            input = scanner.nextInt();
            processInput(input);
        }

        scanner.close();
    }

    private void processInput(int input) {
        switch (input) {
            case (1) -> taskService.displayTasks();
            case (2) -> taskService.displayOverdueTasks();
            case (3) -> taskService.displayTasksOfCategory(scanner);
            case (4) -> taskService.displaySubtasksOfCategory(scanner);
            case (5) -> taskService.createTask(scanner);
            case (6) -> {
                System.out.println("Creating subtask without parent task!");
                taskService.createSubtask(scanner);
            }
            case (7) -> taskService.displayTasksWithWordInDescr(scanner);
            case (8) -> taskService.displaySubtasksWithWordInName(scanner);
        }
    }

    private static void displayBanner() {
        System.out.println("""
            0 - Exit
            1 - Display all tasks
            2 - Display overdue tasks
            3 - Display tasks of category
            4 - Display subtasks of category
            5 - Create task
            6 - Create subtask
            7 - Search by word in the task description
            8 - Search by a sub-task name""");
    }
}
