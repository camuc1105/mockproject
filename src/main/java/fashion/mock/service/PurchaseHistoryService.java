/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fashion.mock.model.Order;
import fashion.mock.model.TransactionHistory;
import fashion.mock.model.DTO.TransactionHistoryDTO;
import fashion.mock.repository.OrderRepository;
import fashion.mock.repository.PurchaseHistoryRepository;
import fashion.mock.repository.TransactionHistoryRepository;

@Service
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final OrderRepository orderRepository;

    public PurchaseHistoryService(PurchaseHistoryRepository purchaseHistoryRepository,
            TransactionHistoryRepository transactionHistoryRepository,
            OrderRepository orderRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.orderRepository = orderRepository;
    }

    public Page<TransactionHistoryDTO> findAllTransactionHistoriesByUserId(Long userId, Pageable pageable) {
        return purchaseHistoryRepository.findAllTransactionHistoriesByUserId(userId, pageable);
    }

    public boolean cancelOrder(Long orderId, Long userId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(orderId, userId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if ("Chờ phê duyệt".equals(order.getStatus())) {
                order.setStatus("Đã hủy");
                order.setUpdatedDate(LocalDate.now());
                orderRepository.save(order);

                Optional<TransactionHistory> optionalTransactionHistory = transactionHistoryRepository.findByOrderId(orderId);
                if (optionalTransactionHistory.isPresent()) {
                    TransactionHistory transactionHistory = optionalTransactionHistory.get();

                    if ("Hoàn tất".equals(transactionHistory.getStatus())) {
                        transactionHistory.setStatus("Chờ thanh toán");
                        transactionHistory.setTransactionDate(LocalDate.now());
                        transactionHistoryRepository.save(transactionHistory);
                    }
                }

                return true;
            }
        }

        return false;
    }

}
