package com.converter.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.converter.data.entity.Ordine;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long>{

}
