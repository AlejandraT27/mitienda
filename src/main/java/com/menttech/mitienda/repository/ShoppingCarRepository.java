package com.menttech.mitienda.repository;

import com.menttech.mitienda.domain.ShoppingCar;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ShoppingCar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingCarRepository extends JpaRepository<ShoppingCar, Long>, JpaSpecificationExecutor<ShoppingCar> {
}
