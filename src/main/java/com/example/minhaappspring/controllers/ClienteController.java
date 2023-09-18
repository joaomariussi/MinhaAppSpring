package com.example.minhaappspring.controllers;

import com.example.minhaappspring.models.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    // Injeção de dependência
    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    // Manda a requisição para o método listarClientesComNomeIniciadoComA da classe ClienteService
    @GetMapping("/clientes-iniciados-com-a")
    public List<Cliente> listarClientesComNomeIniciadoComA() {
        return clienteService.listarClientesComNomeIniciadoComA();
    }

    // Manda a requisição para o método listarTodosClientes da classe ClienteService
    @GetMapping("/clientes")
    public List<Cliente> listarClientes() {
        List<Cliente> listCliente = new ArrayList<>();
        Iterable<Cliente> clientes = clienteService.listarTodosClientes();
        clientes.forEach(listCliente::add);
        return listCliente;
    }

    // Manda a requisição para o método pesquisarPorId da classe ClienteService
    @GetMapping("/pesquisar-por-id")
    public ResponseEntity<?> pesquisarPorId(@RequestParam Long id) {
        Optional<Cliente> clienteOptional = clienteService.pesquisarPorId(id);

        if (clienteOptional.isPresent()) {
            // Cliente encontrado, retornar os dados do cliente
            Cliente cliente = clienteOptional.get();
            return ResponseEntity.ok(cliente);
        } else {
            // Cliente não encontrado, retornar uma resposta de erro
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente com ID " + id + " não foi encontrado.");
        }
    }

    // Manda a requisição para o método salvarCliente da classe ClienteService
    @PostMapping("/salvarCliente")
    public ResponseEntity<String> salvarCliente(@ModelAttribute Cliente cliente) {

        // Verifique se o e-mail já existe em outro cliente
        Cliente clienteExistente = clienteService.encontrarClientePorEmail(cliente.getEmail());

        if (clienteExistente != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um cliente com o e-mail " + cliente.getEmail() + "!");
        }

        // Se o e-mail não existir em outro cliente, salve o cliente
        clienteService.salvarCliente(cliente);
        return ResponseEntity.ok("Cliente com ID " + cliente.getId() + " salvo com sucesso!");
    }

    // Manda a requisição para o método excluirCliente da classe ClienteService
    @PostMapping("/excluirCliente")
    public ResponseEntity<String> excluirCliente(Long id) {
        boolean clienteExcluido = clienteService.excluirCliente(id);
        if (clienteExcluido) {
            return ResponseEntity.ok("Cliente com ID " + id + " excluído com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente com ID " + id + " não encontrado!");
        }
    }

    // Faz a edição do cliete, validando o e-mail e depois chama a requisicao para o metodo salvarCliente da classe ClienteService
    @PostMapping("/editarCliente")
    public ResponseEntity<String> editarCliente(Long id, String nome, String email, LocalDate dataNascimento) {

        // Verifica se já existe um cliente com o e-mail passado como parâmetro
        int countClientesComMesmoEmail = clienteRepository.countClientesWithSameEmail(email, id);

        if (countClientesComMesmoEmail > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um cliente com o e-mail " + email + "!");
        }

        Optional<Cliente> clienteOptional = clienteService.pesquisarPorId(id);

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setDataNascimento(dataNascimento);
            clienteService.salvarCliente(cliente);
            return ResponseEntity.ok("Cliente com ID " + cliente.getId() + " editado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente com ID " + id + " não encontrado!");
        }
    }
}
