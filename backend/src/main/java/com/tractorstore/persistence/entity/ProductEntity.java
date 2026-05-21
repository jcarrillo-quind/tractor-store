package com.tractorstore.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {

  @Id
  @Column(length = 64)
  private String id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "product_highlights", joinColumns = @JoinColumn(name = "product_id"))
  @OrderColumn(name = "sort_order")
  @Column(name = "highlight", nullable = false)
  private List<String> highlights = new ArrayList<>();

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<VariantEntity> variants = new ArrayList<>();

  protected ProductEntity() {}

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public List<String> getHighlights() {
    return List.copyOf(highlights);
  }

  public List<VariantEntity> getVariants() {
    return List.copyOf(variants);
  }
}
