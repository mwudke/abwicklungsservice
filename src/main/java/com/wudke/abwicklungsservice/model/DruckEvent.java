package com.wudke.abwicklungsservice.model;

import java.util.UUID;

public record DruckEvent(
        UUID id,
        UUID reference,
        String licensePlate
) {
}
