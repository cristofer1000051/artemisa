package com.andromeda.artemisa.services;

import java.util.List;

import com.andromeda.artemisa.entities.dtos.ProdottoDto;

import com.andromeda.artemisa.services.inter.EcommerceInter;

public class EcommerceService implements EcommerceInter {

    @Override
    public List<ProdottoDto> recuperoCarrelloSessione(String email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void aggProdottoCarrello(String session) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void canProdottoCarrello(Long id) {

    }

    @Override
    public void generateFattura() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void metodiPagamento() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveOrdine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void puliziaTempCarrello(String session) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
