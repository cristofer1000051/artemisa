package com.andromeda.artemisa.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.entities.TempData;
import com.andromeda.artemisa.entities.dtos.ProdottoDtoTemp;
import com.andromeda.artemisa.repositories.TempDataRepository;
import com.andromeda.artemisa.utils.Obj;

@Service
public class CarrelloService {

    private final TempDataRepository tempDataRepository;

    public CarrelloService(TempDataRepository tempDataRepository) {
        this.tempDataRepository = tempDataRepository;
    }

    //Ricordare che tutto dipendera del Id del prodDtoTemp
    @Transactional
    public void save(ProdottoDtoTemp prodDtoTemp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TempData tempData = null;
        String json;
        Obj obj = new Obj();

        if (prodDtoTemp.getId() == null) {
            json = obj.toJson(prodDtoTemp);
            tempData = new TempData.Builder().key("cart:" + authentication.getName() + ":" + prodDtoTemp.getId()).payload(json).build();
        }
        tempDataRepository.save(tempData);
    }

    @Transactional
    public void saveAll(List<ProdottoDtoTemp> prodDtoTemp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<TempData> prodListStr = prodDtoTemp.stream().map(p -> {
            Obj obj = new Obj();
            String prodString = obj.toJson(p);
            TempData tempData = new TempData.Builder().key("cart:" + authentication.getName() + ":" + p.getId()).payload(prodString).build();
            return tempData;
        }).collect(Collectors.toList());
        tempDataRepository.saveAll(prodListStr);
    }

    @Transactional
    public void deleteById(Long prodId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        tempDataRepository.deleteByKey("cart:" + authentication.getName() + ":" + prodId);
    }

    @Transactional
    public void deleteAll(List<Long> prodIds) {
        tempDataRepository.deleteByIdIn(prodIds);
    }

    public List<Prodotto> findAll() {
        return null;
    }
}
