package fashion.mock.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    public Page<Product> searchProducts(String searchTerm, PageRequest pageRequest) {
        return productRepository.findByProductNameContainingIgnoreCase(searchTerm, pageRequest);
    }

    public Page<Product> getAllProductsSortedByPriceAsc(PageRequest pageRequest) {
        return productRepository.findAllByOrderByPriceAsc(pageRequest);
    }

    public Page<Product> getAllProductsSortedByPriceDesc(PageRequest pageRequest) {
        return productRepository.findAllByOrderByPriceDesc(pageRequest);
    }

    public Page<Product> searchProductsSortedByPriceAsc(String searchTerm, PageRequest pageRequest) {
        return productRepository.findByProductNameContainingIgnoreCaseOrderByPriceAsc(searchTerm, pageRequest);
    }

    public Page<Product> searchProductsSortedByPriceDesc(String searchTerm, PageRequest pageRequest) {
        return productRepository.findByProductNameContainingIgnoreCaseOrderByPriceDesc(searchTerm, pageRequest);
    }
    
    private static final Map<String, List<String>> COLOR_MAPPING = new HashMap<>();
    static {
        COLOR_MAPPING.put("White", Arrays.asList("White", "Trắng"));
        COLOR_MAPPING.put("Yellow", Arrays.asList("Yellow", "Vàng"));
        COLOR_MAPPING.put("Red", Arrays.asList("Red", "Đỏ"));
        COLOR_MAPPING.put("Green", Arrays.asList("Green", "Xanh lá"));
        COLOR_MAPPING.put("Blue", Arrays.asList("Blue", "Xanh dương"));
        COLOR_MAPPING.put("Purple", Arrays.asList("Purple", "Tím"));
        COLOR_MAPPING.put("Orange", Arrays.asList("Orange", "Cam"));
        COLOR_MAPPING.put("Pink", Arrays.asList("Pink", "Hồng"));
        COLOR_MAPPING.put("Black", Arrays.asList("Black", "Đen"));
    }
    
    public Page<Product> getFilteredProducts(String searchTerm, String sortBy, String color, String size, String priceRange, PageRequest pageRequest) {
        Specification<Product> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%"));
        }

        if (color != null && !color.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                List<String> colorVariants = getColorVariants(color);
                return root.get("color").in(colorVariants);
            });
        }

        if (size != null && !size.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("size"), size));
        }
        if (priceRange != null && !priceRange.isEmpty()) {
            String[] range = priceRange.split("-");
            Double minPrice = parsePrice(range[0]); // Giá trị tối thiểu luôn có

            Double maxPrice;
            if (range.length > 1 && !range[1].isEmpty()) {
                // Nếu có giá trị tối đa thì chuyển đổi
                maxPrice = parsePrice(range[1]);
            } else {
                // Nếu không có giá trị tối đa thì đặt là Double.MAX_VALUE
                maxPrice = Double.MAX_VALUE;
            }

            // Áp dụng bộ lọc giá
            spec = spec.and((root, query, cb) -> cb.between(root.get("price"), minPrice, maxPrice));
        }


        if ("priceAsc".equals(sortBy)) {
            return productRepository.findAll(spec, pageRequest.withSort(Sort.by(Sort.Direction.ASC, "price")));
        } else if ("priceDesc".equals(sortBy)) {
            return productRepository.findAll(spec, pageRequest.withSort(Sort.by(Sort.Direction.DESC, "price")));
        } else {
            return productRepository.findAll(spec, pageRequest);
        }
    }
    private Double parsePrice(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) {
            return 0.0; // Giá trị mặc định nếu chuỗi rỗng
        }
        String cleanedStr = priceStr.replaceAll("[^0-9.]", "").replace(".", "");
        return Double.parseDouble(cleanedStr);
    }
    private List<String> getColorVariants(String color) {
        for (Map.Entry<String, List<String>> entry : COLOR_MAPPING.entrySet()) {
            if (entry.getValue().stream().anyMatch(c -> c.equalsIgnoreCase(color))) {
                return entry.getValue();
            }
        }
        return Arrays.asList(color);
    }

 	List<Product> ls = new ArrayList<Product>();

 	public List<Product> getAllProducts() {
 		ls = productRepository.findAll();
 		return ls;
 	}

 	public Product findProductById(Long id) {
 		for (Product product : ls) {
 			if (product.getId() == id) {
 				return product;
 			}
 		}
 		return null;
 	}
 }

