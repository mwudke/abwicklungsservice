package com.wudke.abwicklungsservice.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "recipients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Convert(converter = AddressConverter.class)
    @Column(name = "address", nullable = false)
    private Address address;
}
