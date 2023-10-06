package com.converter.data.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.converter.data.dto.ExampleDataDTO;
import com.converter.data.entity.Ordine;
import com.converter.data.entity.PrintSize;
import com.converter.data.repository.PrintSizeRepository;
import com.converter.data.service.DataConverterService;

@RestController
public class DataConverterController {
    
    @Autowired
    DataConverterService dataConverterService;
    
    @Autowired
    PrintSizeRepository printSizeRepository;

    @PostMapping(value = "/test", consumes = "application/json")
    public ResponseEntity<byte[]> generateExcel(@RequestBody List<ExampleDataDTO> datas) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String filename = "records_" + currentDateTime + ".xlsx";

        // Leggi l'InputStream in un array di byte
        byte[] excelBytes = dataConverterService.generateExcel(datas);

        // Imposta gli header per la risposta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        // Restituisci il file Excel come ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
    
    @GetMapping("/testAll")
    public List<Ordine> findAllTest(){
    	return dataConverterService.findAll();
    }
    
    
    @PostMapping(value = "/createOrder", consumes = "application/json")
    public ResponseEntity<List<Ordine>> createOrder(@RequestBody List<ExampleDataDTO> datas) throws IOException {
    	
    	List<Ordine> result =  dataConverterService.insertOrders(datas);
    	
    	 return ResponseEntity.ok()
                 .body(result);
    }
    
    
    @GetMapping(value = "/test")
    public List<PrintSize> GetPrintSize() {
		return printSizeRepository.findAll();
	}
    
    
}
