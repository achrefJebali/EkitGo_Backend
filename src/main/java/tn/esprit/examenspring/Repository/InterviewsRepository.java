package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.User;

import java.util.List;

@Repository
public interface InterviewsRepository extends JpaRepository<Interview,Integer> {
//    @Query("SELECT i FROM Interview i JOIN i.users u WHERE u.role = 'STUDENT'")
//    List<Interview> findStudentInterviews();
//    @Query("SELECT i FROM Interview i JOIN i.users u WHERE u.role = 'TEACHER'")
//    List<Interview> findTeacherInterviews();

}
