package com.desafio.agenda.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Entity
@Table(name="addresses",  schema= "agenda")
public class Addresses {



    public Addresses() {
    }


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

    private String addressExtra;

    public String getAddressExtra() {
        return addressExtra;
    }

    public void setAddressExtra(String addressExtra) {
        this.addressExtra = addressExtra;
    }

    @ManyToOne
    @JoinColumn(name = "contato_cpf_id_fk", referencedColumnName = "id_pf")
    private ContatoCpf contatoCpf;

    // Relacionamento com ContatoCnpj
    @ManyToOne
    @JoinColumn(name = "contato_cnpj_id_fk",referencedColumnName = "id_pj")
    private ContatoCnpj contatoCnpj;



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

    public ContatoCpf getContatoCpf() {
        return contatoCpf;
    }

    public void setContatoCpf(ContatoCpf contatoCpf) {
        this.contatoCpf = contatoCpf;
    }

    public ContatoCnpj getContatoCnpj() {
        return contatoCnpj;
    }

    public void setContatoCnpj(ContatoCnpj contatoCnpj) {
        this.contatoCnpj = contatoCnpj;
    }
}
