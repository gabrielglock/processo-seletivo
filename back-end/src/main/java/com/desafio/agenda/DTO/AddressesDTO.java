package com.desafio.agenda.DTO;

public class AddressesDTO {
    private Integer id;
    private String cep;
    private String contryState;
    private String stateCity;
    private String cityNeighborhood;
    private String address;
    private String addressNumber;

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
