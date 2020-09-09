package com.gblib.core.repapering.services;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gblib.core.repapering.model.LiborDocument;
import com.opencsv.CSVReader;

@Service
public class FileParsingService {
	
	@Value("${gblib.core.repapering.analytics.classify.outputdir}")
	private String outputFileDir;
	
	public List<LiborDocument> parseCsvFile() {
		String csvFile = outputFileDir + "\\" + "output.csv";
		System.out.println(csvFile);
		
		List<LiborDocument> impactedContracts = new ArrayList<LiborDocument>();
		
        CSVReader reader = null;
        try {
        	reader = new CSVReader(new FileReader(csvFile));
        	String[] line;
        	boolean isFirstLine=true;
        	while ((line = reader.readNext()) != null) {
        		if(!isFirstLine) {
        			//
        			System.out.println("0" + Integer.parseInt(line[0]));
        			System.out.println("1" + Integer.parseInt(line[1]));
        			System.out.println("2" + Integer.parseInt(line[2]));
        			System.out.println("13" + Integer.parseInt(line[13]));
        			System.out.println("14" + Integer.parseInt(line[14]));
        			System.out.println("16" + Integer.parseInt(line[16]));
        			System.out.println("Another line");
        			//
        			try {
        				impactedContracts.add(new LiborDocument(Integer.parseInt(line[0]), Integer.parseInt(line[1]),Integer.parseInt(line[2]), line[3], line[4], 
        						line[5], line[6], line[7], line[8],line[9],line[10], line[11], line[12],Integer.parseInt(line[13]),
        						Integer.parseInt(line[14]),line[15],Integer.parseInt(line[16])));
        			}catch(Exception e) {
        				e.printStackTrace();
        			}

        		}else {
        			isFirstLine=false;
        		}
        	}
        	
        	
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return impactedContracts;
	}
}
