package com.tractorstore.catalog.entities;

import java.util.Objects;

/**
 * Color RGB del dominio de catálogo (recomendaciones por distancia euclídea).
 */
public record RgbColor(int r, int g, int b) {

	public double distanceTo(RgbColor other) {
		Objects.requireNonNull(other, "other");
		int dr = r - other.r;
		int dg = g - other.g;
		int db = b - other.b;
		return Math.sqrt(dr * dr + dg * dg + db * db);
	}

	public static RgbColor average(Iterable<RgbColor> samples) {
		long sr = 0;
		long sg = 0;
		long sb = 0;
		int n = 0;
		for (RgbColor s : samples) {
			sr += s.r;
			sg += s.g;
			sb += s.b;
			n++;
		}
		if (n == 0) {
			return new RgbColor(0, 0, 0);
		}
		return new RgbColor((int) Math.round((double) sr / n), (int) Math.round((double) sg / n),
				(int) Math.round((double) sb / n));
	}

	public static RgbColor fromHex(String hex) {
		if (hex == null || hex.isBlank()) {
			throw new IllegalArgumentException("colorHex vacío");
		}
		String h = hex.strip();
		if (h.startsWith("#")) {
			h = h.substring(1);
		}
		if (h.length() == 3) {
			int rv = fromHexDigit(h.charAt(0)) * 16 + fromHexDigit(h.charAt(0));
			int gv = fromHexDigit(h.charAt(1)) * 16 + fromHexDigit(h.charAt(1));
			int bv = fromHexDigit(h.charAt(2)) * 16 + fromHexDigit(h.charAt(2));
			return new RgbColor(rv, gv, bv);
		}
		if (h.length() != 6) {
			throw new IllegalArgumentException("colorHex no soportado: " + hex);
		}
		int rv = Integer.parseInt(h.substring(0, 2), 16);
		int gv = Integer.parseInt(h.substring(2, 4), 16);
		int bv = Integer.parseInt(h.substring(4, 6), 16);
		return new RgbColor(rv, gv, bv);
	}

	private static int fromHexDigit(char c) {
		return Character.digit(c, 16);
	}
}
