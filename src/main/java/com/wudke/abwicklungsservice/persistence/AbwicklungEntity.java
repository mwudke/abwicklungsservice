package com.wudke.abwicklungsservice.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abwicklungen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbwicklungEntity {

    @Id
    private String id;

    @Column(name = "licence_plate", nullable = false)
    private String licencePlate;

    @Column(name = "payment_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;

    @Column(name = "recipient_id")
    private String recipientId;

    @Column(name = "print_id")
    private String printId;
}
