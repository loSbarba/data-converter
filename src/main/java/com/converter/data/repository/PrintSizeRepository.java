package com.converter.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.converter.data.entity.PrintSize;

@Repository
public interface PrintSizeRepository extends JpaRepository<PrintSize, Long>{
	
}
