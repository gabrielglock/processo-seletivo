package com.desafio.agenda.Controller;


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

@RestController
public class AgendaController {

    private final ContatoCpfRepository contatoCpfRepository;
    private final ContatoCnpjRepository contatoCnpjRepository;
    private final AddressesRepository addressesRepository;

    public AgendaController(ContatoCpfRepository contatoCpfRepository, ContatoCnpjRepository contatoCnpjRepository, AddressesRepository addressesRepository) {
        this.contatoCpfRepository = contatoCpfRepository;
        this.contatoCnpjRepository = contatoCnpjRepository;
        this.addressesRepository = addressesRepository;
    }

    @GetMapping("/api/contacts")
    public ResponseEntity<?> allContacts(
            @RequestParam(defaultValue = "todos") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        switch (type.toLowerCase()) {
            case "pf":
                Page<ContatoCpf> pfPage = contatoCpfRepository.findAll(pageable);
                return ResponseEntity.ok(pfPage);

            case "pj":
                Page<ContatoCnpj> pjPage = contatoCnpjRepository.findAll(pageable);
                return ResponseEntity.ok(pjPage);

            case "todos":
                Map<String, Object> result = new HashMap<>();
                result.put("pf", contatoCpfRepository.findAll(pageable).getContent());
                result.put("pj", contatoCnpjRepository.findAll(pageable).getContent());
                return ResponseEntity.ok(result);

            default:
                return ResponseEntity.badRequest().body("Tipo inválido. Use 'pf', 'pj' ou 'todos'.");
        }
    }

    @GetMapping("/api/contacts/search")
    public ResponseEntity<?> searchContacts(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nome
    ) {
        if (id == null && nome == null) {
            return ResponseEntity.badRequest().body("Informe pelo menos o parâmetro 'id' ou 'nome'.");
        }

        List<Object> resultados = new ArrayList<>();

        // busca por id
            if (id != null) {
            contatoCpfRepository.findById(id).ifPresent(c -> {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pf");
                contatoMap.put("contato", c);
                resultados.add(contatoMap);
            });

            contatoCnpjRepository.findById(id).ifPresent(c -> {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pj");
                contatoMap.put("contato", c);
                resultados.add(contatoMap);
            });
        }

        // bysca por nome
        if (nome != null) {
            List<ContatoCpf> cpfs = contatoCpfRepository.findByFirstNameContainingIgnoreCase(nome);
            List<ContatoCnpj> cnpjs = contatoCnpjRepository.findByCnpjNameContainingIgnoreCase(nome);

            cpfs.forEach(c -> {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pf");
                contatoMap.put("contato", c);
                resultados.add(contatoMap);
            });

            cnpjs.forEach(c -> {
                Map<String, Object> contatoMap = new HashMap<>();
                contatoMap.put("tipo", "pj");
                contatoMap.put("contato", c);
                resultados.add(contatoMap);
            });
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


}
