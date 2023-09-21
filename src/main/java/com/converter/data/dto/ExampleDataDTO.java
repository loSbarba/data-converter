package com.converter.data.dto;

import java.util.List;

import com.converter.data.model.MisuraStampa;
import com.converter.data.model.TagliaAbbigliamento;
import com.converter.data.model.TipoAbbigliamento;

import lombok.Data;

@Data
public class ExampleDataDTO {
	String nomeCliente;
	String cognomeCliente;
	String cellulareCliente;
	TipoAbbigliamento tipoAbbigliamento;
	TagliaAbbigliamento tagliaAbbigliamento;
	List<MisuraStampa> misuraStampa;
	Integer quantita;
}
