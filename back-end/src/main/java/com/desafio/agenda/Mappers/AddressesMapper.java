package com.desafio.agenda.Mappers;

import com.desafio.agenda.DTO.AddressesDTO;
import com.desafio.agenda.Model.Addresses;

public class AddressesMapper {

    public static AddressesDTO toDTO(Addresses address) {
        AddressesDTO dto = new AddressesDTO();
        dto.setId(address.getId());
        dto.setCep(address.getCep());
        dto.setContryState(address.getContryState());
        dto.setStateCity(address.getStateCity());
        dto.setCityNeighborhood(address.getCityNeighborhood());
        dto.setAddress(address.getAddress());
        dto.setAddressExtra(address.getAddressExtra());
        dto.setAddressNumber(address.getAddressNumber());
        return dto;
    }
    public static Addresses toEntity(AddressesDTO dto) {
        Addresses entity = new Addresses();
        entity.setCep(dto.getCep());
        entity.setContryState(dto.getContryState());
        entity.setStateCity(dto.getStateCity());
        entity.setCityNeighborhood(dto.getCityNeighborhood());
        entity.setAddress(dto.getAddress());
        entity.setAddressExtra(dto.getAddressExtra());
        entity.setAddressNumber(dto.getAddressNumber());
        return entity;
    }
}
