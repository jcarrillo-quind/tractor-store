package com.tractorstore.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity {

  @Id
  @Column(length = 64)
  private String id;

  @Column(nullable = false, length = 128)
  private String label;

  @Column(nullable = false, length = 512)
  private String tagline;

  @Column(name = "image_url", nullable = false, length = 512)
  private String imageUrl;

  protected CategoryEntity() {}

  public CategoryEntity(String id, String label, String tagline, String imageUrl) {
    this.id = id;
    this.label = label;
    this.tagline = tagline;
    this.imageUrl = imageUrl;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public String getTagline() {
    return tagline;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
