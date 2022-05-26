package com.MS.catalog.service;


import com.MS.catalog.repository.ProductRepository;
import com.MS.catalog.repository.VariationRepository;
import com.MS.catalog.model.DTO.VariationDTO;
import com.MS.catalog.model.Product;
import com.MS.catalog.model.Variation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class VariationService{

    @Autowired
    private VariationRepository variationRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private ProductRepository productRepository;

    ModelMapper modelMapper = new ModelMapper();

    public Variation createVariation(VariationDTO variationDTO){
        productIdChecker(variationDTO.getProduct_id());
        Variation variation = modelMapper.map(variationDTO, Variation.class);
        variation.setVariant_id(sequenceGeneratorService.generateSequence(Variation.SEQUENCE_NAME));
        variation = productUpdater(variation);
        return variationRepository.save(variation);
    }

    public Variation productUpdater(Variation variation){
        Product product = productRepository.findByProduct_id(variation.getProduct_id());
        mongoTemplate.save(variation);
        mongoTemplate.update(Product.class)
                .matching(where("product_id").is(product.getProduct_id()))
                .apply(new Update().push("variationList", variation))
                .first();
        return variation;
    }

    public void productCleaner(Variation variation, Long oldProductId){
        Product product = productRepository.findByProduct_id(oldProductId);
        mongoTemplate.update(Product.class)
                .matching(where("product_id").is(product.getProduct_id()))
                .apply(new Update().pull("variationList", variation))
                .first();
    }

    @Transactional
    public Variation updateVariation(long variant_id, VariationDTO variationDTO){
        variationIdChecker(variant_id);
        productIdChecker(variationDTO.getProduct_id());
        Variation idVariation = variationRepository.findByVariant_id(variant_id);
        Variation variation = modelMapper.map(variationDTO, Variation.class);
        variation.setVariant_id(idVariation.getVariant_id());
        productCleaner(idVariation, idVariation.getProduct_id());
        variation = productUpdater(variation);
        return variationRepository.save(variation);
    }

    public void deleteVariation(long variant_id){
        variationIdChecker(variant_id);
        variationRepository.deleteByVariant_id(variant_id);
    }

    public void productIdChecker(Long product_id){
        if(productRepository.findByProduct_id(product_id)==null) throw new IllegalStateException("Product with the ID "+product_id+" doesn't exist.");
    }

    public void variationIdChecker(long variant_id){
        if(variationRepository.findByVariant_id(variant_id)==null) throw new IllegalStateException("Variant with the ID "+variant_id+" doesn't exist.");
    }

    public VariationDTO variationFeingFinder(long variant_id){
        variationIdChecker(variant_id);
        Variation variation = variationRepository.findByVariant_id(variant_id);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        VariationDTO variationDTO = modelMapper.map(variation, VariationDTO.class);
        return variationDTO;
    }
}
