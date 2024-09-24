package fashion.mock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fashion.mock.model.Discount;
import fashion.mock.model.Product;
import fashion.mock.repository.DiscountRepository;
import fashion.mock.repository.ProductRepository;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    public Discount addDiscount(Discount discount) {
        validateDiscount(discount);
        Product product = productRepository.findByProductName(discount.getProductName());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        
        // Kiểm tra các chiết khấu hiện có có phạm vi ngày chồng chéo
        if (hasOverlappingDiscount(product, discount.getStartDate(), discount.getEndDate(), null)) {
            throw new IllegalArgumentException("Sản phẩm này đã có khuyến mãi trong khoảng thời gian này");
        }
        
        discount.setProduct(product);
        discount.setCreatedDate(LocalDate.now());
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Discount discount) {
        validateDiscount(discount);
        Discount existingDiscount = discountRepository.findById(discount.getId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảm giá với ID: " + discount.getId()));
        
        Product product = productRepository.findByProductName(discount.getProductName());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại");
        }
        
        // Kiểm tra các chiết khấu hiện có có phạm vi ngày trùng lặp (không bao gồm chiết khấu hiện tại)
        if (hasOverlappingDiscount(product, discount.getStartDate(), discount.getEndDate(), discount.getId())) {
            throw new IllegalArgumentException("Sản phẩm này đã có khuyến mãi trong khoảng thời gian này");
        }
        
        existingDiscount.setProduct(product);
        existingDiscount.setDiscountPercent(discount.getDiscountPercent());
        existingDiscount.setStartDate(discount.getStartDate());
        existingDiscount.setEndDate(discount.getEndDate());
        existingDiscount.setUpdatedDate(LocalDate.now());
        return discountRepository.save(existingDiscount);
    }

    private boolean hasOverlappingDiscount(Product product, LocalDate startDate, LocalDate endDate, Long excludeId) {
        List<Discount> overlappingDiscounts;
        if (excludeId != null) {
            overlappingDiscounts = discountRepository.findOverlappingDiscounts(product, startDate, endDate, excludeId);
        } else {
            overlappingDiscounts = discountRepository.findOverlappingDiscounts(product, startDate, endDate);
        }
        return !overlappingDiscounts.isEmpty();
    }

    private void validateDiscount(Discount discount) {
        if (discount.getDiscountPercent() == null || discount.getDiscountPercent() < 0 || discount.getDiscountPercent() > 100) {
            throw new IllegalArgumentException("Phần trăm giảm giá phải từ 0 đến 100");
        }
        if (discount.getStartDate() == null || discount.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc không được để trống");
        }
        if (discount.getStartDate().isAfter(discount.getEndDate())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    public boolean deleteDiscount(Long id) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Page<Discount> getAllDiscounts(Pageable pageable) {
        return discountRepository.findAll(pageable);
    }

    public Optional<Discount> getDiscountById(Long id) {
        return discountRepository.findById(id);
    }

    public Page<Discount> searchDiscounts(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return discountRepository.findAll(pageable);
        }
        return discountRepository.searchByProductName(searchTerm.trim(), pageable);
    }
}