package in.code.dto;

import java.util.Set;

import in.code.entity.Address;
import in.code.entity.Customer;
import in.code.entity.Order;
import in.code.entity.OrderItem;
import lombok.Data;

@Data
public class Purchase {

	private Customer customer;
	private Address shippigAddress;
	private Address billingAddress;
	private Order order;
	private Set<OrderItem> orderItems;

}
