package com.converter.data.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.converter.data.dto.ExampleDataDTO;
import com.converter.data.dto.OrdineDTO;
import com.converter.data.entity.Ordine;

@Service
public interface DataConverterService {
	
	public List<Ordine> findAll();
	
	public List<OrdineDTO> insertOrders(List<ExampleDataDTO> orders);
	
	public byte[] generateExcel(List<ExampleDataDTO> orders) throws IOException;
	
	public byte[] generateExcelByDB() throws IOException;
	
}
