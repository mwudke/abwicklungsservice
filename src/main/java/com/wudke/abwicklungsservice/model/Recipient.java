package com.wudke.abwicklungsservice.model;

public record Recipient(
        String name,
        Recipient.Address address
) {
    public record Address(
            String street,
            String houseNumber,
            String zipCode,
            String city
    ) {
    }
}
