package com.andromeda.artemisa.services.inter;

import java.util.List;

import com.andromeda.artemisa.entities.TempData;

public interface EcommerceInter {

    public List<TempData> recuperoProdottiSessione(String session);

    public void aggProdottoCarrello(String session);

    public void canProdottoCarrello(Long id);

    public void generateFattura();

    public void metodiPagamento();

    public void saveOrdine();

    public void puliziaTempCarrello(String session);
    
}
