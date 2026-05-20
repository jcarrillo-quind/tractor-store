package com.tractorstore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tractor-store.cart")
public class CartSessionProperties {

  private String cookieName = "TRACTOR_CART_SESSION";
  private int maxAgeSeconds = 604800;
  private String path = "/";
  private boolean httpOnly = true;
  private boolean secure = false;
  private String sameSite = "Lax";

  public String getCookieName() {
    return cookieName;
  }

  public void setCookieName(String cookieName) {
    this.cookieName = cookieName;
  }

  public int getMaxAgeSeconds() {
    return maxAgeSeconds;
  }

  public void setMaxAgeSeconds(int maxAgeSeconds) {
    this.maxAgeSeconds = maxAgeSeconds;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean isHttpOnly() {
    return httpOnly;
  }

  public void setHttpOnly(boolean httpOnly) {
    this.httpOnly = httpOnly;
  }

  public boolean isSecure() {
    return secure;
  }

  public void setSecure(boolean secure) {
    this.secure = secure;
  }

  public String getSameSite() {
    return sameSite;
  }

  public void setSameSite(String sameSite) {
    this.sameSite = sameSite;
  }
}
