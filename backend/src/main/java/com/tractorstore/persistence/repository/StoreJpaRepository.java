package com.tractorstore.persistence.repository;

import com.tractorstore.persistence.entity.StoreEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, String> {

  List<StoreEntity> findAllByOrderByNameAsc();
}
