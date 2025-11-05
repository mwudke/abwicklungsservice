package com.wudke.abwicklungsservice.model;

import com.wudke.abwicklungsservice.persistence.PaymentState;

import java.util.UUID;

public record BezahlEvent(
        UUID reference,
        PaymentState status
) {
}
