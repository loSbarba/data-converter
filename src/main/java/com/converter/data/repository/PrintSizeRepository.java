package com.converter.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.converter.data.entity.PrintSize;
import com.converter.data.model.MisuraStampa;

@Repository
public interface PrintSizeRepository extends JpaRepository<PrintSize, Long>{
	
	@Query(value="SELECT size FROM PrintSize where order_id = :orderId ")
	List<MisuraStampa> findSizeByOrderId(@Param("orderId") Long id);
	
}
