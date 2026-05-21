package com.tractorstore.persistence.repository;

import com.tractorstore.persistence.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {

  @Query(
      """
      SELECT DISTINCT p FROM ProductEntity p
      JOIN FETCH p.category
      JOIN FETCH p.variants
      ORDER BY p.name
      """)
  List<ProductEntity> findAllCatalog();

  @Query(
      """
      SELECT p FROM ProductEntity p
      JOIN FETCH p.category
      JOIN FETCH p.variants
      WHERE p.id = :id
      """)
  Optional<ProductEntity> findByIdWithDetails(String id);

  @Query(
      """
      SELECT DISTINCT p FROM ProductEntity p
      JOIN FETCH p.category
      JOIN FETCH p.variants
      WHERE LOWER(p.category.id) = LOWER(:categoryId)
      ORDER BY p.name
      """)
  List<ProductEntity> findByCategoryId(String categoryId);
}
