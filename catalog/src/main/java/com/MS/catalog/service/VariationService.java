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
        Variation variation = modelMapper.map(variationDTO, Variation.class);
        variation.setVariant_id(sequenceGeneratorService.generateSequence(Variation.SEQUENCE_NAME));
        if(productIdChecker(variationDTO.getProduct_id())){
            Product product = productRepository.findByProduct_id(variationDTO.getProduct_id());
            mongoTemplate.save(variation);
            mongoTemplate.update(Product.class)
                    .matching(where("product_id").is(product.getProduct_id()))
                    .apply(new Update().push("variationList", variation))
                    .first();
            return variationRepository.save(variation);
        }else throw new IllegalStateException("Produto com o ID "+variationDTO.getProduct_id()+" não encontrado");
    }

    @Transactional
    public Variation updateVariation(long variant_id, VariationDTO variationDTO){
        Variation idVariation = variationRepository.findByVariant_id(variant_id);
        if(idVariation != null) {
            Variation variation = modelMapper.map(variationDTO, Variation.class);
            variation.setVariant_id(idVariation.getVariant_id());
            return variationRepository.save(variation);
        }else throw new IllegalStateException("Variação com o ID "+variant_id+" não existe");
    }

    public boolean deleteVariation(long variant_id){
        if(variationIdChecker(variant_id)){
            variationRepository.deleteByVariant_id(variant_id);
            return true;
        }return false;
    }

    public boolean productIdChecker(long product_id){
        if(productRepository.findByProduct_id(product_id)==null) return false;
        return true;
    }

    public boolean variationIdChecker(long variant_id){
        if(variationRepository.findByVariant_id(variant_id)==null) return false;
        return true;
    }

    public VariationDTO variationFeingFinder(long variant_id){
        Variation variation = variationRepository.findByVariant_id(variant_id);
        if(variation==null) throw new IllegalStateException("Varição com o ID "+variant_id+" não existe.");
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        VariationDTO variationDTO = modelMapper.map(variation, VariationDTO.class);
        return variationDTO;
    }
}
