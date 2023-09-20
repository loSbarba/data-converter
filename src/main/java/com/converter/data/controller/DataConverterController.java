package com.converter.data.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.converter.data.dto.ExampleDataDTO;
import com.converter.data.service.DataConverterService;
import com.converter.data.utils.ExcelGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class DataConverterController {
	
	@Autowired
	DataConverterService dataConverterService;

	@Operation(operationId = "test", summary = "test")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json") }) })
	@GetMapping(value = "/test", consumes = "application/json")
	public void getExcell(HttpServletResponse response, @RequestBody List<ExampleDataDTO> datas) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=records_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		ExcelGenerator generator = new ExcelGenerator(datas);

		generator.generate(response);
	}
	
}
