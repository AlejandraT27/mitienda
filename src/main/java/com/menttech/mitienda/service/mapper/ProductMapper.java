package com.menttech.mitienda.service.mapper;


import com.menttech.mitienda.domain.*;
import com.menttech.mitienda.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {StockMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(source = "user.id", target = "userId")
    ProductDTO toDto(Product product);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "productCategoys", ignore = true)
    @Mapping(target = "removeProductCategoy", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "removeProductCategory", ignore = true)
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
