package com.technotree.models.order;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.technotree.models.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "orders")
public class Order 
{
	@Id 
	@GeneratedValue
	private Long id;
	private OrderStatus status = OrderStatus.IN_PROGRESS;
	private Category category = Category.BOOK;
	private String descrition;
	private Float price;
	private Float vat;
	private Date createdAt = new Date();
	private Date updatedAt = new Date();
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User customer;
	
}