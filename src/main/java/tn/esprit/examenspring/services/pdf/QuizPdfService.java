package tn.esprit.examenspring.services.pdf;

import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Service pour générer des rapports PDF de quiz à partir de contenu HTML
 * Cette implémentation utilise Apache PDFBox pour reproduire le design HTML
 */
@Service
public class QuizPdfService {

    // Couleurs pour le PDF
    private static final float[] COLOR_GREEN = new float[]{0.18f, 0.8f, 0.44f}; // #2ecc71
    private static final float[] COLOR_RED = new float[]{0.9f, 0.3f, 0.24f};    // #e74c3c
    private static final float[] COLOR_BLUE = new float[]{0.2f, 0.6f, 0.86f};   // #3498db
    private static final float[] COLOR_BLACK = new float[]{0, 0, 0};
    private static final float[] COLOR_WHITE = new float[]{1, 1, 1};

    /**
     * Génère un rapport PDF à partir du contenu HTML
     * 
     * @param htmlContent Le contenu HTML à convertir
     * @return Un tableau de bytes contenant le rapport PDF
     */
    public byte[] generatePdfFromHtml(String htmlContent) {
        System.out.println("Génération d'un PDF des résultats du quiz avec PDFBox...");
        
        try {
            // Nettoyer le HTML de manière agressive pour retirer tout le formattage problématique
            String cleanedHtml = htmlContent;
            
            // Remplacer d'abord les emojis par des marqueurs spéciaux
            cleanedHtml = cleanedHtml.replace("✅", "EMOJI_CHECK")
                                .replace("❌", "EMOJI_CROSS")
                                .replace("&#9989;", "EMOJI_CHECK")
                                .replace("&#10060;", "EMOJI_CROSS");
            
            // Ensuite, remplacer tous les styles et scripts
            cleanedHtml = cleanedHtml.replaceAll("<script[^>]*>.*?</script>", "");
            cleanedHtml = cleanedHtml.replaceAll("<style[^>]*>.*?</style>", "");
            
            // Extraire les informations importantes du HTML
            String title = extractTitle(cleanedHtml);
            QuizResult quizResult = extractQuizResult(cleanedHtml);
            
            // Extraire les questions et réponses
            String questionsAndAnswers = extractQuestionsAndAnswers(cleanedHtml);
            
            // Générer le PDF avec les données extraites
            return createPdf(title, quizResult, questionsAndAnswers);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());
            e.printStackTrace();
            // Générer un PDF d'erreur en cas de problème
            return createErrorPdf(e.getMessage());
        }
    }

    /**
     * Classe interne pour stocker les informations du quiz
     */
    private static class QuizResult {
        String score = "Score non disponible";
        float[] headerColor = COLOR_BLUE; // Couleur par défaut
        boolean isPassed = false;
        String resultMessage = "";
    }
    
    /**
     * Extrait les informations du quiz (score, couleurs, réussite)
     */
    private QuizResult extractQuizResult(String html) {
        QuizResult result = new QuizResult();
        
        try {
            // Extraire le score (pourcentage ou fraction)
            Pattern scorePattern = Pattern.compile("(\\d+)%|Score\\s*:\\s*(\\d+)\\s*/\\s*(\\d+)");
            Matcher scoreMatcher = scorePattern.matcher(html);
            
            if (scoreMatcher.find()) {
                if (scoreMatcher.group(1) != null) {
                    // Format pourcentage
                    result.score = scoreMatcher.group(1) + "%";
                } else if (scoreMatcher.group(2) != null && scoreMatcher.group(3) != null) {
                    // Format fraction X/Y
                    result.score = scoreMatcher.group(2) + "/" + scoreMatcher.group(3);
                }
            }
            
            // Déterminer si le quiz est réussi
            boolean hasSuccessMessage = html.contains("Félicitations") || 
                                      html.contains("réussi le quiz") ||
                                      html.contains("Bravo");
            
            // Détecter la couleur d'en-tête (généralement verte pour réussite, rouge pour échec)
            Pattern colorPattern = Pattern.compile("background-color:\\s*([#][0-9a-fA-F]{6})");
            Matcher colorMatcher = colorPattern.matcher(html);
            
            if (colorMatcher.find()) {
                String hexColor = colorMatcher.group(1);
                
                // Déterminer si c'est vert ou rouge en fonction du code hex
                if (hexColor.equalsIgnoreCase("#2ecc71")) {
                    // Vert - Quiz réussi
                    result.headerColor = COLOR_GREEN;
                    result.isPassed = true;
                } else if (hexColor.equalsIgnoreCase("#e74c3c")) {
                    // Rouge - Quiz échoué
                    result.headerColor = COLOR_RED;
                    result.isPassed = false;
                }
            } else {
                // Si pas de couleur trouvée, se baser sur le message
                result.isPassed = hasSuccessMessage;
                result.headerColor = hasSuccessMessage ? COLOR_GREEN : COLOR_RED;
            }
            
            // Extraire le message de résultat
            if (result.isPassed) {
                result.resultMessage = "Félicitations ! Vous avez réussi le quiz.";
            } else {
                result.resultMessage = "Vous n'avez pas atteint le score minimum requis pour réussir ce quiz.";
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction des résultats: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Extrait le titre du quiz depuis le contenu HTML
     */
    private String extractTitle(String html) {
        try {
            // Différents patterns pour extraire le titre du quiz
            String[] patterns = {
                "Quiz\\s+([^<>:]+)\\s+pour",  // "Quiz [TITRE] pour"
                "Résultats\\s+du\\s+quiz\\s+([^<>:]+)\\s+pour", // "Résultats du quiz [TITRE] pour"
                "<h1[^>]*>.*?Quiz.*?[:-]\\s*([^<>:]+)</h1>",  // <h1>Quiz: [TITRE]</h1>
                "<title>.*?Quiz.*?[:-]\\s*([^<>:]+)</title>",  // <title>Quiz: [TITRE]</title>
                "Quiz\\s+d'admission\\s+-\\s+([^<>:]+)"        // "Quiz d'admission - [TITRE]"
            };
            
            for (String patternStr : patterns) {
                Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher matcher = pattern.matcher(html);
                
                if (matcher.find()) {
                    String title = matcher.group(1).trim();
                    return title;
                }
            }
            
            // Si encore pas trouvé, chercher simplement "Quiz d'admission"
            if (html.contains("Quiz d'admission")) {
                return "Quiz d'admission";
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction du titre: " + e.getMessage());
        }
        
        return "Quiz";
    }
    
    /**
     * Extrait les questions et réponses du contenu HTML
     */
    private String extractQuestionsAndAnswers(String html) {
        StringBuilder result = new StringBuilder();
        
        try {
            // Trouver la section des questions
            int startIndex = html.indexOf("DÉTAIL DES QUESTIONS");
            if (startIndex == -1) {
                startIndex = html.indexOf("Détail des questions");
            }
            
            if (startIndex == -1) {
                return "Aucune question trouvée.";
            }
            
            // Extraire la section des questions
            String questionsSection = html.substring(startIndex);
            
            // Remplacer les émoticônes par des marqueurs
            questionsSection = questionsSection
                    .replace("✅", "EMOJI_CHECK")
                    .replace("❌", "EMOJI_CROSS")
                    .replace("&#9989;", "EMOJI_CHECK")
                    .replace("&#10060;", "EMOJI_CROSS");
            
            // Extraire chaque question et ses réponses
            Pattern questionPattern = Pattern.compile("<div[^>]*>\\s*<h3[^>]*>([^<]+)</h3>\\s*<ul[^>]*>(.*?)</ul>\\s*</div>", 
                                                   Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher questionMatcher = questionPattern.matcher(questionsSection);
            
            int questionNumber = 1;
            
            while (questionMatcher.find()) {
                String questionText = questionMatcher.group(1).trim();
                String answersHtml = questionMatcher.group(2);
                
                // Ajouter la question au résultat
                result.append("Question ").append(questionNumber++).append(": ").append(questionText).append("\n\n");
                
                // Extraire les réponses
                Pattern answerPattern = Pattern.compile("<li[^>]*>(.*?)</li>", Pattern.DOTALL);
                Matcher answerMatcher = answerPattern.matcher(answersHtml);
                
                while (answerMatcher.find()) {
                    String answerHtml = answerMatcher.group(1);
                    boolean isCorrect = answerHtml.contains("EMOJI_CHECK") || 
                                       (answerHtml.contains("color: green") || answerHtml.contains("color:#2ecc71"));
                    boolean isIncorrect = answerHtml.contains("EMOJI_CROSS") || 
                                         (answerHtml.contains("color: red") || answerHtml.contains("color:#e74c3c"));
                    
                    // Nettoyer le texte de la réponse
                    String answerText = answerHtml
                            .replaceAll("<[^>]*>", "")
                            .replace("EMOJI_CHECK", "")
                            .replace("EMOJI_CROSS", "")
                            .trim();
                    
                    // Ajouter la réponse au résultat
                    if (isCorrect) {
                        result.append("[EMOJI_CHECK] ").append(answerText).append("\n");
                    } else if (isIncorrect) {
                        result.append("[EMOJI_CROSS] ").append(answerText).append("\n");
                    } else {
                        result.append("- ").append(answerText).append("\n");
                    }
                }
                
                result.append("\n---------------------------------------\n\n");
            }
            
            // Si aucune question n'a été trouvée avec cette méthode, utiliser une approche alternative
            if (questionNumber == 1) {
                // Approche alternative basée sur le texte
                Pattern altPattern = Pattern.compile("(Question\\s+\\d+[^\\n]+)\\n(.*?)(?=\\n(?:Question\\s+\\d+|$))", 
                                                  Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                Matcher altMatcher = altPattern.matcher(questionsSection);
                
                while (altMatcher.find()) {
                    String question = altMatcher.group(1).trim();
                    String answerBlock = altMatcher.group(2).trim();
                    
                    result.append(question).append("\n\n");
                    
                    // Extraire les réponses
                    String[] answerLines = answerBlock.split("\\n");
                    for (String line : answerLines) {
                        line = line.trim();
                        if (!line.isEmpty()) {
                            if (line.contains("EMOJI_CHECK")) {
                                result.append("[EMOJI_CHECK] ").append(line.replace("EMOJI_CHECK", "").trim()).append("\n");
                            } else if (line.contains("EMOJI_CROSS")) {
                                result.append("[EMOJI_CROSS] ").append(line.replace("EMOJI_CROSS", "").trim()).append("\n");
                            } else if (line.startsWith("-")) {
                                result.append(line).append("\n");
                            } else if (!line.startsWith("---")) {
                                result.append("- ").append(line).append("\n");
                            }
                        }
                    }
                    
                    result.append("\n---------------------------------------\n\n");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction des questions et réponses: " + e.getMessage());
            e.printStackTrace();
            result.append("Erreur lors de l'extraction des questions et réponses.\n");
        }
        
        return result.toString();
    }
    
    /**
     * Crée un PDF à partir des informations extraites
     */
    private byte[] createPdf(String title, QuizResult quizResult, String questionsAndAnswers) {
        try {
            // Créer un document PDF
            PDDocument document = new PDDocument();
            
            // Ajouter une page
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            // Créer un flux pour écrire sur la page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Configuration des polices et marges
            PDType1Font titleFont = PDType1Font.HELVETICA_BOLD;
            PDType1Font headerFont = PDType1Font.HELVETICA_BOLD;
            PDType1Font normalFont = PDType1Font.HELVETICA;
            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;
            
            float titleFontSize = 16;
            float headerFontSize = 14;
            float normalFontSize = 11;
            float lineHeight = 15;
            float margin = 50;
            float width = page.getMediaBox().getWidth() - 2 * margin;
            float startX = margin;
            float y = page.getMediaBox().getHeight() - margin;
            
            // Dessiner l'en-tête coloré (comme dans l'email)
            // Rectangle d'en-tête
            contentStream.setNonStrokingColor(quizResult.headerColor[0], quizResult.headerColor[1], quizResult.headerColor[2]);
            contentStream.addRect(margin - 20, y - 40, width + 40, 60);
            contentStream.fill();
            
            // Écrire le titre principal en blanc sur l'en-tête coloré
            contentStream.beginText();
            contentStream.setFont(titleFont, titleFontSize);
            contentStream.setNonStrokingColor(COLOR_WHITE[0], COLOR_WHITE[1], COLOR_WHITE[2]);
            contentStream.newLineAtOffset(margin, y - 25);
            contentStream.showText("RÉSULTATS DU QUIZ");
            contentStream.endText();
            
            // Réinitialiser la couleur au noir pour le reste du texte
            contentStream.setNonStrokingColor(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2]);
            
            // Position après l'en-tête
            y -= 80;
            
            // Dessiner le titre du quiz
            if (title != null && !title.isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(boldFont, normalFontSize);
                contentStream.newLineAtOffset(startX, y);
                contentStream.showText("Quiz: " + title);
                contentStream.endText();
                y -= lineHeight * 1.5f;
            }
            
            // Afficher la date
            contentStream.beginText();
            contentStream.setFont(normalFont, normalFontSize);
            contentStream.newLineAtOffset(startX, y);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            contentStream.showText("Date: " + now.format(formatter));
            contentStream.endText();
            y -= lineHeight * 1.5f;
            
            // Afficher le score
            if (quizResult.score != null && !quizResult.score.equals("Score non disponible")) {
                contentStream.beginText();
                contentStream.setFont(boldFont, normalFontSize + 1);
                contentStream.newLineAtOffset(startX, y);
                contentStream.showText("Votre score: " + quizResult.score);
                contentStream.endText();
                y -= lineHeight;
            }
            
            // Afficher le message de résultat
            if (quizResult.resultMessage != null && !quizResult.resultMessage.isEmpty()) {
                contentStream.beginText();
                // Utiliser la couleur verte ou rouge selon le résultat
                contentStream.setNonStrokingColor(quizResult.headerColor[0], quizResult.headerColor[1], quizResult.headerColor[2]);
                contentStream.setFont(boldFont, normalFontSize);
                contentStream.newLineAtOffset(startX, y);
                contentStream.showText(quizResult.resultMessage);
                contentStream.endText();
                // Réinitialiser la couleur
                contentStream.setNonStrokingColor(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2]);
                y -= lineHeight * 2;
            }
            
            // Dessiner une ligne de séparation
            contentStream.setLineWidth(1);
            contentStream.moveTo(startX, y + 5);
            contentStream.lineTo(page.getMediaBox().getWidth() - margin, y + 5);
            contentStream.stroke();
            y -= lineHeight;
            
            // Titre de la section des questions
            contentStream.beginText();
            contentStream.setFont(headerFont, headerFontSize);
            contentStream.newLineAtOffset(startX, y);
            contentStream.showText("DÉTAIL DES QUESTIONS ET RÉPONSES");
            contentStream.endText();
            y -= lineHeight * 2;
            
            // Analyser le contenu des questions et réponses
            String[] lines = questionsAndAnswers.split("\n");
            
            for (String line : lines) {
                // Si on va déborder de la page, créer une nouvelle page
                if (y < margin + 20) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    y = page.getMediaBox().getHeight() - margin;
                }
                
                // Déterminer le style à appliquer
                boolean isQuestionHeader = line.startsWith("Question ");
                boolean isCorrectAnswer = line.contains("[EMOJI_CHECK]");
                boolean isIncorrectAnswer = line.contains("[EMOJI_CROSS]");
                boolean isSeparator = line.startsWith("-----");
                
                // Traitement des séparateurs
                if (isSeparator) {
                    contentStream.setLineWidth(0.5f);
                    contentStream.moveTo(startX, y);
                    contentStream.lineTo(startX + width, y);
                    contentStream.stroke();
                    y -= lineHeight;
                    continue;
                }
                
                // Appliquer le style approprié
                float xPosition = startX;
                PDType1Font font = normalFont;
                
                if (isQuestionHeader) {
                    font = boldFont;
                    y -= 5; // Espace supplémentaire avant les questions
                } else if (line.startsWith("-") || isCorrectAnswer || isIncorrectAnswer) {
                    xPosition = startX + 15; // Indenter les réponses
                }
                
                // Colorier les réponses correctes/incorrectes
                contentStream.beginText();
                
                if (isCorrectAnswer) {
                    contentStream.setNonStrokingColor(COLOR_GREEN[0], COLOR_GREEN[1], COLOR_GREEN[2]);
                    xPosition = startX; // Annuler l'indentation pour les réponses avec emoji
                    
                    // Dessiner un petit carré vert pour les bonnes réponses
                    contentStream.endText();
                    contentStream.setNonStrokingColor(COLOR_GREEN[0], COLOR_GREEN[1], COLOR_GREEN[2]);
                    contentStream.addRect(startX, y - 3, 10, 10);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(COLOR_GREEN[0], COLOR_GREEN[1], COLOR_GREEN[2]);
                    contentStream.beginText();
                    
                    // Nettoyer le texte pour affichage
                    line = line.replace("[EMOJI_CHECK]", "").trim();
                    xPosition = startX + 15;
                } else if (isIncorrectAnswer) {
                    contentStream.setNonStrokingColor(COLOR_RED[0], COLOR_RED[1], COLOR_RED[2]);
                    xPosition = startX; // Annuler l'indentation pour les réponses avec emoji
                    
                    // Dessiner un petit carré rouge pour les mauvaises réponses
                    contentStream.endText();
                    contentStream.setNonStrokingColor(COLOR_RED[0], COLOR_RED[1], COLOR_RED[2]);
                    contentStream.addRect(startX, y - 3, 10, 10);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(COLOR_RED[0], COLOR_RED[1], COLOR_RED[2]);
                    contentStream.beginText();
                    
                    // Nettoyer le texte pour affichage
                    line = line.replace("[EMOJI_CROSS]", "").trim();
                    xPosition = startX + 15;
                } else {
                    contentStream.setNonStrokingColor(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2]);
                }
                
                contentStream.setFont(font, normalFontSize);
                contentStream.newLineAtOffset(xPosition, y);
                contentStream.showText(line);
                contentStream.endText();
                
                // Réinitialiser la couleur au noir
                contentStream.setNonStrokingColor(COLOR_BLACK[0], COLOR_BLACK[1], COLOR_BLACK[2]);
                
                y -= lineHeight;
            }
            
            // Pied de page
            y = margin + 20;
            contentStream.beginText();
            contentStream.setFont(normalFont, 9);
            contentStream.newLineAtOffset(page.getMediaBox().getWidth() / 2 - 110, y);
            contentStream.showText("Ce rapport a été généré automatiquement par ElitGo");
            contentStream.endText();
            
            // Fermer le flux et sauvegarder le document
            contentStream.close();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();
            
            return baos.toByteArray();
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du PDF: " + e.getMessage());
            e.printStackTrace();
            return createErrorPdf("Erreur de création PDF: " + e.getMessage());
        }
    }
    
    /**
     * Crée un PDF d'erreur simple
     */
    private byte[] createErrorPdf(String errorMessage) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Titre
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Erreur lors de la génération du PDF");
            contentStream.endText();
            
            // Message d'erreur
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 670);
            contentStream.showText("Un problème est survenu lors de la génération du rapport.");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("Veuillez consulter l'email pour voir les résultats du quiz.");
            contentStream.endText();
            
            if (errorMessage != null && !errorMessage.isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                contentStream.newLineAtOffset(100, 620);
                contentStream.showText("Détail de l'erreur: " + 
                                   (errorMessage.length() > 80 ? errorMessage.substring(0, 77) + "..." : errorMessage));
                contentStream.endText();
            }
            
            contentStream.close();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();
            
            return baos.toByteArray();
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du PDF d'erreur: " + e.getMessage());
            e.printStackTrace();
            return "Erreur PDF".getBytes(StandardCharsets.UTF_8);
        }
    }
    
    /**
     * Vérifie si la génération de PDF est activée
     * @return true car cette implémentation est prête à l'emploi
     */
    public boolean isPdfGenerationEnabled() {
        return true;
    }
}
