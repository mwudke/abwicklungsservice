package com.wudke.abwicklungsservice.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AddressConverter implements AttributeConverter<Address, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address address) {
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Address convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Address.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
