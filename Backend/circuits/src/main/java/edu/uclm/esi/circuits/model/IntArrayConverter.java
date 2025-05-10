package edu.uclm.esi.circuits.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
class IntArrayConverter implements AttributeConverter<int[][], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(int[][] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error al convertir int[][] a JSON", e);
        }
    }

    @Override
    public int[][] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, int[][].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error al convertir JSON a int[][]", e);
        }
    }
}
