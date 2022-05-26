package com.MS.catalog.service;

import com.MS.catalog.model.Variation;
import com.MS.catalog.repository.CategoryRepository;
import com.MS.catalog.repository.ProductRepository;
import com.MS.catalog.repository.VariationRepository;
import com.MS.catalog.model.Category;
import com.MS.catalog.model.DTO.CategoryDTO;
import com.MS.catalog.model.DTO.ProductDTO;
import com.MS.catalog.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class ProductService{

    @Autowired
    private ProductRepository productRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private VariationRepository variationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public Product createProduct(ProductDTO productDTO){
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory_ids(productDTO.getCategory_ids());
        categoryChecker(product.getCategory_ids());
        activeCategoryChecker(product.getCategory_ids());
        product.setProduct_id(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        product = categoryUpdater(product);
        return productRepository.save(product);
    }

    public List<ProductDTO> listDTO(List<Product> products) {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Product product : products){
            productDTOList.add(normalToDTO(product));
        }
        return productDTOList;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public ProductDTO normalToDTO(Product product){
        long product_id = product.getProduct_id();
        product.setVariationList(variationRepository.findByProduct_id(product_id));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    public Product getProductById(long product_id) {
        Product product = productRepository.findByProduct_id(product_id);
        if(product == null) throw new IllegalStateException("Product with the ID "+product_id+" doesn't exist.");
        return product;
    }

    public void categoryChecker(List<Long> category_ids){
        for(Long category_id : category_ids){
            Category category = categoryRepository.findByCategory_id(category_id);
            if(category == null) throw new IllegalStateException("Category with the ID "+category_id+" doesn't exist.");
        }
    }

    public void categoryCleaner(Product product, List<Long> oldCategoryIds){
        for (Long category_id: oldCategoryIds){
            Category category = categoryRepository.findByCategory_id(category_id);
            mongoTemplate.update(Category.class)
                    .matching(where("category_id").is(category.getCategory_id()))
                    .apply(new Update().pull("productList", product))
                    .first();
        }
    }

    public Product categoryUpdater(Product product){
        for (Long category_id : product.getCategory_ids()){
            Category category = categoryRepository.findByCategory_id(category_id);
            mongoTemplate.save(product);
            mongoTemplate
                    .update(Category.class)
                    .matching(where("category_id").is(category.getCategory_id()))
                    .apply(new Update().push("productList", product))
                    .first();
        }
        return product;
    }

    @Transactional
    public Product updateProduct(long product_id,ProductDTO productDTO) {
        Product idProduct = getProductById(product_id);
        categoryChecker(productDTO.getCategory_ids());
        activeCategoryChecker(productDTO.getCategory_ids());
        Product product = modelMapper.map(productDTO, Product.class);
        product.setVariationList(idProduct.getVariationList());
        product.setProduct_id(idProduct.getProduct_id());
        categoryCleaner(idProduct, idProduct.getCategory_ids());
        product = categoryUpdater(product);
        return productRepository.save(product);
    }

    public void deleteProduct(long product_id) {
        Product product = getProductById(product_id);
        deleteVariations(product_id);
        productRepository.delete(product);
    }

    void deleteVariations(long product_id) {
        variationRepository.deleteByProduct_id(product_id);
    }

    public void setActiveToFalse(Long category_id){
        List<Product> products = productRepository.findByCategory_ids(category_id);
        for(Product product : products){
            if(product.getCategory_ids().size()==1) product.setActive(false);
            product.getCategory_ids().remove(category_id);
            productRepository.save(product);
        }
    }
    public void activeCategoryChecker(List<Long> category_ids){
        for(Long category_id : category_ids){
            Category category = categoryRepository.findByCategory_id(category_id);
            if(!category.getActive()) throw new IllegalStateException("Category with the ID "+category_id+" is not active.");
        }

    }
}
