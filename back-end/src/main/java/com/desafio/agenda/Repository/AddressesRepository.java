package com.desafio.agenda.Repository;

import com.desafio.agenda.Model.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressesRepository extends JpaRepository<Addresses, Integer> {
}
