package fashion.mock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fashion.mock.model.DTO.TransactionHistoryDTO;
import fashion.mock.repository.PurchaseHistoryRepository;

@Service
public class PurchaseHistoryService {

    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;
    
    public Page<TransactionHistoryDTO> findAllTransactionHistories(Pageable pageable) {
        return purchaseHistoryRepository.findAllTransactionHistories(pageable);
    }
}
