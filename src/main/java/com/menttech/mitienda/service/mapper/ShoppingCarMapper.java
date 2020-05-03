package com.menttech.mitienda.service.mapper;


import com.menttech.mitienda.domain.*;
import com.menttech.mitienda.service.dto.ShoppingCarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShoppingCar} and its DTO {@link ShoppingCarDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface ShoppingCarMapper extends EntityMapper<ShoppingCarDTO, ShoppingCar> {

    @Mapping(source = "user.id", target = "userId")
    ShoppingCarDTO toDto(ShoppingCar shoppingCar);

    @Mapping(source = "userId", target = "user")
    ShoppingCar toEntity(ShoppingCarDTO shoppingCarDTO);

    default ShoppingCar fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.setId(id);
        return shoppingCar;
    }
}
