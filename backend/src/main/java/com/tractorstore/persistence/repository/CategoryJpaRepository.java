package com.tractorstore.persistence.repository;

import com.tractorstore.persistence.entity.CategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {

  List<CategoryEntity> findAllByOrderByIdAsc();
}
