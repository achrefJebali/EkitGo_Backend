package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.User;

import java.util.List;

@Repository
public interface InterviewsRepository extends JpaRepository<Interview,Integer> {
    @Query("SELECT i FROM Interview i WHERE i.student.id = :studentId")
    List<Interview> findByStudentId(Integer studentId);

    @Query("SELECT i FROM Interview i WHERE i.teacher.id = :teacherId")
    List<Interview> findByTeacherId(Integer teacherId);
}
