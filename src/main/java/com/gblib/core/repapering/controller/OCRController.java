 package com.gblib.core.repapering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gblib.core.repapering.services.OCRService;



@RestController
 public class OCRController {

	@Autowired
	OCRService ocrService;
	
	@RequestMapping(value = "/ocr/convert/{input}", method = RequestMethod.GET)
	public void converttoPdf(@PathVariable String input) {
		ocrService.convert(input, "ocroutput.pdf");
	}
	
}
