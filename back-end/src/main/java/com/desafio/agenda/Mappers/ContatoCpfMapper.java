package com.desafio.agenda.Mappers;

import com.desafio.agenda.Model.ContatoCpf;
import com.desafio.agenda.DTO.ContatoCpfDTO;
import com.desafio.agenda.DTO.AddressesDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class ContatoCpfMapper {

    public static ContatoCpfDTO toDTO(ContatoCpf contato) {
        ContatoCpfDTO dto = new ContatoCpfDTO();
        dto.setId(contato.getIdPf());
        dto.setCpf(contato.getCpf());
        dto.setFirstName(contato.getFirstName());
        dto.setSurName(contato.getSurName());
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
