package com.converter.data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.converter.data.entity.Ordine;

@Service
public interface DataConverterService {
	
	public List<Ordine> findAll();
	
}
