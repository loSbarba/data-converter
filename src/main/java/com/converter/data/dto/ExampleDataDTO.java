package com.converter.data.dto;

import java.util.List;

import com.converter.data.model.MisuraStampa;
import com.converter.data.model.TagliaAbbigliamento;
import com.converter.data.model.TipoAbbigliamento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExampleDataDTO {
	String nomeCliente;
	String cognomeCliente;
	String cellulareCliente;
	TipoAbbigliamento tipoAbbigliamento;
	TagliaAbbigliamento tagliaAbbigliamento;
	List<MisuraStampa> misuraStampa;
	Long quantita;
}
