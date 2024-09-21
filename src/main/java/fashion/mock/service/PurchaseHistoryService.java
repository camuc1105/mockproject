/**
 * @author Duong Van Luc 01/07/2000
 */
package fashion.mock.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fashion.mock.model.DTO.TransactionHistoryDTO;
import fashion.mock.repository.PurchaseHistoryRepository;

@Service
public class PurchaseHistoryService {

	private final PurchaseHistoryRepository purchaseHistoryRepository;

	public PurchaseHistoryService(PurchaseHistoryRepository purchaseHistoryRepository) {
		this.purchaseHistoryRepository = purchaseHistoryRepository;
	}

	public Page<TransactionHistoryDTO> findAllTransactionHistories(Pageable pageable) {
		return purchaseHistoryRepository.findAllTransactionHistories(pageable);
	}
}
