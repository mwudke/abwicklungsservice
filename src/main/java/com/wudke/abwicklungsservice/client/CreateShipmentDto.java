package com.wudke.abwicklungsservice.client;

public record CreateShipmentDto(
        String parcelId, //todo check if this is really String or can also be uuid
        ShipmentRecipientDto recipient
) {
}
