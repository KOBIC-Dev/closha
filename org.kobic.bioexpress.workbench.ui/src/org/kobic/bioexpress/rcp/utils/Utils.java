package org.kobic.bioexpress.rcp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.kobic.bioexpress.rcp.constant.Constants;

public class Utils {

	protected static Utils instance = null;

	public final static Utils getInstance() {

		if (instance == null) {
			instance = new Utils();
		} else {
			return instance;
		}

		return instance;
	}

	public String readFileToString(String path) {

		InputStream stream = this.getClass().getResourceAsStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		String str = null;
		StringBuffer buffer = new StringBuffer();

		try {
			while ((str = br.readLine()) != null) {
				buffer.append(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public List<String> readLines(String path) {

		List<String> line = new ArrayList<String>();

		InputStream stream = this.getClass().getResourceAsStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		String str = null;

		try {
			while ((str = br.readLine()) != null) {
				line.add(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return line;
	}

	public String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "KMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public String getNumberFormat(float value) {

		DecimalFormat format = new DecimalFormat(".#");

		if (value == 0) {
			return String.valueOf(value);
		} else {
			return format.format(value);
		}
	}

	public String getDate() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt).toString();
	}

	public String getDateTime(long timeInMillis) {

		Date currentDate = new Date(timeInMillis);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(currentDate);
	}

	public String getDateTime() {

		long timeInMillis = System.currentTimeMillis();

		Date currentDate = new Date(timeInMillis);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(currentDate);
	}
	
	public String getCurruntTime() {

		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

		return format.format(now);
	}

	public String getNewRawID() {

		String rawID = UUID.randomUUID().toString();

		return rawID;
	}
	
	public String getKeywordFormat(String keywords) {
		
		List<String> keywordList = new ArrayList<String>();
		
		String keywordArray[] = keywords.split(",");
		
		for (String keyword : keywordArray) {
			if(keyword.length() != 0) {
				
				if(!keyword.startsWith("#")) {
					keyword = String.format(Constants.KEYWORD_FORMAT, keyword.trim());
				}
				
				if(!keywordList.contains(keyword)) {
					keywordList.add(keyword);
				}
			}

		}
		
		keywords = String.join(",", keywordList);
		
		return keywords;
	}
}
