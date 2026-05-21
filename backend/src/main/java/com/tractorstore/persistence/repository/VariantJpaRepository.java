package com.tractorstore.persistence.repository;

import com.tractorstore.persistence.entity.VariantEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VariantJpaRepository extends JpaRepository<VariantEntity, String> {

  @Query(
      """
      SELECT v FROM VariantEntity v
      JOIN FETCH v.product p
      JOIN FETCH p.category
      WHERE v.sku = :sku
      """)
  Optional<VariantEntity> findBySkuWithProduct(String sku);
}
