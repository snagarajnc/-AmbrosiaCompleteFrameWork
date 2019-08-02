package com.nectar.ambrosia.utilities.csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.nectar.ambrosia.common.Consts;

public class CSVFileData {
	static Logger log = Logger.getLogger(CSVFileData.class.getName());
	
	private int rowCount = -1;
	private int columnCount = -1;
	private List<CSVRecord> rowData;
	private int curRowNumber = -1;

	public static void main(String[] args) throws IOException {
		CSVFileData cfd = new CSVFileData("./testdata/master.csv");
		System.out.println(cfd.getRowCount());
		System.out.println(cfd.getColumnCount());
		cfd.setCurRowNumber("tcid", "tc-012");
		System.out.println(cfd.getValue("runflag"));
	}

	public CSVFileData(List<CSVRecord> rowData) {
		this.rowData = rowData;
		initialize();
	}

	public CSVFileData(String filePath) throws IOException {
		try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			this.rowData = csvParser.getRecords();
			initialize();
		}
	}

	private void initialize() {
		this.rowCount = this.rowData.size();
		setColumnCount();
	}

	public int getRowCount() {
		return this.rowCount;
	}

	public void setColumnCount() {
		if (this.rowCount > 0) {
			this.columnCount = this.rowData.get(0).size();
		}
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public int getRowNumber() {
		return this.curRowNumber;
	}

	public int getCurrentRowNumber() {
		return this.curRowNumber;
	}

	public boolean setCurRowNumber(String colName, String colValue) {
		
		for (CSVRecord csvR : rowData) {
			try {
				if (csvR.get(Consts.CN_TESTCASEID).equalsIgnoreCase(colValue)) {
					this.curRowNumber = (int) csvR.getRecordNumber();
					this.curRowNumber--;
					log.debug("Current row number is set to " + this.curRowNumber);
					return true;
				}
			} catch (Exception e) {
				log.error(String.format("Error while setting the current row for the column %s with value as %s ",colName,colValue));
			}
		}
		return false;
	}

	public boolean setCurRowNumber(HashMap<String, String> data) {
		for (CSVRecord csvR : rowData) {
			try {
				int matchCount = data.size();
				int currentCount = 0;
				for (String k : data.keySet()) {
					if (csvR.get(k).equalsIgnoreCase(data.get(k))) {
						currentCount++;
					}
				}
				if (matchCount == currentCount) {
					this.curRowNumber = (int) csvR.getRecordNumber();
					this.curRowNumber--;
					log.debug("Current row number is set to " + this.curRowNumber);
					return true;
				}

			} catch (Exception e) {
				log.error(String.format("Error while setting the current row for the column %s with value as %s ",data.keySet().toArray(),data.values().toArray()));
			}
		}
		return false;
	}

	public String getValue(String colName) {
		try {
			return this.rowData.get(this.curRowNumber).get(colName);
		} catch (Exception e) {
			log.error(String.format("no data found for the column %s in row number %s ",colName,this.curRowNumber));
		}
		return "";
	}
	
	public Map<String,String> getValuesOfTestCase(){
		return this.rowData.get(this.curRowNumber).toMap();
	}
	
	public List<CSVRecord> getListData(){
		return this.rowData;
	}
}
