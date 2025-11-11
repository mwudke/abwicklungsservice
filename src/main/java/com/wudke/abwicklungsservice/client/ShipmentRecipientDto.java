package com.wudke.abwicklungsservice.client;

public record ShipmentRecipientDto(
        String name,
        String street,
        String houseNumber,
        String zipCode,
        String city
) {
}
