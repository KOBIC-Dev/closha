package org.kobic.bioexpress.rcp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

	private static ValidationUtils instance;

	public static final ValidationUtils getInstance() {

		if (instance == null) {
			instance = new ValidationUtils();
		} else {
			return instance;
		}

		return instance;
	}

	public boolean isNameVaildation(String name) {

		Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_.]*$");
		Matcher matcher = pattern.matcher(name);

		if (!matcher.find()) {

			return false;

		} else {

			return true;
		}
	}

	public boolean isFileNameValidation(String name) {

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9-_.]*$");
		Matcher matcher = pattern.matcher(name);

		if (!matcher.find()) {

			return false;

		} else {

			return true;
		}

	}
}
