package com.tractorstore.persistence.repository;

import com.tractorstore.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, String> {}
