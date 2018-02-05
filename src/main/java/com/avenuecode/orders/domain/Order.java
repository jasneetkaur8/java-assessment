package com.avenuecode.orders.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigInteger.ZERO;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "orders")
@JsonInclude(NON_NULL)
public class Order implements Serializable {

	public static final int PRECISION = 2;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private String orderId;

	@Column(unique = true, nullable = false, length = 8)
	private String orderNumber;

	@Column
	private BigDecimal discount;

	@Column(nullable = false)
	private BigDecimal taxPercent;

	private BigDecimal total;

	private BigDecimal totalTax;

	private BigDecimal grandTotal;

	@Column(length = 10)
	private String status;

	@ManyToMany(fetch = EAGER, cascade = ALL)
	@JoinTable(name = "order_product", joinColumns = @JoinColumn(name = "order_id", updatable = false, nullable = false), inverseJoinColumns = @JoinColumn(name = "product_id", updatable = false, nullable = false))
	private List<Product> products = new ArrayList<>();

	public Order(String orderId, String orderNumber, BigDecimal discount, BigDecimal taxPercent, String status) {
		super();
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.discount = discount;
		this.taxPercent = taxPercent;
		this.status = status;
	}

	public Order() {

	}

	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(ZERO);
		for (Product product : products) {
			total = total.add(product.getPrice());
		}
		return scaled(total);
	}

	public BigDecimal getTotalTax() {
		return scaled(getTotal().multiply(taxPercent.divide(new BigDecimal("100"))));
	}

	public BigDecimal getGrandTotal() {
		BigDecimal total = this.getTotal().add(getTotalTax());
		if (discount != null) {
			return scaled(total.subtract(discount));
		}
		return scaled(total);
	}

	private BigDecimal scaled(BigDecimal value) {
		return value.setScale(PRECISION, ROUND_FLOOR);
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderNumber=" + orderNumber + ", discount=" + discount + ", taxPercent="
				+ taxPercent + ", status=" + status + ", products=" + products + "]";
	}

}
