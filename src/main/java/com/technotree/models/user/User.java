package com.technotree.models.user;

import java.util.ArrayList;   
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.technotree.models.order.Order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "users")
public class User 
{
	@Id 
	@GeneratedValue
	private  Long id;
	
	private String username = "username" ;
	private String password = "password";
	
	private String adminSecretKey = "";
	
	private String fullname;
	private Gender gender;
	private String address;
	private String phone;
	
	private Role role = Role.CUSTOMER;
	
	@OneToMany( mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders = new ArrayList<>();
	
	@Override
	public String toString()
	{
		return "Id: " + this.id
			+	" Username: " + this.username
			+	" Fullname: " + this.fullname
			+	" Gender: " + this.gender
			+	" Address: " + this.address 
			+	" Phone: " + this.phone
			+	" Orders: " + this.orders.toString();
	}
	
	
}

