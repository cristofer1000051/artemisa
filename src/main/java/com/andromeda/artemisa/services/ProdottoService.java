package com.andromeda.artemisa.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.andromeda.artemisa.entities.Categoria;
import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.repositories.CategoriaRepository;
import com.andromeda.artemisa.repositories.ProdottoRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdottoService(ProdottoRepository prodottoRepository, CategoriaRepository categoriaRepository) {
        this.prodottoRepository = prodottoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Page<Prodotto> reperireProdottiPageable(String nome, String categoria, BigDecimal prezzoMin, BigDecimal prezzoMax, Pageable pageable) {
        Specification<Prodotto> filtro = (root, query, cb) -> {
            /**
             * cb operatori logici matematici root la tabella query, la query
             * che stiamo per inviare
             */
            List<Predicate> predicates = new ArrayList<>();
            //1. Controllo filtro nome prodotto
            if (nome != null && !nome.isEmpty()) {
                predicates.add(cb.like(root.get("nome"), "%" + nome + "%"));
            }
            //2. Controllo filtro categoria (con join)
            if (categoria != null && !categoria.isEmpty()) {
                Join<Prodotto, Categoria> categoriaJoin = root.join("categorie");
                predicates.add(cb.like(categoriaJoin.get("nome"), "%" + categoria + "%"));
            }
            if (prezzoMin != null && prezzoMax != null) {
                if (prezzoMin.compareTo(BigDecimal.ZERO) >= 0 && prezzoMax.compareTo(prezzoMin) >= 0) {
                    predicates.add(cb.between(root.get("prezzo"), prezzoMin, prezzoMax));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Prodotto> pageProdotto = prodottoRepository.findAll(filtro, pageable);
        return pageProdotto;
    }

    @Transactional
    @SuppressWarnings("ConvertToTryWithResources")
    public void addListProdotti(MultipartFile file) throws IOException {

        //1. Aprire il file excel usando Apache POI
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //2. Leggi il primo foglio
        Sheet sheet = workbook.getSheetAt(0);
        List<Prodotto> prodotti = new ArrayList<>();
        for (Row row : sheet) {
            //Saltiamo la parima riga delle intestazioni
            if (row.getRowNum() == 0) {
                continue;
            }
            String nome = row.getCell(0).getStringCellValue();
            int quantita = (int) row.getCell(1).getNumericCellValue();
            BigDecimal prezzo = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
            //Leggi la stringa 1,2,3 ...
            String categorieRaw = row.getCell(3).getStringCellValue();
            String[] array = categorieRaw.split(",");
            List<Categoria> categorie = Arrays.stream(array)
                    .map((s) -> {
                        Long id = Long.valueOf(s.trim());
                        Categoria cat = new Categoria.Builder()
                                .id(id)
                                .build();

                        return cat;
                    })
                    .distinct()
                    .collect(Collectors.toList());

            Prodotto prodotto = new Prodotto.Builder()
                    .nome(nome)
                    .prezzo(prezzo)
                    .quantita(quantita)
                    .categorie(categorie)
                    .build();

            prodotti.add(prodotto);
        }
        workbook.close();
        prodottoRepository.saveAll(prodotti);
    }

    @Transactional
    @SuppressWarnings("ConvertToTryWithResources")
    public Map<String, Object> addListCategorie(MultipartFile file) throws IOException {
        //1. Aprire il file excel usando Apache POI
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //2. Leggi il primo foglio
        Sheet sheet = workbook.getSheetAt(0);
        List<Categoria> categorie = new ArrayList<>();
        List<String> nomi = new ArrayList<>();
        for (Row row : sheet) {
            //Saltiamo la parima riga delle intestazioni
            if (row.getRowNum() == 0) {
                continue;
            }
            String nome = row.getCell(0).getStringCellValue().trim();
            Categoria cat = new Categoria.Builder().nome(nome).build();
            categorie.add(cat);
            nomi.add(nome);
        }
        List<String> nomiRipetuti = categoriaRepository.findNomeEsistenti(nomi);

        Map<String, Object> risposta = new HashMap<>();
        if (!nomiRipetuti.isEmpty()) {
            risposta.put("stato", "parziale");
            risposta.put("messaggio", "File caricato, ma alcune categorie esistevano già.");
            risposta.put("nomi_duplicati", nomiRipetuti); // <--- Ecco la lista che vedrai a video
            risposta.put("numero_salvati", nomiRipetuti.size());
        } else {
            risposta.put("stato", "successo");
            risposta.put("messaggio", "Tutte le categorie sono state salvate!");
        }
        categorie.removeIf(c -> nomiRipetuti.contains(c.getnome()));
        workbook.close();
        if (!categorie.isEmpty()) {
            categoriaRepository.saveAll(categorie);
        }
        return risposta;
    }

}
