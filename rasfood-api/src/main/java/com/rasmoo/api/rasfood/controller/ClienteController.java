package com.rasmoo.api.rasfood.controller;

import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasmoo.api.rasfood.entity.Cliente;
import com.rasmoo.api.rasfood.entity.ClienteId;
import com.rasmoo.api.rasfood.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/cliente")
@RestController
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Cliente>> consultarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteRepository.findAll());
    }

    @GetMapping("/{cpf}/{email}")
    public ResponseEntity<Cliente> consultarPorEmailCpf(@PathVariable("cpf") String cpf,
                                                        @PathVariable("email") String email) {

        ClienteId clienteId = new ClienteId(cpf, email);
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        return cliente.map(value -> ResponseEntity.status(HttpStatus.OK).body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable("id") final String id,
                                             @RequestBody final Cliente cliente) throws JsonMappingException {

        Optional<Cliente> clienteEncontrado = clienteRepository.findByEmailOrCpf(id);
        if(clienteEncontrado.isPresent()) {
            objectMapper.updateValue(clienteEncontrado.get(), cliente);
            return ResponseEntity.status(HttpStatus.OK).body(this.clienteRepository.save(clienteEncontrado.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
