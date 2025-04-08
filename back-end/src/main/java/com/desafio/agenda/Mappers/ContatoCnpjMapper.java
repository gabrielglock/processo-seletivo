package com.desafio.agenda.Mappers;

import com.desafio.agenda.Model.ContatoCnpj;
import com.desafio.agenda.DTO.ContatoCnpjDTO;
import com.desafio.agenda.DTO.AddressesDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class ContatoCnpjMapper {

    public static ContatoCnpjDTO toDTO(ContatoCnpj contato) {
        ContatoCnpjDTO dto = new ContatoCnpjDTO();
        dto.setId(contato.getId());
        dto.setCnpj(contato.getCnpj());
        dto.setCnpjName(contato.getCnpjName());
        dto.setEmail(contato.getEmail());
        dto.setPhoneNumber(contato.getPhoneNumber());
        dto.setDescription(contato.getDescription());
        if (contato.getAddresses() != null) {
            Set<AddressesDTO> addressesDTO = contato.getAddresses().stream()
                    .map(AddressesMapper::toDTO)
                    .collect(Collectors.toSet());
            dto.setAddresses(addressesDTO);
        }
        return dto;
    }
}
