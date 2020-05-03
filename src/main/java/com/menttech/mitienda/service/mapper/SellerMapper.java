package com.menttech.mitienda.service.mapper;


import com.menttech.mitienda.domain.*;
import com.menttech.mitienda.service.dto.SellerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Seller} and its DTO {@link SellerDTO}.
 */
@Mapper(componentModel = "spring", uses = {StockMapper.class})
public interface SellerMapper extends EntityMapper<SellerDTO, Seller> {

    @Mapping(source = "user.id", target = "userId")
    SellerDTO toDto(Seller seller);

    @Mapping(source = "userId", target = "user")
    Seller toEntity(SellerDTO sellerDTO);

    default Seller fromId(Long id) {
        if (id == null) {
            return null;
        }
        Seller seller = new Seller();
        seller.setId(id);
        return seller;
    }
}
