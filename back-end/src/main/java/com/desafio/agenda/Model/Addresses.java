package com.desafio.agenda.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Entity
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[0-9]+$", message = "Apenas números são permitidos")
    private String cep;

    private String contryState;

    private String stateCity;

    private String cityNeighborhood;

    private String address;

    private String addressNumber;

    @ManyToMany
    private Set<ContatoCnpj> addresesCnpj;

    @ManyToMany
    private Set<ContatoCpf> addressesCpf;


    public Addresses() {
    }

    public Set<ContatoCnpj> getAddresesCnpj() {
        return addresesCnpj;
    }

    public void setAddresesCnpj(Set<ContatoCnpj> addresesCnpj) {
        this.addresesCnpj = addresesCnpj;
    }

    public Set<ContatoCpf> getContatosCpf() {
        return addressesCpf;
    }

    public void setContatosCpf(Set<ContatoCpf> addressCpf) {
        this.addressesCpf = addressCpf;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getContryState() {
        return contryState;
    }

    public void setContryState(String contryState) {
        this.contryState = contryState;
    }

    public String getStateCity() {
        return stateCity;
    }

    public void setStateCity(String stateCity) {
        this.stateCity = stateCity;
    }

    public String getCityNeighborhood() {
        return cityNeighborhood;
    }

    public void setCityNeighborhood(String cityNeighborhood) {
        this.cityNeighborhood = cityNeighborhood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }
}
