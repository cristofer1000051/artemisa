package com.andromeda.artemisa.security.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.andromeda.artemisa.security.dtos.TokenDto;
import com.andromeda.artemisa.security.entities.Token;



@Mapper
public interface TokenMapper {
    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);
    TokenDto toDto(Token token);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "revoked", ignore = true)
    @Mapping(target = "utente", ignore = true)
    Token toEntity(TokenDto tokenDto);
}
