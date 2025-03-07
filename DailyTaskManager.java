import java.util.InputMismatchException;
import java.util.Scanner;

// Stack for Undo Mechanism
class TaskStack {
    private String[] stack;
    private boolean[] completed;
    private int top;
    private int capacity;
    private int[] taskIndices;

    public TaskStack(int size) {
        this.capacity = size;
        this.stack = new String[size];
        this.completed = new boolean[size];
        this.taskIndices = new int[size];
        this.top = -1;
    }
    // Push a completed task in the stack
    public void push(String task, int index) {
        if (top == capacity - 1) {
            System.out.println("Stack Overflow! Cannot add more completed tasks.");
            return;
        }
        stack[++top] = task + " (Completed)"; // Mark task as completed
        taskIndices[top] = index;
        completed[top] = true;
        System.out.println("Task '" + task + "' marked as completed.");
    }
    // Remove the last completed task (Undo last stack completion)
    public int pop() {
        if (top == -1) {
                System.out.println("Stack Underflow! No tasks to undo.");
                return -1;
        }
        System.out.println("Undo: Task '" + stack[top] + "' is now pending again.");
        return taskIndices[top--];
    }
    // Showing the last completed task
    public String peek() {
        if (top == -1) {
            return "No completed tasks.";
        }
        return stack[top];
    }
}

// Node for Linked List
class TaskNode {
    String task;
    TaskNode next;

    public TaskNode(String task) {
        this.task = task;
        this.next = null;
    }
}

// Linked List for Dynamic Tasks
class TaskLinkedList {
    private TaskNode head;

    // Insert a new task into linked list
    public void insertTask(String task) {
        TaskNode newNode = new TaskNode(task);
        if (head == null) {
            head = newNode;
        } else {
            TaskNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        System.out.println("Task added: " + task);
    }
    // Delete a task from linked list base on the chosen position
    public void deleteTask(int position) {
        if (head == null) {
            System.out.println("No tasks to remove.");
            return;
        }

        if (position == 0) { // Remove head
            System.out.println("Task '" + head.task + "' removed.");
            head = head.next;
            return;
        }

        TaskNode current = head;
        TaskNode previous = null;
        int count = 0;

        while (current != null && count < position) {
            previous = current;
            current = current.next;
            count++;
        }

        if (current != null) {
            System.out.println("Task '" + current.task + "' removed.");
            previous.next = current.next;
        } else {
            System.out.println("Invalid position. No task removed.");
        }
    }
    // Displaying all task that are inside linked list
    public void displayTasks() {
        if (head == null) {
            System.out.println("No dynamic tasks available.");
            return;
        }

        System.out.println("\nDynamic Tasks:");
        TaskNode current = head;
        int index = 0;
        while (current != null) {
            System.out.println(index + ": " + current.task);
            current = current.next;
            index++;
        }
    }
}

// Main Application
public class DailyTaskManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Predefined List of Tasks
        String[] taskArray = {"Check Schedule", "Attend lecture", "Exercise", "Study", "Homework", "Improve Piano Skills", "Don't Lose Your Sanity",};
        boolean[] taskCompleted = new boolean[taskArray.length];

        // Stack to store completed tasks
        TaskStack completedTasks = new TaskStack(10);

        // To allow user to add/remove tasks
        TaskLinkedList taskList = new TaskLinkedList();

        while (true) {
            // Displaying menu options
            System.out.println("\n--- Daily Task Manager ---");
            System.out.println("1. View Tasks"); // Task Array
            System.out.println("2. Update Task"); // Task Array
            System.out.println("3. Mark Task as Completed"); // Push to Stack
            System.out.println("4. View Last Completed Task"); // Peek the Last Task Completed to Stack
            System.out.println("5. Undo Last Completion"); // Pop from Stack
            System.out.println("6. Add New Dynamic Task"); // Linked List
            System.out.println("7. Remove Dynamic Task"); // Linked List
            System.out.println("8. View Dynamic Tasks"); // Linked List
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }

            switch (choice) {
                case 1 -> {
                    // Display tasks from array
                    System.out.println("\nTasks in Array:");
                    for (int i = 0; i < taskArray.length; i++) {
                        String status = taskCompleted[i] ? " (Completed)" : "";
                        System.out.println(i + ": " + taskArray[i] + status);
                    }
                }

                case 2 -> {
                    // Update a specific task
                    System.out.print("Enter task index to update: ");
                    try {
                        int index = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (index >= 0 && index < taskArray.length) {
                            System.out.print("Enter new task: ");
                            taskArray[index] = scanner.nextLine();
                            System.out.println("Task updated successfully.");
                        } else {
                            System.out.println("Invalid index.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        scanner.nextLine();
                    }
                }

                case 3 -> {
                    System.out.print("Enter task index to complete: ");
                    try {
                        int completeIndex = scanner.nextInt();
                        scanner.nextLine();
                        if (completeIndex >= 0 && completeIndex < taskArray.length) {
                            completedTasks.push(taskArray[completeIndex], completeIndex);
                            taskCompleted[completeIndex] = true;
                            System.out.println("Task '" + taskArray[completeIndex] + "' completed.");
                        } else {
                            System.out.println("Invalid index.");
                        }
                    // if the input is not a integer than it will show this error message, asking for a new input
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        scanner.nextLine();
                    }
                }

                case 4 -> 
                // Peek at the last completed task
                    System.out.println("Last completed task: " + completedTasks.peek());

                case 5 -> {
                    int undoneIndex = completedTasks.pop();
                    if (undoneIndex != -1) {
                        taskCompleted[undoneIndex] = false;
                    }
                }

                case 6 -> {
                    // Add new task
                    System.out.print("Enter new task description: ");
                    String newTask = scanner.nextLine();
                    taskList.insertTask(newTask);
                }

                case 7 -> {
                    // Remove task by position that has been chosen
                    System.out.print("Enter the task position to remove (starting from 0): ");
                    int position = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    taskList.deleteTask(position);
                }

                case 8 -> // Display all tasks in linked list
                    taskList.displayTasks();

                case 9 -> {
                    // Exiting the program
                    System.out.println("Exiting Task Manager.");
                    scanner.close();
                    return;
                }

                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
