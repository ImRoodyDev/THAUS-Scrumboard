package com.thaus.chatbox.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
	private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy h:mma");

	public static Date parseAndFormatDate(String dateString) {
		try {
			if (dateString == null || dateString.isEmpty()) {
				return null;
			}

			// Parse the input string
			LocalDateTime localDateTime = LocalDateTime.parse(dateString, inputFormatter);

			// Convert to Date object
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatDate(Date date) {
		try {
			if (date == null) {
				return null;
			}
			
			// Convert Date to LocalDateTime
			LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			// Format the date
			return localDateTime.format(outputFormatter).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
