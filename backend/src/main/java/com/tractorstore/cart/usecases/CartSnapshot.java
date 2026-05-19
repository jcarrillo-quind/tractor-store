package com.tractorstore.cart.usecases;

import com.tractorstore.cart.entities.LineItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record CartSnapshot(List<LineItem> lines, int itemCount, BigDecimal subtotal) {
  public CartSnapshot {
    lines = lines == null ? List.of() : List.copyOf(lines);
    Objects.requireNonNull(subtotal, "subtotal");
  }
}
