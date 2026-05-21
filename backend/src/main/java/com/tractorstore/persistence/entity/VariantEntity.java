package com.tractorstore.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "variants")
public class VariantEntity {

  @Id
  @Column(length = 64)
  private String sku;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  private ProductEntity product;

  @Column(nullable = false, length = 128)
  private String label;

  @Column(name = "color_hex", nullable = false, length = 16)
  private String colorHex;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  @Column(name = "image_url", nullable = false, length = 512)
  private String imageUrl;

  protected VariantEntity() {}

  public String getSku() {
    return sku;
  }

  public ProductEntity getProduct() {
    return product;
  }

  public String getLabel() {
    return label;
  }

  public String getColorHex() {
    return colorHex;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
