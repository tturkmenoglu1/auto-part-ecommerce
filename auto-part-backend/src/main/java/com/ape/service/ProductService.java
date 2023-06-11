package com.ape.service;

import com.ape.dto.ProductDTO;
import com.ape.dto.request.ProductRequest;
import com.ape.dto.request.ProductUpdateRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.ProductMapper;
import com.ape.model.Brand;
import com.ape.model.Category;
import com.ape.model.ImageFile;
import com.ape.model.Product;
import com.ape.model.enums.ProductStatus;
import com.ape.repository.BrandRepository;
import com.ape.repository.CategoryRepository;
import com.ape.repository.ProductRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ImageFileService imageFileService;

    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    private final ProductMapper productMapper;


    public void saveProduct(ProductRequest productRequest) {
        Set<ImageFile> imageFiles = new HashSet<>();
        for (String each:productRequest.getImageId()) {
            Product foundProduct = productRepository.findProductByImageId(each);
            if (foundProduct==null){
                imageFiles.add(imageFileService.getImageById(each));
            }else{
                throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
            }
        }

        boolean hasShowcase = false;
        for (ImageFile each:imageFiles) {
            if (each.isShowcase()){
                hasShowcase = true;
                break;
            }
        }
        if(!hasShowcase){
            ImageFile imageFile = imageFiles.stream().findFirst().orElse(null);
            assert imageFile != null;
            imageFile.setShowcase(true);
        }
        Brand brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.BRAND_NOT_FOUND_MESSAGE,productRequest.getBrandId())));;
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());

        Product product = productMapper.productRequestToProduct(productRequest);
        product.setStatus(ProductStatus.NOT_PUBLISHED);
        product.setBrand(brand);
        product.setImage(imageFiles);
        product.setStatus(productRequest.getStatus());
        product.setCategory(category);
        product.setDiscountedPrice(product.getPrice()*(100-product.getDiscount())/100);
        productRepository.save(product);
    }

    public Product findProductById(Long productId) {
        return productRepository.findProductById(productId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,productId)));
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public ProductDTO getProductDTOById(Long id) {
        Product product = findProductById(id);

        return productMapper.productToProductDTO(product);
    }

    public void removeById(Long id) {
        Product product = findProductById(id);

        List<Product> products = productRepository.checkOrderItemsByID(id);
        if (products.size()>0){
            throw new BadRequestException(ErrorMessage.PRODUCT_USED_BY_ORDER_MESSAGE);
        }
        productRepository.delete(product);
    }

    public ProductDTO updateProduct(Long id,ProductUpdateRequest productUpdateRequest) {
        Product product = findProductById(id);
        Set<ImageFile> imageFiles = product.getImage();
        for (String each : productUpdateRequest.getImageId()) {
            Product foundProduct = productRepository.findProductByImageId(each);
            if (foundProduct == null) {
                imageFiles.add(imageFileService.getImageById(each));
            } else {
                throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
            }
        }
        boolean hasShowcase = false;
        for (ImageFile each : imageFiles) {
            if (each.isShowcase()) {
                hasShowcase = true;
                break;
            }
        }
        if (!hasShowcase) {
            ImageFile imageFile = imageFiles.stream().findFirst().orElse(null);
            assert imageFile != null;
            imageFile.setShowcase(true);
        }
        Brand brand = brandRepository.findById(productUpdateRequest.getBrandId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.BRAND_NOT_FOUND_MESSAGE, id)));
        Category category = categoryService.getCategoryById(productUpdateRequest.getCategoryId());

        product.setTitle(productUpdateRequest.getTitle());
        product.setShortDesc(productUpdateRequest.getShortDesc());
        product.setLongDesc(productUpdateRequest.getLongDesc());
        product.setPrice(productUpdateRequest.getPrice());
        product.setTax(productUpdateRequest.getTax());
        product.setDiscount(productUpdateRequest.getDiscount());
        product.setStockAmount(productUpdateRequest.getStockAmount());
        product.setStatus(productUpdateRequest.getStatus());
        product.setWidth(productUpdateRequest.getWidth());
        product.setLength(productUpdateRequest.getLength());
        product.setHeight(productUpdateRequest.getHeight());
        product.setUpdateAt(LocalDateTime.now());
        product.setBrand(brand);
        product.setCategory(category);
        product.setImage(imageFiles);
        product.setDiscountedPrice(product.getPrice() * (100 - product.getDiscount()) / 100);
        productRepository.save(product);

        return productMapper.productToProductDTO(product);
    }

    public void removeImageById(String id) {
        Product product = productRepository.findProductByImageId(id);
        imageFileService.removeById(id);
        boolean hasShowcase = false;
        for (ImageFile each:product.getImage()) {
            if (each.isShowcase()){
                hasShowcase = true;
                break;
            }
        }
        if(!hasShowcase){
            ImageFile imageFile = product.getImage().stream().findFirst().orElse(null);
            assert imageFile != null;
            imageFile.setShowcase(true);
        }
    }
}
