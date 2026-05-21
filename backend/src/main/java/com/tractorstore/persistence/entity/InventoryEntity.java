package com.tractorstore.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class InventoryEntity {

  @Id
  @Column(length = 64)
  private String sku;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sku")
  private VariantEntity variant;

  @Column(name = "available_units", nullable = false)
  private int availableUnits;

  protected InventoryEntity() {}

  public String getSku() {
    return sku;
  }

  public int getAvailableUnits() {
    return availableUnits;
  }
}
