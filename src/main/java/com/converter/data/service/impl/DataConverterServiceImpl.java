package com.converter.data.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.converter.data.dto.ExampleDataDTO;
import com.converter.data.entity.Ordine;
import com.converter.data.entity.PrintSize;
import com.converter.data.model.MisuraStampa;
import com.converter.data.repository.OrdineRepository;
import com.converter.data.repository.PrintSizeRepository;
import com.converter.data.service.DataConverterService;
import com.converter.data.utils.ExcelGenerator;

@Service
public class DataConverterServiceImpl implements DataConverterService {
	
	@Autowired
	protected OrdineRepository ordineRepository;
	
	@Autowired
	protected PrintSizeRepository printSizeRepository;

	@Override
	public List<Ordine> findAll() {
		return ordineRepository.findAll();
	}

	@Override
	public List<Ordine> insertOrders(List<ExampleDataDTO> orders) {
		List<Ordine> ordersNTT = new ArrayList<>();
		
		List<Long> existingOrderNumber = ordineRepository.findDistinctOrderNumber();
		
		Long orderNumber = generaNumeroEscluso(existingOrderNumber);
		
		for(ExampleDataDTO order : orders) {
			Ordine ordine = Ordine.builder()
					.customer_name(order.getNomeCliente())
					.customer_surname(order.getCognomeCliente())
					.customer_phone_number(order.getCellulareCliente())
					.clothing_type(order.getTipoAbbigliamento())
					.quantity(order.getQuantita())
					.order_number(orderNumber)
					.clothing_size(order.getTagliaAbbigliamento().toString())
					.build();
			
			Ordine ord = ordineRepository.save(ordine);
			ordersNTT.add(ord);
						
			for (MisuraStampa misuraStampa : order.getMisuraStampa()) {
				PrintSize printSize = PrintSize.builder()
						.order_id(ord.getId())
						.size(misuraStampa.name())
						.build();
			printSizeRepository.save(printSize);
			}
			
		}
		
		return ordersNTT;
	}
	
	 public String convertListToString(List<MisuraStampa> stampe) {
	        String str = "";
	        for (MisuraStampa stampa : stampe) {
	            if (stampa.equals(MisuraStampa.GRANDE)) {
	                str += String.valueOf(stampa) + "(10€)" + "," + " ";
	            } else if (stampa.equals(MisuraStampa.MEDIA)) {
	                str += String.valueOf(stampa) + "(7€)" + "," + " ";
	            } else if (stampa.equals(MisuraStampa.PICCOLA)) {
	                str += String.valueOf(stampa) + "(5€)" + "," + " ";
	            }
	        }
	        str = str.substring(0, str.length() - 1);
	        return str;
	    }
	
	 public static Long generaNumeroEscluso(List<Long> listaEsclusi) {
	        Random rand = new Random();
	        Long numeroCasuale;

	        do {
	            numeroCasuale = (long) (rand.nextInt(10000 - 1000 + 1) + 1000);
	        } while (listaEsclusi.contains(numeroCasuale));

	        return numeroCasuale;
	    }

	@Override
	public byte[] generateExcel(List<ExampleDataDTO> orders) throws IOException {

        ExcelGenerator generator = new ExcelGenerator(orders);

        // Ottieni l'InputStream dal generatore
        InputStream inputStream = generator.generateAsInputStream();

        // Leggi l'InputStream in un array di byte
        byte[] excelBytes = org.apache.commons.io.IOUtils.toByteArray(inputStream);
        
        return excelBytes;
	}
	 
}

