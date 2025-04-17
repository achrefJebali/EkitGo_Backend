package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Task;

import java.util.List;

public interface ITaskService {
    Task addTask(Task task);
    List<Task> getTasks();
    Task modifyTask(Task task);
    void deleteTask(Integer id);
}
