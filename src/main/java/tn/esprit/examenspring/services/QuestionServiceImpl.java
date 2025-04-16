                                            package tn.esprit.examenspring.services;

                                            import jakarta.transaction.Transactional;
                                            import org.springframework.beans.factory.annotation.Autowired;
                                            import org.springframework.stereotype.Service;
                                            import org.springframework.web.multipart.MultipartFile;
                                            import tn.esprit.examenspring.Repository.QuestionRepository;
                                            import tn.esprit.examenspring.Repository.QuizRepository;
                                            import tn.esprit.examenspring.entities.Question;
                                            import tn.esprit.examenspring.entities.Quiz;

                                            import java.io.IOException;
                                            import java.nio.file.Files;
                                            import java.nio.file.Path;
                                            import java.nio.file.Paths;
                                            import java.nio.file.StandardCopyOption;
                                            import java.util.List;

                                            @Service
                                            public class QuestionServiceImpl implements IQuestionService {

                                                @Autowired
                                                private QuestionRepository questionRepository;

                                                @Autowired
                                                private QuizRepository quizRepository;

                                              /*  @Override
                                                public Question addQuestion(Question question) {
                                                    if (question.getQuiz() != null && question.getQuiz().getQuizId() != null) {
                                                        Quiz quiz = quizRepository.findById(question.getQuiz().getQuizId())
                                                                .orElseThrow(() -> new RuntimeException("Quiz non trouvé"));
                                                        question.setQuiz(quiz);
                                                    }
                                                    return questionRepository.save(question);
                                                }*/
                                              @Transactional
                                              public Question addQuestion(Question question, MultipartFile file) {
                                                  try {
                                                      // 1. Gestion de l’image (si présente)
                                                      if (file != null && !file.isEmpty()) {
                                                          String originalFileName = file.getOriginalFilename();

                                                          // Répertoire où enregistrer l’image
                                                          Path filePath = Paths.get("C:/Users/user/Desktop/project pi/project pi/project pi/project pi/elitgo/src/assets/question/" + originalFileName);
                                                          Files.createDirectories(filePath.getParent());  // Create parent directories if not exist
                                                          Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                                                          // Stocker le chemin d'accès relatif pour le frontend
                                                          question.setImageUrl("/assets/question/" + originalFileName); // Exposing to frontend as URL
                                                      }

                                                      // 2. Lier le quiz si quizId présent
                                                      if (question.getQuiz() != null && question.getQuiz().getQuizId() != null) {
                                                          Quiz quiz = quizRepository.findById(question.getQuiz().getQuizId())
                                                                  .orElseThrow(() -> new RuntimeException("Quiz non trouvé avec l'ID : " + question.getQuiz().getQuizId()));
                                                          question.setQuiz(quiz);
                                                      }

                                                      // 3. Sauvegarde de la question
                                                      return questionRepository.save(question);

                                                  } catch (IOException e) {
                                                      throw new RuntimeException("Erreur lors de l’enregistrement de l’image", e);
                                                  } catch (Exception e) {
                                                      throw new RuntimeException("Erreur lors de l’ajout de la question", e);
                                                  }
                                              }



                                                @Override
                                                public Question updateQuestion(Question question) {
                                                    return questionRepository.save(question);
                                                }

                                                @Override
                                                public void deleteQuestion(Long id) {
                                                    questionRepository.deleteById(id);
                                                }

                                                @Override
                                                public Question getQuestionById(Long id) {
                                                    return questionRepository.findByIdWithQuiz(id).orElse(null);
                                                }

                                                public List<Question> getAllQuestions() {
                                                    return questionRepository.findAllWithQuiz();
                                                }




                                                public Question assignQuestionToQuiz(Long questionId, Long quizId) {
                                                    Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question introuvable"));
                                                    Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz introuvable"));

                                                    question.setQuiz(quiz);
                                                    return questionRepository.save(question);
                                                }



                                            }
