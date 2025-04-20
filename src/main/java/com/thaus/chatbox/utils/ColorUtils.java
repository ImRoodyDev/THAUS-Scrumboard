package com.thaus.chatbox.utils;

import java.util.Random;

public class ColorUtils {

	public static String getRandomColorExceptBlack() {
		Random random = new Random();
		int red, green, blue;
		// Keep generating until we get a color that's not too dark (not black)
		do {
			red = random.nextInt(256);
			green = random.nextInt(256);
			blue = random.nextInt(256);
		} while (red < 30 && green < 30 && blue < 30); // Skip very dark colors

		return String.format("#%02x%02x%02x;", red, green, blue);
	}
}
