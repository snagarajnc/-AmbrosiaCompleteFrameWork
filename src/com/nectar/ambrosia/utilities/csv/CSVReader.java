package com.nectar.ambrosia.utilities.csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

public class CSVReader {
	static Logger log = Logger.getLogger(CSVReader.class.getName());
	
	public static List<CSVRecord> getCSVRecords(String filePath) throws IOException {
		try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());){
			log.info("Reading csv file from " + filePath);
			return csvParser.getRecords();
		}
	}
}
