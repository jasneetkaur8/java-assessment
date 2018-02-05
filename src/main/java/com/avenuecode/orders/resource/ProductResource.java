package com.avenuecode.orders.resource;

import com.avenuecode.orders.domain.Product;
import com.avenuecode.orders.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<List<Product>> listProducts() {
		List<Product> products = productService.listProducts();
		products.forEach(a -> System.out.println(a));
		return ok(products);
	}

	@GetMapping(value = "/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable String productId) {
		Product product = productService.getProduct(productId);
		if (product == null) {
			return notFound().build();
		}
		return ok(product);
	}

	@GetMapping(value = "/priceMoreThan30")
	public ResponseEntity<List<Product>> getProduct() {
		List<Product> products = productService.getProductsWithPriceMoreThan(new BigDecimal(30));
		if (products == null) {
			return notFound().build();
		}
		return ok(products);
	}

}
