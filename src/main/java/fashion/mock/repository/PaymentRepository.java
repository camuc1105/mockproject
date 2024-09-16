package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
