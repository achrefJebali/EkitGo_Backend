package tn.esprit.examenspring.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date purchaseDate;
    private String paymentReference; // Stripe payment ID or reference
    private boolean isActive; // Track if the purchase is active/valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Formation formation;
}