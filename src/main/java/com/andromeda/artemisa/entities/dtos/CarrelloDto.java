package com.andromeda.artemisa.entities.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.andromeda.artemisa.entities.dtos.ItemInt.ItemDto;

public class CarrelloDto {

    List<ItemDto> itemListDto;
    BigDecimal prezzoTotal;

    public CarrelloDto() {
    }

    public CarrelloDto(BigDecimal prezzoTotal, List<ItemDto> itemListDto) {
        this.prezzoTotal = prezzoTotal;
        this.itemListDto = itemListDto;
    }

    public List<ItemDto> getItemListDto() {
        return itemListDto;
    }

    public BigDecimal getPrezzoTotal() {
        return prezzoTotal;
    }

    public static class Builder {

        private List<ItemDto> itemListDto;
        private BigDecimal prezzoTotal;

        public Builder() {

        }

        public Builder itemListDto(List<ItemDto> itemListDto) {
            this.itemListDto = itemListDto;
            return this;
        }

        public Builder prezzoTotal(BigDecimal prezzoTotal){
            this.prezzoTotal=prezzoTotal;
            return this;
        }
        public CarrelloDto build(){
            return new CarrelloDto(prezzoTotal, itemListDto);
        }

    }
}
