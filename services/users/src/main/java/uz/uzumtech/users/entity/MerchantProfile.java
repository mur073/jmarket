package uz.uzumtech.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uz.uzumtech.users.generated.dto.MerchantVerificationStatusDto;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "merchant_profiles")
public class MerchantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String inn;

    @Column(nullable = false)
    private String legalAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MerchantVerificationStatusDto verificationStatus;

    @Column(nullable = false)
    private Double rating;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
