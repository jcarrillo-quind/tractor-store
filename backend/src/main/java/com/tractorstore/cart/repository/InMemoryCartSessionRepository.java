package com.tractorstore.cart.repository;

import com.tractorstore.cart.entities.Cart;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** Carritos por id de sesión (cookie) en memoria. */
@Repository
public class InMemoryCartSessionRepository {

  private final ConcurrentHashMap<String, Cart> sessions = new ConcurrentHashMap<>();

  public Cart getOrCreate(String sessionId) {
    return sessions.computeIfAbsent(sessionId, id -> new Cart());
  }

  public void clear(String sessionId) {
    sessions.remove(sessionId);
  }
}
