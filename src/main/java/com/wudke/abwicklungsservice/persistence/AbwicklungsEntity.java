package com.wudke.abwicklungsservice.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name = "abwicklungen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbwicklungsEntity {

    @Id
    private UUID id;

    @Column(name = "licence_plate")
    private String licencePlate;

    @Column(name = "payment_state")
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private RecipientEntity recipient;

    @Column(name = "print_id")
    private String printId;
}
