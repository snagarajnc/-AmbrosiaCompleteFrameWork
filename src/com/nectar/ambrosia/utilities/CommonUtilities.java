package com.nectar.ambrosia.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;

public class CommonUtilities {
	
	static Logger log = Logger.getLogger(CommonUtilities.class.getName());
	
	private static FileWriter file;
	
	public static boolean getBoolValue(String boolValue) {
		if(StringUtils.isEmpty(boolValue)) {
			return false;
		}else if(boolValue.equalsIgnoreCase("yes")) {
			return true;
		}else if(boolValue.equalsIgnoreCase("y")) {
			return true;
		}else if(boolValue.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	public static List<String> splitStringGetAsList(String str,String splitter){
		if(StringUtils.isNotEmpty(str) && StringUtils.isNotEmpty(splitter)) {
			return Arrays.asList(str.split(splitter));
		}
		return new ArrayList<>();
	}
	public static List<String> splitByCommaGetAsList(String str){
		if(StringUtils.isNotEmpty(str)) {
			return Arrays.asList(str.split(Consts.COMMA_SPLITTER));
		}
		return new ArrayList<>();
	}
	public static List<String> splitByPipeGetAsList(String str){
		if(StringUtils.isNotEmpty(str)) {
			return Arrays.asList(str.split(Consts.TESTS_SPLITTER));
		}
		return new ArrayList<>();
	}
	public static String getFormatedDateAndTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");  
		String strDate = dateFormat.format(date);  
		return strDate;
	}
	public static String getFormatedDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
		String strDate = dateFormat.format(date);  
		return strDate;
	}
	public static String getDateAndTimeString() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy_hh-mm");  
		String strDate = dateFormat.format(Calendar.getInstance().getTime());  
		return strDate;
	}
	public static String getDateAndTimeStringForFile() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM_hh-mm");  
		String strDate = dateFormat.format(Calendar.getInstance().getTime());  
		return strDate;
	}
	public static String getFormattedDateAndTimeDifference(Date start,Date end) {
		long diff = Math.abs(end.getTime() - start.getTime());
		Duration du = Duration.ofMillis(diff);
		long mins = du.toMinutes();
		long hrs = du.toHours();
		long days = du.toDays();
		long secs = du.toMillis() / 1000;
		String returnStr = "";
		
		if(days > 0) {
			returnStr = days + " days, ";
		}
		if(hrs > 0) {
			returnStr += hrs + " hours, ";
		}
		if(mins > 0) {
			returnStr += mins + " minuets, ";
		}
		if(secs > 0) {
			returnStr += secs + " seconds. ";
		}
		return returnStr;
	}
	
	public static String getTestDataPath(String dataSheetname) {
		return CurrentRunProperties.getTestDataPath() + dataSheetname + ".csv";
	}
	public static boolean createFolder(String folderPath) {
		return new File(folderPath).mkdirs();
	}
	public static void createFileWithContent(String filePath,String data) {
		try {
			file = new FileWriter(filePath);
			file.write(data);
			file.flush();
			file.close();
		} catch (IOException e) {
			log.error("Unable to write content in the file " + filePath);
		}
	}
	public static String getFileContent(String filePath) {
		String str = "";
		
		File file = new File(filePath);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			str = new String(data, "UTF-8");
		} catch (Exception e) {
			log.error("Unable to get content of the file " + filePath);
		}
		return str;
	}
	 public static String forHTML(String aText){
	     final StringBuilder result = new StringBuilder();
	     final StringCharacterIterator iterator = new StringCharacterIterator(aText);
	     char character =  iterator.current();
	     while (character != CharacterIterator.DONE ){
	       if (character == '<') {
	         result.append("&lt;");
	       }
	       else if (character == '>') {
	         result.append("&gt;");
	       }
	       else if (character == '&') {
	         result.append("&amp;");
	      }
	       else if (character == '\"') {
	         result.append("&quot;");
	       }
	       else if (character == '\t') {
	         addCharEntity(9, result);
	       }
	       else if (character == '!') {
	         addCharEntity(33, result);
	       }
	       else if (character == '#') {
	         addCharEntity(35, result);
	       }
	       else if (character == '$') {
	         addCharEntity(36, result);
	       }
	       else if (character == '%') {
	         addCharEntity(37, result);
	       }
	       else if (character == '\'') {
	         addCharEntity(39, result);
	       }
	       else if (character == '(') {
	         addCharEntity(40, result);
	       }
	       else if (character == ')') {
	         addCharEntity(41, result);
	       }
	       else if (character == '*') {
	         addCharEntity(42, result);
	       }
	       else if (character == '+') {
	         addCharEntity(43, result);
	       }
	       else if (character == ',') {
	         addCharEntity(44, result);
	       }
	       else if (character == '-') {
	         addCharEntity(45, result);
	       }
	       else if (character == '.') {
	         addCharEntity(46, result);
	       }
	       else if (character == '/') {
	         addCharEntity(47, result);
	       }
	       else if (character == ':') {
	         addCharEntity(58, result);
	       }
	       else if (character == ';') {
	         addCharEntity(59, result);
	       }
	       else if (character == '=') {
	         addCharEntity(61, result);
	       }
	       else if (character == '?') {
	         addCharEntity(63, result);
	       }
	       else if (character == '@') {
	         addCharEntity(64, result);
	       }
	       else if (character == '[') {
	         addCharEntity(91, result);
	       }
	       else if (character == '\\') {
	         addCharEntity(92, result);
	       }
	       else if (character == ']') {
	         addCharEntity(93, result);
	       }
	       else if (character == '^') {
	         addCharEntity(94, result);
	       }
	       else if (character == '_') {
	         addCharEntity(95, result);
	       }
	       else if (character == '`') {
	         addCharEntity(96, result);
	       }
	       else if (character == '{') {
	         addCharEntity(123, result);
	       }
	       else if (character == '|') {
	         addCharEntity(124, result);
	       }
	       else if (character == '}') {
	         addCharEntity(125, result);
	       }
	       else if (character == '~') {
	         addCharEntity(126, result);
	       }
	       else {
	         //the char is not a special one
	         //add it to the result as is
	         result.append(character);
	       }
	       character = iterator.next();
	     }
	     return result.toString();
	  }
	 private static void addCharEntity(Integer aIdx, StringBuilder aBuilder){
		    String padding = "";
		    if( aIdx <= 9 ){
		       padding = "00";
		    }
		    else if( aIdx <= 99 ){
		      padding = "0";
		    }
		    else {
		      //no prefix
		    }
		    String number = padding + aIdx.toString();
		    aBuilder.append("&#" + number + ";");
		  }

	 public static String forJSON(String aText){
		    final StringBuilder result = new StringBuilder();
		    StringCharacterIterator iterator = new StringCharacterIterator(aText);
		    char character = iterator.current();
		    while (character != StringCharacterIterator.DONE){
		      if( character == '\"' ){
		        result.append("\\\"");
		      }
		      else if(character == '\''){
			        result.append("&apos;");
			      }
		      else if(character == '\\'){
		        result.append("\\\\");
		      }
		      else if(character == '/'){
		        result.append("\\/");
		      }
		      else if(character == '\b'){
		        result.append("\\b");
		      }
		      else if(character == '\f'){
		        result.append("\\f");
		      }
		      else if(character == '\n'){
		        result.append("\\n");
		      }
		      else if(character == '\r'){
		        result.append("\\r");
		      }
		      else if(character == '\t'){
		        result.append("\\t");
		      }
		      else {
		        //the char is not a special one
		        //add it to the result as is
		        result.append(character);
		      }
		      character = iterator.next();
		    }
		    return result.toString();    
		  }
	 public static String formattedStringForJSon(String data) {
		 return forJSON(forHTML(data));
	 }
	 
	 public static HashMap<String, String> parseDataToMap(String actData){
		 HashMap<String, String> data = new HashMap<>();
		 
		 if(StringUtils.isNotBlank(actData)) {
			 String[] rowData = actData.split("\n");
			 for(String row : rowData) {
				 if(StringUtils.isNotBlank(row)) {
					 if(row.contains("=")) {
						 int fE = row.indexOf("=");
						 String pn = row.substring(0,fE);
						 String pv = row.substring(fE+1);
						 data.put(pn, pv);
					 }
				 }
			 }
		 }
		 return data;	 
	 }
	 
	 public static HashMap<String, List<String>> parseDataToMap(String actData,String valueSplitter){
		 HashMap<String, List<String>> data = new HashMap<>();
		 
		 if(StringUtils.isNotBlank(actData)) {
			 String[] rowData = actData.split("\n");
			 for(String row : rowData) {
				 if(StringUtils.isNotBlank(row)) {
					 if(row.contains("=")) {
						 int fE = row.indexOf("=");
						 String pn = row.substring(0,fE);
						 String pv = row.substring(fE+1);
						 data.put(pn, Arrays.asList(pv.split(valueSplitter)));
					 }
				 }
			 }
		 }
		 return data;	 
	 }
	 
}
