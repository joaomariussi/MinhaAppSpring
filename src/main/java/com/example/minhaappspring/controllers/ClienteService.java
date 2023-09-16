package com.example.minhaappspring.controllers;

import com.example.minhaappspring.models.Cliente;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Injeção de dependência
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    // Manda a requisição para o método findByNomeStartingWith da classe ClienteRepository
    public List<Cliente> listarClientesComNomeIniciadoComA() {
        return clienteRepository.findByNomeStartingWith("A");
    }

    // Manda a requisição para o método findAll da classe ClienteRepository, assim lista todos os clietes
    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }

    // Manda a requisição para o método findById da classe ClienteRepository, recuperando apenas o ID do cliente
    public Optional<Cliente> pesquisarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Manda a requisição para o método save da classe ClienteRepository, salvando o cliente
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Manda a requisição para o método deleteById da classe ClienteRepository, excluindo o cliente
    public boolean excluirCliente(Long id) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if (clienteOptional.isPresent()) {
            clienteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }




}
