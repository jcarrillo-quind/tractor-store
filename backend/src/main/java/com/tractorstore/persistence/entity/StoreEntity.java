package com.tractorstore.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stores")
public class StoreEntity {

  @Id
  @Column(length = 64)
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, length = 128)
  private String city;

  @Column(name = "address_line", nullable = false)
  private String addressLine;

  @Column(name = "image_url", nullable = false, length = 512)
  private String imageUrl;

  protected StoreEntity() {}

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public String getAddressLine() {
    return addressLine;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
