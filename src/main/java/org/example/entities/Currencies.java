package org.example.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public class Currencies {

    private Long id;
    private String code;
    private String name;
    private String sign;

    public Currencies(Long id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public Currencies(){
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "\"id\": " + id + ","+ "\n" +
                "\"code\": " + code + ","+ "\n" +
                "\"name\": " + name + ","+ "\n" +
                "\"sign\": " + sign + ","+ "\n" +
                "}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
