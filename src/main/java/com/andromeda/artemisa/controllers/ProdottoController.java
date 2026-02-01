package com.andromeda.artemisa.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.entities.dtos.ProdottoDto;
import com.andromeda.artemisa.services.ProdottoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

    private final ProdottoService prodottoService;

    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @GetMapping("/list")
    public PagedModel<EntityModel<ProdottoDto>> cercaProdotti(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) BigDecimal prezzoMin,
            @RequestParam(required = false) BigDecimal prezzoMax,
            Pageable pageable,
            PagedResourcesAssembler<ProdottoDto> assembler
    ) {

        Page<Prodotto> pageProdotto = prodottoService.reperireProdottiPageable(nome, categoria, prezzoMin, prezzoMax, pageable);
        Page<ProdottoDto> pageProdottoDto = pageProdotto.map(
                prodotto -> new ProdottoDto.ProdottoBuilder()
                        .nome(prodotto.getNome())
                        .prezzo(prodotto.getPrezzo())
                        .quantita(prodotto.getQuantita())
                        .build()
        );
        return assembler.toModel(pageProdottoDto);
    }

    @PostMapping("/add")
    public ResponseEntity<String> aggProdotto(@RequestBody ProdottoDto prodottoDto) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> rmProdotto(@RequestBody ProdottoDto prodottoDto) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @PutMapping("/modify")
    public ResponseEntity<String> mdProdotto(@RequestBody Integer quantita) {
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    //TO-DO ADD EXCEL PRODOTTI
    @PostMapping(value = "/uploadProdotti", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> aggProdotti(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Per favore, seleziona un file");
        }
        this.prodottoService.addListProdotti(file);
        return ResponseEntity.ok("Il file è stato caricato con successo!");
    }

    //Consente di aggiungere una lista di categorie
    @PostMapping(value = "/uploadCategorie", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> aggCategorie(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> risposta = new HashMap<>();
        if (file.isEmpty()) {
            risposta.put("message", "Per favore, seleziona un file");
        } else {
            risposta = this.prodottoService.addListCategorie(file);
        }

        return ResponseEntity.ok(risposta);
    }
}
