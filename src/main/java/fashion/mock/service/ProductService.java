package fashion.mock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fashion.mock.model.Image;
import fashion.mock.model.Product;
import fashion.mock.repository.ImageRepository;
import fashion.mock.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Product addProduct(Product product, List<MultipartFile> imageFiles) {
        validateProduct(product);
        product.setCreatedDate(LocalDate.now());
        Product savedProduct = productRepository.save(product);
        
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String imagePath = fileStorageService.storeFile(file);
                Image image = new Image();
                image.setImgLink(imagePath);
                image.setProduct(savedProduct);
                imageRepository.save(image);
            }
        }
        
        return savedProduct;
    }

    public Product updateProduct(Product product, List<MultipartFile> imageFiles) {
        validateProduct(product);
        Product existingProduct = productRepository.findById(product.getId())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + product.getId()));
        
        existingProduct.setProductName(product.getProductName().trim());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setColor(product.getColor());
        existingProduct.setSize(product.getSize());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setUpdatedDate(LocalDate.now());
        
        Product updatedProduct = productRepository.save(existingProduct);
        
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String imagePath = fileStorageService.storeFile(file);
                Image image = new Image();
                image.setImgLink(imagePath);
                image.setProduct(updatedProduct);
                imageRepository.save(image);
            }
        }
        
        return updatedProduct;
    }

    private void validateProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        String trimmedName = product.getProductName().trim();
        Product existingProduct = productRepository.findByProductName(trimmedName);
        if (existingProduct != null && !existingProduct.getId().equals(product.getId())) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại");
        }
        product.setProductName(trimmedName);
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            for (Image image : product.getImages()) {
                fileStorageService.deleteFile(image.getImgLink());
            }
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.searchByName(searchTerm.trim(), pageable);
    }

    public boolean deleteImage(Long imageId) {
        Optional<Image> imageOpt = imageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            fileStorageService.deleteFile(image.getImgLink());
            imageRepository.delete(image);
            return true;
        }
        return false;
    }
}