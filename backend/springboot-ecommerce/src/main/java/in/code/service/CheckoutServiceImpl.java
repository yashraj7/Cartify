package in.code.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import in.code.dao.CustomerRepository;
import in.code.dto.PaymentInfo;
import in.code.dto.Purchase;
import in.code.dto.PurchaseResponse;
import in.code.entity.Customer;
import in.code.entity.Order;
import in.code.entity.OrderItem;
import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	private CustomerRepository customerRepository;

	public CheckoutServiceImpl(@Value("${stripe.key.secret}") String secretKey) {
		Stripe.apiKey = secretKey;
	}

	@Override
	@Transactional
	public PurchaseResponse placeOrder(Purchase purchase) {

		// retrieve order info from dto
		Order order = purchase.getOrder();

		// generate Tracking number
		String orderTrackingNumber = generateOrderTrackingNumber();
		order.setOrderTrackingNumber(orderTrackingNumber);

		// populate order with orderItems
		Set<OrderItem> orderItems = purchase.getOrderItems();
		orderItems.forEach(item -> order.add(item));

		// populate order with billing and shippingAddress
		order.setBillingAddress(purchase.getBillingAddress());
		order.setShippingAddress(purchase.getShippigAddress());

		// populate customer with order
		Customer customer = purchase.getCustomer();

		String theEmail = customer.getEmail();

		Customer customerFromDB = customerRepository.findByEmail(theEmail);

		if (customerFromDB != null)
			customer = customerFromDB;

		customer.add(order);

		// save to database
		customerRepository.save(customer);

		// return a response
		return new PurchaseResponse(orderTrackingNumber);
	}

	private String generateOrderTrackingNumber() {
		// generate a random UUID number (UUID version-4)
		// For details see: https://en.wikipedia.org/wiki/Universally_unique_identifier
		return UUID.randomUUID().toString();
	}

	@Override
	public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

		List<String> paymentMethodTypes = new ArrayList<>();
		paymentMethodTypes.add("card");

		Map<String, Object> params = new HashMap<>();
		params.put("amount", paymentInfo.getAmount());
		params.put("currency", paymentInfo.getCurrency());
		params.put("payment_method_types", paymentMethodTypes);
		params.put("description", "Cartify Purchase");
		params.put("receipt_email", paymentInfo.getReceiptEmail());

		return PaymentIntent.create(params);
	}

}
