package com.desafio.agenda.Model;

import com.desafio.agenda.Validation.CNPJ;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
;

import java.util.Set;

@Entity
@Table(name="contato_cnpj",  schema= "agenda")
public class ContatoCnpj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[0-9]+$", message = "Apenas números são permitidos")
    @CNPJ
    private String cnpj;

    @NotEmpty
    private String cnpjName;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]+$", message = "Apenas números são permitidos")
    private String phoneNumber;

    private String description;

    public ContatoCnpj() {
    }

    public Set<Addresses> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Addresses> addresses) {
        this.addresses = addresses;
    }

    @ManyToMany
    @JoinTable(
            name = "addresses_cnpj",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "contato_cnpj_id"))
    private Set<Addresses> addresses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
