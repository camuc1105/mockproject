package fashion.mock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fashion.mock.model.Payment;
import fashion.mock.repository.PaymentRepository;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public PaymentService(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}
}
