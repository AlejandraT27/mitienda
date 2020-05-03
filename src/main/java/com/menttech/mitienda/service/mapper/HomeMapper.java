package com.menttech.mitienda.service.mapper;


import com.menttech.mitienda.domain.*;
import com.menttech.mitienda.service.dto.HomeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Home} and its DTO {@link HomeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HomeMapper extends EntityMapper<HomeDTO, Home> {



    default Home fromId(Long id) {
        if (id == null) {
            return null;
        }
        Home home = new Home();
        home.setId(id);
        return home;
    }
}
