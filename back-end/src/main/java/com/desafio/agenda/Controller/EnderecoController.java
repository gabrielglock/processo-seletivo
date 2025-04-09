package com.desafio.agenda.Controller;

import com.desafio.agenda.DTO.AddressesDTO;
import com.desafio.agenda.Mappers.AddressesMapper;
import com.desafio.agenda.Model.Addresses;
import com.desafio.agenda.Model.ContatoCnpj;
import com.desafio.agenda.Model.ContatoCpf;
import com.desafio.agenda.Repository.AddressesRepository;
import com.desafio.agenda.Repository.ContatoCnpjRepository;
import com.desafio.agenda.Repository.ContatoCpfRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/endereco")
public class EnderecoController {

    private final AddressesRepository addressesRepository;
    private final ContatoCpfRepository contatoCpfRepository;
    private final ContatoCnpjRepository contatoCnpjRepository;

    public EnderecoController(AddressesRepository addressesRepository,
                              ContatoCpfRepository contatoCpfRepository,
                              ContatoCnpjRepository contatoCnpjRepository) {
        this.addressesRepository = addressesRepository;
        this.contatoCpfRepository = contatoCpfRepository;
        this.contatoCnpjRepository = contatoCnpjRepository;
    }

    @PostMapping("/pf/{contatoId}")
    public ResponseEntity<?> adicionarEnderecoPf(@PathVariable Integer contatoId, @RequestBody AddressesDTO addressesDTO) {
        Optional<ContatoCpf> contatoOpt = contatoCpfRepository.findById(contatoId);
        if (contatoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato PF não encontrado.");
        }
        ContatoCpf contato = contatoOpt.get();


        Addresses endereco = AddressesMapper.toEntity(addressesDTO);

        endereco.setContatoCpf(contato);
        endereco.setContatoCnpj(null);

        Addresses novoEndereco = addressesRepository.save(endereco);
        contato.getAddresses().add(novoEndereco);
        contatoCpfRepository.save(contato);
        AddressesDTO novoEnderecoDTO = AddressesMapper.toDTO(novoEndereco);

        return ResponseEntity.ok(novoEnderecoDTO);
    }
    @GetMapping("/pf/{contatoId}")
    public ResponseEntity<?> listarEnderecosPf(@PathVariable Integer contatoId) {
        Optional<ContatoCpf> contatoOpt = contatoCpfRepository.findById(contatoId);
        if (contatoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato PF não encontrado.");
        }
        Set<Addresses> enderecos = contatoOpt.get().getAddresses();

        // Converter cada endereço para seu DTO correspondente
        Set<AddressesDTO> dtos = enderecos.stream()
                .map(AddressesMapper::toDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(dtos);
    }
    @DeleteMapping("/pf/{contatoId}/{enderecoId}")
    public ResponseEntity<?> removerEnderecoPf(@PathVariable Integer contatoId, @PathVariable Integer enderecoId) {
        Optional<ContatoCpf> contatoOpt = contatoCpfRepository.findById(contatoId);
        Optional<Addresses> enderecoOpt = addressesRepository.findById(enderecoId);

        if (contatoOpt.isEmpty() || enderecoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", "Contato ou endereço não encontrado."));
        }

        ContatoCpf contato = contatoOpt.get();
        Addresses endereco = enderecoOpt.get();

        // Remove a associação entre contato e endereço
        if (contato.getAddresses().remove(endereco)) {
            contatoCpfRepository.save(contato);
            // Se o endereço não for compartilhado, você pode optar por deletá-lo:
            // addressesRepository.delete(endereco);
            return ResponseEntity.ok(Map.of("mensagem", "Endereço desvinculado do contato PF."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensagem", "Endereço não vinculado a esse contato PF."));
        }
    }

    @PostMapping("/pj/{contatoId}")
    public ResponseEntity<?> adicionarEnderecoPj(@PathVariable Integer contatoId, @RequestBody AddressesDTO addressesDTO) {
        Optional<ContatoCnpj> contatoOpt = contatoCnpjRepository.findById(contatoId);
        if (contatoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato PJ não encontrado.");
        }
        ContatoCnpj contato = contatoOpt.get();

        // Converter DTO para entidade
        Addresses endereco = AddressesMapper.toEntity(addressesDTO);
        // Assegure que o endereço será vinculado apenas ao contato PF
        endereco.setContatoCnpj(contato);
        endereco.setContatoCpf(null);

        // Salvar o endereço e associar ao contato
        Addresses novoEndereco = addressesRepository.save(endereco);
        contato.getAddresses().add(novoEndereco);
        contatoCnpjRepository.save(contato);

        // Converter a entidade salva para DTO
        AddressesDTO novoEnderecoDTO = AddressesMapper.toDTO(novoEndereco);

        return ResponseEntity.ok(novoEnderecoDTO);
    }
    @GetMapping("/pj/{contatoId}")
    public ResponseEntity<?> listarEnderecosPj(@PathVariable Integer contatoId) {
        Optional<ContatoCnpj> contatoOpt = contatoCnpjRepository.findById(contatoId);
        if (contatoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato PJ não encontrado.");
        }
        Set<Addresses> enderecos = contatoOpt.get().getAddresses();

        // Converter cada endereço para seu DTO correspondente
        Set<AddressesDTO> dtos = enderecos.stream()
                .map(AddressesMapper::toDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(dtos);
    }
    @DeleteMapping("/pj/{contatoId}/{enderecoId}")
    public ResponseEntity<?> removerEnderecoPj(@PathVariable Integer contatoId, @PathVariable Integer enderecoId) {
        Optional<ContatoCnpj> contatoOpt = contatoCnpjRepository.findById(contatoId);
        Optional<Addresses> enderecoOpt = addressesRepository.findById(enderecoId);

        if (contatoOpt.isEmpty() || enderecoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", "Contato ou endereço não encontrado."));
        }

        ContatoCnpj contato = contatoOpt.get();
        Addresses endereco = enderecoOpt.get();


        if (contato.getAddresses().remove(endereco)) {
            contatoCnpjRepository.save(contato);

            return ResponseEntity.ok(Map.of("mensagem", "Endereço desvinculado do contato PF."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensagem", "Endereço não vinculado a esse contato PF."));
        }
    }


    @PutMapping("/update/{enderecoId}")
    public ResponseEntity<?> atualizarEndereco(@PathVariable Integer enderecoId, @RequestBody AddressesDTO enderecoDTO) {
        Optional<Addresses> enderecoOpt = addressesRepository.findById(enderecoId);
        if (enderecoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", "Endereço não encontrado."));
        }

        Addresses endereco = enderecoOpt.get();

        endereco.setCep(enderecoDTO.getCep());
        endereco.setContryState(enderecoDTO.getContryState());
        endereco.setStateCity(enderecoDTO.getStateCity());
        endereco.setCityNeighborhood(enderecoDTO.getCityNeighborhood());
        endereco.setAddress(enderecoDTO.getAddress());
        endereco.setAddressExtra(enderecoDTO.getAddressExtra());
        endereco.setAddressNumber(enderecoDTO.getAddressNumber());

        Addresses atualizado = addressesRepository.save(endereco);

        AddressesDTO atualizadoDTO = AddressesMapper.toDTO(atualizado);

        return ResponseEntity.ok(atualizadoDTO);
    }






}
