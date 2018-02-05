package com.avenuecode.orders.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> listProducts() {
		return productRepository.findAll();
	}

	public Product getProduct(String productId) {
		return productRepository.findOne(productId);
	}

	public List<Product> getProductsWithPriceMoreThan(BigDecimal price) {
		return productRepository.findByPriceGreaterThan(price);
	}

}
