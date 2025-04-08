package com.desafio.agenda.Controller;


import com.desafio.agenda.DTO.ContatoCnpjDTO;
import com.desafio.agenda.DTO.ContatoCpfDTO;
import com.desafio.agenda.Mappers.ContatoCnpjMapper;
import com.desafio.agenda.Mappers.ContatoCpfMapper;
import com.desafio.agenda.Model.Addresses;
import com.desafio.agenda.Model.ContatoCnpj;
import com.desafio.agenda.Model.ContatoCpf;
import com.desafio.agenda.Repository.AddressesRepository;
import com.desafio.agenda.Repository.ContatoCnpjRepository;
import com.desafio.agenda.Repository.ContatoCpfRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AgendaController {

    private final ContatoCpfRepository contatoCpfRepository;
    private final ContatoCnpjRepository contatoCnpjRepository;


    public AgendaController(ContatoCpfRepository contatoCpfRepository, ContatoCnpjRepository contatoCnpjRepository, AddressesRepository addressesRepository) {
        this.contatoCpfRepository = contatoCpfRepository;
        this.contatoCnpjRepository = contatoCnpjRepository;

    }

    @GetMapping("/api/contacts")
    public ResponseEntity<?> allContacts(
            @RequestParam(defaultValue = "todos") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {


        switch (type.toLowerCase()) {
            case "pf":
                Pageable pageablePf = PageRequest.of(page, size);
                Page<ContatoCpf> pfPage = contatoCpfRepository.findAll(pageablePf);
                // Converte cada entidade para DTO
                Page<ContatoCpfDTO> pfDtoPage = pfPage.map(ContatoCpfMapper::toDTO);
                return ResponseEntity.ok(pfDtoPage);

            case "pj":
                Pageable pageablePj = PageRequest.of(page, size);
                Page<ContatoCnpj> pjPage = contatoCnpjRepository.findAll(pageablePj);
                Page<ContatoCnpjDTO> pjDtoPage = pjPage.map(ContatoCnpjMapper::toDTO);
                return ResponseEntity.ok(pjDtoPage);

            case "todos":
                Pageable pageable = PageRequest.of(page, size/2);
                List<ContatoCpfDTO> pfDtos = contatoCpfRepository.findAll(pageable).getContent().stream()
                        .map(ContatoCpfMapper::toDTO)
                        .collect(Collectors.toList());
                List<ContatoCnpjDTO> pjDtos = contatoCnpjRepository.findAll(pageable).getContent().stream()
                        .map(ContatoCnpjMapper::toDTO)
                        .collect(Collectors.toList());
                Map<String, Object> result = new HashMap<>();
                result.put("pf", pfDtos);
                result.put("pj", pjDtos);
                return ResponseEntity.ok(result);

            default:
                return ResponseEntity.badRequest().body("Tipo inválido. Use 'pf', 'pj' ou 'todos'.");
        }
    }

    @GetMapping("/api/contacts/search")
    public ResponseEntity<?> searchContacts(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String query
    ) {
        if (id == null && query == null) {
            return ResponseEntity.badRequest().body("Informe pelo menos o parâmetro 'id' ou 'query'.");
        }

        List<Object> resultados = new ArrayList<>();

        // Busca por ID
        if (id != null) {
            contatoCpfRepository.findById(id).ifPresent(cpf -> {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pf");
                contatoMap.put("contato", ContatoCpfMapper.toDTO(cpf));
                resultados.add(contatoMap);
            });

            contatoCnpjRepository.findById(id).ifPresent(cnpj -> {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pj");
                contatoMap.put("contato", ContatoCnpjMapper.toDTO(cnpj));
                resultados.add(contatoMap);
            });
        }

        // Busca por 'query': pode ser nome, CPF ou CNPJ
        if (query != null) {
            // Para contatos PF: busca por nome e CPF
            List<ContatoCpf> contatosPfByNome = contatoCpfRepository.findByFirstNameContainingIgnoreCase(query);
            List<ContatoCpf> contatosPfByCpf = contatoCpfRepository.findByCpfStartingWith(query);
            Set<ContatoCpf> pfSet = new HashSet<>();
            pfSet.addAll(contatosPfByNome);
            pfSet.addAll(contatosPfByCpf);

            for (ContatoCpf cpf : pfSet) {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pf");
                contatoMap.put("contato", ContatoCpfMapper.toDTO(cpf));
                resultados.add(contatoMap);
            }

            // Para contatos PJ: busca por nome da empresa e por CNPJ
            List<ContatoCnpj> contatosPjByNome = contatoCnpjRepository.findByCnpjNameContainingIgnoreCase(query);
            List<ContatoCnpj> contatosPjByCnpj = contatoCnpjRepository.findByCnpjStartingWith(query);
            Set<ContatoCnpj> pjSet = new HashSet<>();
            pjSet.addAll(contatosPjByNome);
            pjSet.addAll(contatosPjByCnpj);

            for (ContatoCnpj pj : pjSet) {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pj");
                contatoMap.put("contato", ContatoCnpjMapper.toDTO(pj));
                resultados.add(contatoMap);
            }
        }

        return ResponseEntity.ok(resultados);
    }






    @PostMapping("/api/newContact/pf")
    public ResponseEntity<?> newContactPf(@Valid @RequestBody ContatoCpf contatoCpf){
        try {
            ContatoCpf saved = contatoCpfRepository.save(contatoCpf);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            //TODO mensagens para SQL exceptions

            return ResponseEntity.status(500).body("Erro ao salvar contato PF: " + e.getMessage());
        }
    }

    @PostMapping("/api/newContact/pj")
    public ResponseEntity<?> newContactPj(@Valid @RequestBody ContatoCnpj contatoCnpj){

        try {

            ContatoCnpj saved = contatoCnpjRepository.save(contatoCnpj);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {


            return ResponseEntity.status(500).body("Erro ao salvar contato PJ: " + e.getMessage());
        }
    }
    @PutMapping("/api/updateContact/pf/{id}")
    public ResponseEntity<?> updateContactPf(@PathVariable Integer id, @Valid @RequestBody ContatoCpf contatoCpf) {
        Optional<ContatoCpf> optionalContato = contatoCpfRepository.findById(id);
        if (optionalContato.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato PF não encontrado com o ID " + id);
        }

        ContatoCpf existing = optionalContato.get();



        existing.setCpf(contatoCpf.getCpf());
        existing.setFirstName(contatoCpf.getFirstName());
        existing.setSurName(contatoCpf.getSurName());
        existing.setEmail(contatoCpf.getEmail());
        existing.setPhoneNumber(contatoCpf.getPhoneNumber());
        existing.setDescription(contatoCpf.getDescription());


        try {
            ContatoCpf updated = contatoCpfRepository.save(existing);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar contato PF: " + e.getMessage());
        }
    }


    @PutMapping("/api/updateContact/pj/{id}")
    public ResponseEntity<?> updateContactPj(@PathVariable Integer id, @Valid @RequestBody ContatoCnpj contatoCnpj) {
        Optional<ContatoCnpj> optionalContato = contatoCnpjRepository.findById(id);

        if (optionalContato.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato PJ não encontrado com o ID " + id);
        }

        ContatoCnpj existing = optionalContato.get();

        existing.setCnpj(contatoCnpj.getCnpj());
        existing.setCnpjName(contatoCnpj.getCnpjName());
        existing.setEmail(contatoCnpj.getEmail());
        existing.setPhoneNumber(contatoCnpj.getPhoneNumber());
        existing.setDescription(contatoCnpj.getDescription());


        try {
            ContatoCnpj updated = contatoCnpjRepository.save(existing);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar contato PJ: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/delContact/{contactType}/{contactId}")
    public ResponseEntity<?> deleteContact(
            @PathVariable String contactType,
            @PathVariable Integer contactId) {

        switch(contactType.toLowerCase()) {
            case "pf": {
                Optional<ContatoCpf> optionalPf = contatoCpfRepository.findById(contactId);
                if (optionalPf.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Contato PF não encontrado com o ID " + contactId);
                }
                try {
                    contatoCpfRepository.delete(optionalPf.get());
                    return ResponseEntity.ok("Contato PF deletado com sucesso.");
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Erro ao deletar contato PF: " + e.getMessage());
                }
            }
            case "pj": {
                Optional<ContatoCnpj> optionalPj = contatoCnpjRepository.findById(contactId);
                if (optionalPj.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Contato PJ não encontrado com o ID " + contactId);
                }
                try {
                    contatoCnpjRepository.delete(optionalPj.get());
                    return ResponseEntity.ok("Contato PJ deletado com sucesso.");
                } catch(Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Erro ao deletar contato PJ: " + e.getMessage());
                }
            }
            default:
                return ResponseEntity.badRequest()
                        .body("Tipo de contato inválido. Use 'pf' ou 'pj'.");
        }
    }


}
