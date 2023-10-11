package com.converter.data.dto;

import com.converter.data.model.TipoAbbigliamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdineDTO {
	Long id;
	Long orderNumber;
	String customerName;
	String customerSurname;
	String customerPhoneNumber;
	TipoAbbigliamento clothingType;
	String clothingSize;
	Long quantity;
}
