package com.example.minhaappspring.controllers;

import com.example.minhaappspring.models.Cliente;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

        // Método que retorna uma lista de clientes que começam com o prefixo passado como parâmetro
        List<Cliente> findByNomeStartingWith(String prefixo);

        // Método que retorna uma lista de todos os clientes
        List<Cliente> findAll();

        // Método que retorna um cliente pelo seu ID
        Optional<Cliente> findById(Long id);

        // Método que salva um cliente
        Cliente save(Cliente cliente);

        // Método que exclui um cliente pelo seu ID
        void deleteById(Long id);

        // Método que retorna a quantidade de clientes com o mesmo email
        @Query("Select count (c) from Cliente c where c.email = :email and c.id <> :id")
        int countClientesWithSameEmail(@Param("email") String email, @Param("id") Long id);

    }
