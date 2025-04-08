package com.desafio.agenda.Model;


import com.desafio.agenda.Validation.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Set;

@Entity
@Table(name="contato_cpf", schema="agenda")
public class ContatoCpf {

    public ContatoCpf() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pf")
    private Integer idPf;

    @Pattern(regexp = "^[0-9]+$", message = "Apenas números são permitidos")
    @CPF
    private String cpf;

    @NotEmpty
    @Size(max = 30, message = "O primeiro nome deve ter no máximo 30 caracteres")
    private String firstName;

    @Size(max = 60, message = "O sobrenome deve ter no máximo 60 caracteres")
    private String surName;

    @Email(message = "O email deve ser válido")
    @Size(max = 319, message = "O email deve ter no máximo 319 caracteres")
    private String email;

    @Pattern(regexp = "^[0-9]+$", message = "Apenas números são permitidos")
    private String phoneNumber;

    @Size(max = 250, message = "A descrição deve ter no máximo 250 caracteres")
    private String description;

    @OneToMany(mappedBy = "contatoCpf", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Addresses> addresses;

    public Set<Addresses> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Addresses> addresses) {
        this.addresses = addresses;
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

    public Integer getIdPf() {
        return idPf;
    }
}



