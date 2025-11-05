package com.wudke.abwicklungsservice.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name = "recipients")
@Data
@NoArgsConstructor
public class RecipientEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Convert(converter = AddressTypeConverter.class)
    @Column(name = "address", nullable = false)
    private Address address;

    public RecipientEntity(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
