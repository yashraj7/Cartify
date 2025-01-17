package in.code.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import in.code.dto.PaymentInfo;
import in.code.dto.Purchase;
import in.code.dto.PurchaseResponse;
import in.code.service.CheckoutService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	private CheckoutService checkoutService;

	@PostMapping("/purchase")
	public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {

		PurchaseResponse purchaseResponse = checkoutService.placeOrder(purchase);
		return purchaseResponse;
	}

	@PostMapping("/payment-intent")
	public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {

		logger.info("payment amount: " + paymentInfo.getAmount());

		PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);
		String paymentStr = paymentIntent.toJson();

		return new ResponseEntity<>(paymentStr, HttpStatus.OK);
	}

}
