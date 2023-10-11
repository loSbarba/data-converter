package com.converter.data.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.converter.data.dto.OrdineDTO;
import com.converter.data.entity.Ordine;

@Mapper
public interface OrdineMapper {

    OrdineMapper INSTANCE = Mappers.getMapper( OrdineMapper.class );

    @InheritInverseConfiguration()
    OrdineDTO entityToDto(Ordine listNTT);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orderNumber", target = "order_number")
    @Mapping(source = "customerName", target = "customer_name")
    @Mapping(source = "customerSurname", target = "customer_surname")
    @Mapping(source = "customerPhoneNumber", target = "customer_phone_number")
    @Mapping(source = "clothingType", target = "clothing_type")
    @Mapping(source = "clothingSize", target = "clothing_size")
    @Mapping(source = "quantity", target = "quantity")
    Ordine dtoToEntity(OrdineDTO ordineDTO);




}
