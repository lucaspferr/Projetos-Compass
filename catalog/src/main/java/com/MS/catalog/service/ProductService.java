package com.MS.catalog.service;

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
        //
        product.setProduct_id(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        List<Long> category_id = product.getCategory_ids();
        System.out.println("AQUI: " + category_id);
        Boolean checker = false;
        for(Long id : category_id){
            Category category = categoryRepository.findByCategory_id(id);
            checker = category.getActive();
            if(checker){
                mongoTemplate.save(product);
                mongoTemplate
                        .update(Category.class)
                        .matching(where("category_id").is(category.getCategory_id()))
                        .apply(new Update().push("productList", product))
                        .first();
            }
        }if(checker) return productRepository.save(product);
        else throw new IllegalStateException("Categoria não está ativa");
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

    public List<ProductDTO> getByCategoryId(long category_id){
        return listDTO(productRepository.findByCategory_ids(category_id));
    }

    public ProductDTO normalToDTO(Product product){
        long product_id = product.getProduct_id();
        product.setVariationList(variationRepository.findByProduct_id(product_id));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    public Product getProductById(long product_id) {
        return this.productRepository.findByProduct_id(product_id);
    }

    @Transactional
    public Product updateProduct(long product_id,ProductDTO productDTO) {

        Product idProduct = getProductById(product_id);
        if(idProduct != null){
            Product product = modelMapper.map(productDTO, Product.class);
            product.setProduct_id(idProduct.getProduct_id());
            return productRepository.save(product);
        }else throw new IllegalStateException("Houve um erro com o update de dados.");
    }

    public void deleteProduct(long product_id) {
        try{
            Product product = productRepository.findByProduct_id(product_id);
            deleteVariations(product_id);
            productRepository.delete(product);
        }catch (Exception e){throw new IllegalStateException("Id "+product_id+" não encontrado.");}
    }

    public void deleteVariations(long product_id) {
        try{variationRepository.deleteByProduct_id(product_id);}
        catch(Exception e){throw new IllegalStateException("Id "+product_id+" não encontrado.");}
    }

    public void setActiveToFalse(Long category_id){
        List<Product> products = productRepository.findByCategory_ids(category_id);
        System.out.println(products);
        for(Product product : products){
            product.setActive(false);
            System.out.println(product);
            System.out.println(product);
            product.getCategory_ids().remove(category_id);
            productRepository.save(product);
        }
    }
}
