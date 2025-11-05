package com.wudke.abwicklungsservice.model;


import java.util.UUID;

public record BestellEvent(
        UUID id,
        String licensePlate,
        Recipient recipient
) {
}
