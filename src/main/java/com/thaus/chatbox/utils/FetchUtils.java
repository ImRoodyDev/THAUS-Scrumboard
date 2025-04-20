package com.thaus.chatbox.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class FetchUtils {
	private static HttpURLConnection configureConnection(HttpURLConnection connection) {
		connection.setConnectTimeout(4000);
		connection.setReadTimeout(4000);
		return connection;
	}

	public static JSONObject post(String url, Map<String, String> data, String accessToken, String refreshToken) throws Exception {
		URL endpoint = new URL(url);
		HttpURLConnection connection = configureConnection((HttpURLConnection) endpoint.openConnection());
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");

		if (accessToken != null && !accessToken.isEmpty()) {
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
		}

		if (refreshToken != null && !refreshToken.isEmpty()) {
			connection.setRequestProperty("Cookie", "refreshToken=" + refreshToken);
		}

		connection.setDoOutput(true);
		String jsonPayload = mapToJson(data);
		try (OutputStream os = connection.getOutputStream()) {
			os.write(jsonPayload.getBytes());
			os.flush();
		}

		int statusCode = connection.getResponseCode();
		String responseBody = readResponse(connection);
		
		JSONObject jsonResponse = new JSONObject(responseBody);
		jsonResponse.put("statusCode", statusCode);

		String newRefreshToken = connection.getHeaderField("Set-Cookie");
		if (newRefreshToken != null && newRefreshToken.startsWith("refreshToken=")) {
			jsonResponse.put("refreshToken", newRefreshToken.split(";")[0].substring("refreshToken=".length()));
		}

		return jsonResponse;
	}

	public static JSONObject get(String url, String accessToken, String refreshToken) throws Exception {
		URL endpoint = new URL(url);
		HttpURLConnection connection = configureConnection((HttpURLConnection) endpoint.openConnection());
		connection.setRequestMethod("GET");

		if (accessToken != null && !accessToken.isEmpty()) {
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
		}

		if (refreshToken != null && !refreshToken.isEmpty()) {
			connection.setRequestProperty("Cookie", "refreshToken=" + refreshToken);
		}

		int statusCode = connection.getResponseCode();
		String responseBody = readResponse(connection);

		JSONObject jsonResponse = new JSONObject(responseBody);
		jsonResponse.put("statusCode", statusCode);

		return jsonResponse;
	}

	private static String mapToJson(Map<String, String> data) {
		return new JSONObject(data).toString();
	}

	private static String readResponse(HttpURLConnection connection) throws Exception {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream()))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			return response.toString();
		}
	}
}