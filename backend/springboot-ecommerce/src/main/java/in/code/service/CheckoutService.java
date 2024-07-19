package in.code.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import in.code.dto.PaymentInfo;
import in.code.dto.Purchase;
import in.code.dto.PurchaseResponse;

public interface CheckoutService {

	PurchaseResponse placeOrder(Purchase purchase);
	
	PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
