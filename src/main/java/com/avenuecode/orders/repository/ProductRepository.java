package com.avenuecode.orders.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avenuecode.orders.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Serializable> {
	public List<Product> findByPriceGreaterThan(BigDecimal value);
}
