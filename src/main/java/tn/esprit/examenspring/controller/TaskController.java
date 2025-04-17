package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Task;
import tn.esprit.examenspring.services.ITaskService;
import tn.esprit.examenspring.services.ITaskService;

import java.util.List;
@RestController
@RequestMapping("/Task")

public class TaskController {
    @Autowired
    private ITaskService taskService;

    @GetMapping("/retrieve-all-task")
    public List<Task> getTask() {
        return taskService.getTasks();
    }

    @PostMapping("/add-task")
    public Task addTask(@RequestBody Task e) {
        return taskService.addTask(e);
    }
    @DeleteMapping("/remove-task/{task-id}")
    public void removeTask(@PathVariable("task-id") Integer eid) {
        taskService.deleteTask(eid);
    }
    @PutMapping("/modify-task")
    public Task modifyTask(@RequestBody Task e) {
        return taskService.modifyTask(e);
    }


}
