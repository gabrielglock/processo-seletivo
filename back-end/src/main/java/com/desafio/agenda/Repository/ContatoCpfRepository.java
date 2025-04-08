package com.desafio.agenda.Repository;

import com.desafio.agenda.Model.ContatoCpf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatoCpfRepository extends JpaRepository<ContatoCpf, Integer> {

    List<ContatoCpf> findByFirstNameContainingIgnoreCase(String nome);
    List<ContatoCpf> findByCpfStartingWith(String cpf);
}
