package com.desafio.agenda.DTO;

import java.util.Set;

public class ContatoCnpjDTO {
    private Integer id;
    private String cnpj;
    private String cnpjName;
    private String email;
    private String phoneNumber;
    private String description;
    private Set<AddressesDTO> addresses;

    // Getters e Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public String getCnpjName() {
        return cnpjName;
    }
    public void setCnpjName(String cnpjName) {
        this.cnpjName = cnpjName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<AddressesDTO> getAddresses() {
        return addresses;
    }
    public void setAddresses(Set<AddressesDTO> addresses) {
        this.addresses = addresses;
    }
}
