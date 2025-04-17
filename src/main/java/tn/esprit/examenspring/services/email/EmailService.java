package tn.esprit.examenspring.services.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.Event;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.entities.ClubQuiz;
import tn.esprit.examenspring.entities.ClubQuizQuestion;
import tn.esprit.examenspring.services.EventServiceImpl;
import tn.esprit.examenspring.services.UserServicelmpl;
import tn.esprit.examenspring.services.pdf.QuizPdfService;

import jakarta.mail.util.ByteArrayDataSource;
import jakarta.mail.MessagingException;
import org.springframework.core.io.ByteArrayResource;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Autowired(required = false)
    private UserServicelmpl userService;
    
    @Autowired(required = false)
    private EventServiceImpl eventService;
    
    @Autowired(required = false)
    private tn.esprit.examenspring.Repository.ClubQuizRepository quizRepository;
    
    @Autowired(required = false)
    private tn.esprit.examenspring.Repository.ClubQuizQuestionRepository questionRepository;
    
    @Autowired(required = false)
    private QuizPdfService quizPdfService;
    
    /**
     * Envoie un email simple (version texte uniquement)
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            System.out.println("Email envoyé avec succès à " + to);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Envoie un email HTML
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        sendHtmlEmail(to, subject, htmlContent, "ElitGo Events");
    }
    
    /**
     * Envoie un email HTML avec un nom d'expéditeur personnalisé
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent, String senderName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(new InternetAddress(fromEmail, senderName));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indique que c'est du HTML
            
            mailSender.send(message);
            System.out.println("Email HTML envoyé avec succès à " + to + " de la part de " + senderName);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email HTML : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Envoie un email HTML avec une pièce jointe PDF
     */
    public void sendHtmlEmailWithPdfAttachment(String to, String subject, String htmlContent, String senderName, byte[] pdfContent, String attachmentFilename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(new InternetAddress(fromEmail, senderName));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indique que c'est du HTML
            
            // Ajouter la pièce jointe PDF
            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfContent, "application/pdf");
            helper.addAttachment(attachmentFilename, dataSource);
            
            mailSender.send(message);
            System.out.println("Email HTML avec pièce jointe PDF envoyé avec succès à " + to + " de la part de " + senderName);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email HTML avec pièce jointe PDF : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Envoie un email de confirmation d'inscription à un événement de manière asynchrone
     */
    public void sendEventRegistrationEmailAsync(Integer userId, Integer eventId) {
        try {
            // Récupérer les informations nécessaires AVANT la partie asynchrone
            User user = userService.getUserById(userId);
            Event event = eventService.getEventById(eventId);
            
            if (user == null || event == null || user.getEmail() == null) {
                System.err.println("Impossible d'envoyer l'email : utilisateur, événement ou email manquant");
                return;
            }
            
            // Préparer toutes les données en dehors de la partie asynchrone
            final String userEmail = user.getEmail();
            final String userName = (user.getName() != null ? user.getName() : user.getUsername());
            final String eventName = event.getName();
            final String eventDate = (event.getEventDate() != null ? event.getEventDate().toString() : "Date non spécifiée");
            final String eventDescription = (event.getDescription() != null ? event.getDescription() : "Aucune description disponible");
            final String location = event.getLocation() != null ? event.getLocation() : "";
            
            // Encoder l'emplacement pour Google Maps
            final String encodedLocation = java.net.URLEncoder.encode(location, java.nio.charset.StandardCharsets.UTF_8);
            final String mapsLink = "https://www.google.com/maps/place/" + encodedLocation;
            
            // Seulement après avoir préparé toutes les données, démarrer l'envoi asynchrone
            CompletableFuture.runAsync(() -> {
                try {
                    // Création du contenu HTML de l'email en utilisant les données préparées
                    String htmlContent = 
                        "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                        "<div style='background-color: #4a89dc; color: white; padding: 20px; text-align: center;'>" +
                        "<h1>Confirmation d'inscription</h1>" +
                        "</div>" +
                        "<div style='padding: 20px;'>" +
                        "<p>Bonjour " + userName + ",</p>" +
                        "<p>Nous confirmons votre inscription à l'événement suivant :</p>" +
                        "<h2>" + eventName + "</h2>" +
                        "<ul>" +
                        "<li><strong>Date :</strong> " + eventDate + "</li>" +
                        "<li><strong>Description :</strong> " + eventDescription + "</li>" +
                        (location.isEmpty() ? "" : ("<li><strong>Lieu :</strong> " + location + "<br/><a href='" + mapsLink + "' target='_blank' style='color:#4a89dc;text-decoration:underline;'>Voir sur Google Maps</a></li>")) +
                        "</ul>" +
                        "<p>Nous sommes impatients de vous y retrouver !</p>" +
                        "</div>" +
                        "<div style='background-color: #f5f5f5; padding: 10px 20px; text-align: center; font-size: 12px; color: #777;'>" +
                        "<p>Cet email a été envoyé automatiquement. Merci de ne pas y répondre.</p>" +
                        "<p>&copy; 2025 ElitGo. Tous droits réservés.</p>" +
                        "</div>" +
                        "</div>";
                    
                    // Envoi de l'email avec un nom d'expéditeur personnalisé pour l'événement
                    String subject = "Confirmation d'inscription à " + eventName;
                    String senderName = "ElitGo Événements";
                    System.out.println("Envoi de l'email de confirmation d'inscription à " + userEmail + " pour l'événement " + eventName);
                    sendHtmlEmail(userEmail, subject, htmlContent, senderName);
                    
                    System.out.println("Email de confirmation d'inscription envoyé avec succès à " + userEmail);
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'envoi asynchrone de l'email de confirmation : " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la préparation de l'email de confirmation : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Envoie un email d'annulation d'inscription à un événement de manière asynchrone
     */
    public void sendEventCancellationEmailAsync(Integer userId, Integer eventId) {
        CompletableFuture.runAsync(() -> {
            try {
                // Récupérer les informations nécessaires
                User user = userService.getUserById(userId);
                Event event = eventService.getEventById(eventId);
                
                if (user == null || event == null || user.getEmail() == null) {
                    System.err.println("Impossible d'envoyer l'email : utilisateur, événement ou email manquant");
                    return;
                }
                
                // Création du contenu HTML de l'email
                String htmlContent = 
                    "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                    "<div style='background-color: #e74c3c; color: white; padding: 20px; text-align: center;'>" +
                    "<h1>Annulation d'inscription</h1>" +
                    "</div>" +
                    "<div style='padding: 20px;'>" +
                    "<p>Bonjour " + (user.getName() != null ? user.getName() : user.getUsername()) + ",</p>" +
                    "<p>Nous confirmons l'annulation de votre inscription à l'événement suivant :</p>" +
                    "<h2>" + event.getName() + "</h2>" +
                    "<ul>" +
                    "<li><strong>Date :</strong> " + (event.getEventDate() != null ? event.getEventDate().toString() : "Date non spécifiée") + "</li>" +
                    "<li><strong>Description :</strong> " + (event.getDescription() != null ? event.getDescription() : "Aucune description disponible") + "</li>" +
                    "</ul>" +
                    "<p>Nous espérons vous revoir bientôt pour d'autres événements.</p>" +
                    "</div>" +
                    "<div style='background-color: #f5f5f5; padding: 10px 20px; text-align: center; font-size: 12px; color: #777;'>" +
                    "<p>Cet email a été envoyé automatiquement. Merci de ne pas y répondre.</p>" +
                    "<p>&copy; 2025 ElitGo. Tous droits réservés.</p>" +
                    "</div>" +
                    "</div>";
                
                // Envoi de l'email
                sendHtmlEmail(
                    user.getEmail(),
                    "Annulation de votre inscription à " + event.getName(),
                    htmlContent
                );
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email d'annulation : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Envoie un email avec les résultats du quiz
     */
    public void sendQuizResultsEmailAsync(Integer userId, Integer quizId, Map<Integer, Integer> userAnswers, Map<Integer, Boolean> correctnessMap, int score, boolean passed) {
        try {
            // Récupérer les informations nécessaires AVANT la partie asynchrone (pendant que la session Hibernate est encore ouverte)
            User user = userService.getUserById(userId);
            
            // Arrêter si l'utilisateur n'existe pas ou n'a pas d'email
            if (user == null || user.getEmail() == null) {
                System.err.println("Impossible d'envoyer l'email : utilisateur ou email manquant");
                return;
            }
            
            // Récupérer les détails du quiz
            ClubQuiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new NoSuchElementException("Quiz non trouvé avec l'ID: " + quizId));
            
            // Récupérer toutes les données des questions et options avant la partie asynchrone
            List<ClubQuizQuestion> questions = questionRepository.findByQuizId(quizId);
            
            // Préparer toutes les données nécessaires avant l'exécution asynchrone
            final String userEmail = user.getEmail();
            final String userName = (user.getName() != null ? user.getName() : user.getUsername());
            final String quizTitle = quiz.getTitle();
            final String clubName = (quiz.getClub() != null ? quiz.getClub().getName() : "");
            
            // Construire le contenu HTML pendant que la session est encore ouverte
            StringBuilder questionsHtml = new StringBuilder();
            
            for (ClubQuizQuestion question : questions) {
                Integer userAnswer = userAnswers.get(question.getId());
                boolean isCorrect = correctnessMap.get(question.getId()) != null ? correctnessMap.get(question.getId()) : false;
                
                // Important: Créer une copie de la liste d'options pour éviter l'erreur lazy loading
                List<String> optionsCopy = new java.util.ArrayList<>();
                for (String option : question.getOptions()) {
                    optionsCopy.add(option); // Copie chaque option dans une nouvelle liste
                }
                
                questionsHtml.append("<div style='margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px;'>")
                        .append("<h3 style='margin-top: 0;'>" + question.getQuestionText() + "</h3>")
                        .append("<ul style='list-style: none; padding-left: 0;'>");
                
                // Parcourir les options disponibles
                for (int i = 0; i < optionsCopy.size(); i++) {
                    String option = optionsCopy.get(i);
                    if (option != null && !option.isEmpty()) {
                        String style = "";
                        if (i == question.getCorrectOptionIndex()) {
                            // Bonne réponse toujours en vert
                            style = "color: #2ecc71; font-weight: bold;";
                        } else if (i == userAnswer && !isCorrect) {
                            // Mauvaise réponse de l'utilisateur en rouge
                            style = "color: #e74c3c; text-decoration: line-through;";
                        }
                        
                        // Ajouter une icône pour indiquer si c'est correct ou incorrect
                        String icon = "";
                        if (i == question.getCorrectOptionIndex()) {
                            icon = "&#9989; "; // Coche verte
                        } else if (i == userAnswer && !isCorrect) {
                            icon = "&#10060; "; // Croix rouge
                        }
                        
                        questionsHtml.append("<li style='margin-bottom: 8px; " + style + "'>" + icon + option + "</li>");
                    }
                }
                
                questionsHtml.append("</ul></div>");
            }
            
            // Déterminer le style et le message basé sur la réussite
            final String headerColor = passed ? "#2ecc71" : "#e74c3c";
            final String resultMessage = passed ? "Félicitations ! Vous avez réussi le quiz." : "Vous n'avez pas atteint le score minimum requis pour réussir ce quiz.";
            final String resultDetails = passed ? "Vous êtes maintenant membre du club !" : "N'hésitez pas à réessayer.";
            
            // Générer le contenu HTML final pour l'email
            final String questionsHtmlString = questionsHtml.toString();
            
            // Exécuter l'envoi d'email de manière asynchrone avec toutes les données préparées
            CompletableFuture.runAsync(() -> {
                try {
                    // Définir le sujet de l'email basé sur la réussite ou non
                    final String subject = passed ? "Félicitations - Vous avez réussi le quiz !" : "Résultats de votre quiz";
                    
                    // Création du contenu HTML de l'email en utilisant les données déjà préparées
                    String htmlContent = 
                        "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                        "<div style='background-color: " + headerColor + "; color: white; padding: 20px; text-align: center;'>" +
                        "<h1>Résultats du Quiz</h1>" +
                        "</div>" +
                        "<div style='padding: 20px;'>" +
                        "<p>Bonjour " + userName + ",</p>" +
                        "<p>Voici les résultats de votre quiz <strong>" + quizTitle + "</strong> pour le club <strong>" + clubName + "</strong>.</p>" +
                        
                        "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                        "<h2 style='margin-top: 0; text-align: center;'>Votre score: <span style='color: " + headerColor + ";'>" + score + "%</span></h2>" +
                        "<p style='text-align: center; font-weight: bold;'>" + resultMessage + "</p>" +
                        "<p style='text-align: center;'>" + resultDetails + "</p>" +
                        "</div>" +
                        
                        "<h3>Détail des questions et réponses:</h3>" +
                        questionsHtmlString +
                        
                        "<hr style='margin: 30px 0;'>" +
                        "<p>Cordialement,</p>" +
                        "<p>L'équipe ElitGo</p>" +
                        "</div>" +
                        "<div style='background-color: #f5f5f5; padding: 10px 20px; text-align: center; font-size: 12px; color: #777;'>" +
                        "<p>Cet email a été envoyé automatiquement. Merci de ne pas y répondre.</p>" +
                        "<p>&copy; 2025 ElitGo. Tous droits réservés.</p>" +
                        "</div>" +
                        "</div>";
                    
                    // Générer un PDF avec les résultats du quiz si le service est disponible
                    byte[] pdfContent = null;
                    if (quizPdfService != null) {
                        try {
                            // Vérifier si la génération de PDF est activée
                            boolean pdfEnabled = quizPdfService.isPdfGenerationEnabled();
                            
                            if (pdfEnabled) {
                                // Générer le PDF à partir du contenu HTML
                                pdfContent = quizPdfService.generatePdfFromHtml(htmlContent);
                                System.out.println("PDF généré avec succès, taille: " + (pdfContent != null ? pdfContent.length : 0) + " octets");
                            }
                        } catch (Exception e) {
                            // Ne pas bloquer l'envoi de l'email si la génération du rapport échoue
                            System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());
                            e.printStackTrace();
                            // Continuer sans le PDF
                            pdfContent = null;
                        }
                    }
                    String senderName = "Club " + clubName;
                    String reportFilename = "Resultats_Quiz_" + quizTitle.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
                    
                    // Envoyer l'email avec ou sans pièce jointe selon si le rapport a été généré avec succès
                    System.out.println("Envoi de l'email à " + userEmail + " depuis " + senderName);
                    if (pdfContent != null && pdfContent.length > 0) {
                        // Utiliser le contenu comme pièce jointe texte
                        try {
                            MimeMessage message = mailSender.createMimeMessage();
                            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                            helper.setTo(userEmail);
                            helper.setSubject(subject);
                            helper.setText(htmlContent, true);
                            helper.setFrom(new InternetAddress("noreply@elitgo.com", senderName));
                            
                            // Ajouter le rapport en pièce jointe
                            helper.addAttachment(reportFilename, new ByteArrayResource(pdfContent), "application/pdf");
                            
                            mailSender.send(message);
                            System.out.println("Email envoyé avec la pièce jointe: " + reportFilename);
                        } catch (Exception e) {
                            System.err.println("Erreur lors de l'envoi de l'email avec pièce jointe: " + e.getMessage());
                            e.printStackTrace();
                            // En cas d'échec, essayer d'envoyer sans pièce jointe
                            sendHtmlEmail(userEmail, subject, htmlContent, senderName);
                        }
                    } else {
                        sendHtmlEmail(userEmail, subject, htmlContent, senderName);
                        System.out.println("Email envoyé sans pièce jointe (rapport non généré ou service non disponible)");
                    }
                    
                    System.out.println("Email de résultats du quiz envoyé avec succès à " + userEmail);
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'envoi asynchrone de l'email : " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la préparation de l'email des résultats du quiz : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
