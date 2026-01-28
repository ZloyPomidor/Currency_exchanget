package org.example.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "fullName", "code", "sign"})
public record RCurrencies(Long id, String code, String fullName, String sign){
}
