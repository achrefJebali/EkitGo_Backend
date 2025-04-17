package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.TaskRepository;
import tn.esprit.examenspring.entities.Task;


import java.util.List;
@Service
@Slf4j

public class TaskServiceImpl implements  ITaskService {


        @Autowired
        private TaskRepository taskRepository; // Injection correcte du repository

        @Override
        public Task addTask(Task task) {
            return taskRepository.save(task);
        }

        @Override
        public List<Task> getTasks() {
            return taskRepository.findAll();
        }

        @Override
        public Task modifyTask(Task task) {
            return taskRepository.save(task);
        }

        @Override
        public void deleteTask(Integer id) {
            taskRepository.deleteById(id);
        }
}
