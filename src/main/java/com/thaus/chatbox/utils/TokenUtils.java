package com.thaus.chatbox.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TokenUtils {
	private static final String TOKEN_FILE = "tokens.txt";

	public static void saveTokens(String accessToken, String refreshToken) throws IOException {
		String tokens = "accessToken=" + accessToken + "\nrefreshToken=" + refreshToken;
		Files.writeString(Path.of(TOKEN_FILE), tokens);
	}

	/**
	 * Get tokens
	 * 1. AccessToken
	 * 2. RefreshToken
	 */
	public static String[] loadTokens() {
		try {
			if (!Files.exists(Path.of(TOKEN_FILE))) {
				return null;
			}
			String content = Files.readString(Path.of(TOKEN_FILE));
			String accessToken = content.split("\n")[0].split("=")[1];
			String refreshToken = content.split("\n")[1].split("=")[1];
			return new String[]{accessToken, refreshToken};
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void clearTokens() {
		try {
			Files.deleteIfExists(Path.of(TOKEN_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
