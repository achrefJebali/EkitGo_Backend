package tn.esprit.examenspring.services;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.CertificateRepository;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.Certificate;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.entities.User;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Slf4j
public class CertificateServiceImpl implements ICertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserRepository studentRepository;
    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private ProgressServiceImpl progressService;

    @Override
    public Certificate issueCertificate(Integer userId, Integer formationId) {
        log.info("Attempting to issue certificate for userId: {}, formationId: {}", userId, formationId);
        if (!canIssueCertificate(userId, formationId)) {
            log.warn("Cannot issue certificate: Course not completed or quiz not passed for userId: {}, formationId: {}", userId, formationId);
            throw new RuntimeException("Cannot issue certificate: Course not completed or quiz not passed");
        }

        log.debug("Fetching user with userId: {}", userId);
        User student = studentRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Student not found for userId: {}", userId);
                    return new RuntimeException("Student not found");
                });

        log.debug("Fetching formation with formationId: {}", formationId);
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> {
                    log.error("Formation not found for formationId: {}", formationId);
                    return new RuntimeException("Formation not found");
                });

        log.debug("Checking if certificate already exists for userId: {}, formationId: {}", userId, formationId);
        Certificate certificate = certificateRepository.findByUserIdAndFormationId(userId, formationId)
                .orElse(new Certificate());
        certificate.setUser(student);
        certificate.setFormation(formation);
        certificate.setIssueDate(new Date());
        log.info("Saving certificate for userId: {}, formationId: {}", userId, formationId);
        return certificateRepository.save(certificate);
    }

    @Override
    public Certificate getCertificate(Integer userId, Integer formationId) {
        log.info("Fetching certificate for userId: {}, formationId: {}", userId, formationId);
        Certificate certificate = certificateRepository.findByUserIdAndFormationId(userId, formationId)
                .orElse(null);
        if (certificate == null) {
            log.info("Certificate not found for userId: {}, formationId: {}", userId, formationId);
        } else {
            log.debug("Certificate found: {}", certificate);
        }
        return certificate;
    }

    @Override
    public boolean canIssueCertificate(Integer userId, Integer formationId) {
        log.debug("Checking if certificate can be issued for userId: {}, formationId: {}", userId, formationId);
        boolean canIssue = progressService.isCourseCompleted(userId, formationId);
        log.debug("Can issue certificate: {}", canIssue);
        return canIssue;
    }

    @Override
    public byte[] generateCertificatePdf(Integer userId, Integer formationId) throws Exception {
        log.info("Generating certificate PDF for userId: {}, formationId: {}", userId, formationId);

        // Fetch user and formation details
        log.debug("Fetching user with userId: {}", userId);
        User user = studentRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for userId: {}", userId);
                    return new RuntimeException("User not found");
                });

        log.debug("Fetching formation with formationId: {}", formationId);
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> {
                    log.error("Formation not found for formationId: {}", formationId);
                    return new RuntimeException("Formation not found");
                });

        log.debug("Fetching certificate for userId: {}, formationId: {}", userId, formationId);
        Certificate certificate = getCertificate(userId, formationId);
        if (certificate == null) {
            log.error("Certificate not found for userId: {}, formationId: {}", userId, formationId);
            throw new RuntimeException("Certificate not found for userId: " + userId + ", formationId: " + formationId);
        }

        // Create a ByteArrayOutputStream to hold the PDF content
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            // Explicitly add a page to the document
            pdf.addNewPage();
            Document document = new Document(pdf, PageSize.A4);

            // Add a decorative border to the page
            PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage());
            canvas.setStrokeColor(new DeviceRgb(0, 102, 204)) // Blue border
                    .setLineWidth(5)
                    .rectangle(20, 20, PageSize.A4.getWidth() - 40, PageSize.A4.getHeight() - 40)
                    .stroke();

            // Add title
            Paragraph title = new Paragraph(new Text("Certificate of Achievement"))
                    .setFontSize(28)
                    .setBold()
                    .setFontColor(new DeviceRgb(0, 102, 204))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(title);

            // Add a horizontal line
            document.add(new Paragraph()
                    .add(new Text("________________________________________________"))
                    .setFontColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setMarginBottom(20));

            // Add content
            document.add(new Paragraph("This certifies that")
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(new Text(user.getName()))
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));
            document.add(new Paragraph("has successfully completed the course")
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));
            document.add(new Paragraph(new Text(formation.getTitle()))
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));

            // Convert Date to LocalDate for formatting
            log.debug("Formatting issue date: {}", certificate.getIssueDate());
            Date issueDate = certificate.getIssueDate();
            if (issueDate == null) {
                log.warn("Issue date is null for certificate. Using current date.");
                issueDate = new Date();
            }
            String formattedDate = issueDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            document.add(new Paragraph("on " + formattedDate)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));

            // Add a signature line
            document.add(new Paragraph()
                    .add(new Text("___________________________"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30));
            document.add(new Paragraph("Authorized Signature")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            // Add a footer
            Paragraph footer = new Paragraph("ElitGo Education - Empowering Future Leaders")
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(50, 30, PageSize.A4.getWidth() - 100);
            document.add(footer);

            // Close the document
            document.close();
            log.info("Successfully generated certificate PDF for userId: {}, formationId: {}", userId, formationId);
        } catch (Exception e) {
            log.error("Failed to generate PDF for userId: {}, formationId: {}: {}", userId, formationId, e.getMessage(), e);
            throw new RuntimeException("Failed to generate certificate PDF: " + e.getMessage(), e);
        }

        return baos.toByteArray();
    }
}