package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Event;
import tn.esprit.examenspring.entities.Task;

public interface TaskRepository extends JpaRepository<Task,Integer> {

}
