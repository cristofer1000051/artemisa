package com.andromeda.artemisa.security.utils.interfaces;

import org.mapstruct.Mapper;

import com.andromeda.artemisa.security.dtos.TokenDto;
import com.andromeda.artemisa.security.entities.Token;



@Mapper(componentModel = "spring")
public interface TokenMapper {

    TokenDto toDto(Token token);
}
