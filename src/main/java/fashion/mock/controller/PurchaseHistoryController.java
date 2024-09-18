package fashion.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.DTO.TransactionHistoryDTO;
import fashion.mock.service.PurchaseHistoryService;

@Controller
@RequestMapping("/information")
public class PurchaseHistoryController {

    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    @GetMapping("purchase-history")
    public String viewPurchaseHistory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionHistoryDTO> transactionHistoriesPage = purchaseHistoryService.findAllTransactionHistories(pageable);

        model.addAttribute("transactionHistoriesPage", transactionHistoriesPage);
        return "purchase-history";
    }

    @GetMapping("customer-information")
    public String viewCustomerInformation(Model model) {
        return "customer-information";
    }
}
