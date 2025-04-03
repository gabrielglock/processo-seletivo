package com.desafio.agenda.Repository;

import com.desafio.agenda.Model.ContatoCnpj;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatoCnpjRepository extends JpaRepository<ContatoCnpj, Integer> {
    List<ContatoCnpj> findByCnpjNameContainingIgnoreCase(String nome);
}
