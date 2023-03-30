package com.rasmoo.api.rasfood.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasmoo.api.rasfood.dto.CardapioDTO;
import com.rasmoo.api.rasfood.entity.Cardapio;
import com.rasmoo.api.rasfood.repository.CardapioRepository;
import com.rasmoo.api.rasfood.repository.projection.CardapioProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/cardapio")
@RestController
public class CardapioController {

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /*@GetMapping
    public ResponseEntity<List<Cardapio>> consultarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAll());
    }*/

    @GetMapping
    public ResponseEntity<Page<Cardapio>> consultarTodosPag(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Pageable peageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAll(peageable));
    }

    @GetMapping("/nome/{nome}/disponivel")
    public ResponseEntity<List<CardapioDTO>> consultarPorNome(@PathVariable("nome") String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAllByNome(nome));
    }

    @GetMapping("/categoria/{categoriaId}/disponivel")
    public ResponseEntity<List<Cardapio>> consultarPorCategoria(@PathVariable("categoriaId") Integer categoriaId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAllByCategoria(categoriaId));
    }

    @GetMapping("/categoria/{categoriaId}/disponivel/projection")
    public ResponseEntity<List<CardapioProjection>> consultarPorCategoriaIdProj(@PathVariable("categoriaId") Integer categoriaId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardapioRepository.findAllByCategoriaIdProj(categoriaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> consultarPorId(@PathVariable("id") final Integer id) {
        Optional<Cardapio> cardapioEncontrado = cardapioRepository.findById(id);
        if (cardapioEncontrado.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(cardapioEncontrado.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirPorId(@PathVariable("id") final Integer id) {
        Optional<Cardapio> cardapioEncontrado = cardapioRepository.findById(id);
        if (cardapioEncontrado.isPresent()) {
            cardapioRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Elemento não encontrado");
    }

    @PostMapping
    public ResponseEntity<Cardapio> criar(@RequestBody final Cardapio cardapio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cardapioRepository.save(cardapio));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cardapio> atualizar(@PathVariable("id") final Integer id, @RequestBody final Cardapio cardapio) throws JsonMappingException {
        Optional<Cardapio> cardapioEncontrado = this.cardapioRepository.findById(id);
        if (cardapioEncontrado.isPresent()) {
            objectMapper.updateValue(cardapioEncontrado.get(), cardapio);
            return ResponseEntity.status(HttpStatus.OK).body(this.cardapioRepository.save(cardapioEncontrado.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PatchMapping(path = "/{id}/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Cardapio> salvarImg(@PathVariable("id") final Integer id,
                                              @RequestParam MultipartFile file) throws IOException {
        Optional<Cardapio> cardapioEncontrado = this.cardapioRepository.findById(id);
        if (cardapioEncontrado.isPresent()) {
            Cardapio cardapio = cardapioEncontrado.get();
            cardapio.setImg(file.getBytes());
            objectMapper.updateValue(cardapioEncontrado.get(), cardapio);
            return ResponseEntity.status(HttpStatus.OK).body(this.cardapioRepository.save(cardapioEncontrado.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //
    }
}
