package com.converter.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.converter.data.entity.Ordine;
import com.converter.data.repository.OrdineRepository;
import com.converter.data.service.DataConverterService;

@Service
public class DataConverterServiceImpl implements DataConverterService {
	
	@Autowired
	protected OrdineRepository testRepository;

	@Override
	public List<Ordine> findAll() {
		return testRepository.findAll();
	}
	
	
	
}

