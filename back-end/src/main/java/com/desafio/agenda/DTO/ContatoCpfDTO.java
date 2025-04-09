package com.desafio.agenda.DTO;

import com.desafio.agenda.DTO.AddressesDTO;

import java.util.Set;

public class ContatoCpfDTO {
    private Integer id;
    private String cpf;
    private String firstName;
    private String surName;
    private String email;
    private String phoneNumber;
    private String description;
    private Set<AddressesDTO> addresses;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
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
