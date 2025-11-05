package com.wudke.abwicklungsservice.client;

import com.wudke.abwicklungsservice.model.Recipient;

import java.util.UUID;

public record CreateShipmentDto(
        UUID parcelId,
        Recipient recipient
) {
}
