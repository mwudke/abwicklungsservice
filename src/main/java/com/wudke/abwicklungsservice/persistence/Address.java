package com.wudke.abwicklungsservice.persistence;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String street;

    private String houseNumber;

    private String zipCode;

    private String city;
}
