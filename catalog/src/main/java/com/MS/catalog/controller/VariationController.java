package com.MS.catalog.controller;


import com.MS.catalog.model.DTO.VariationDTO;
import com.MS.catalog.model.Variation;
import com.MS.catalog.service.VariationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/variations")
public class VariationController{

    @Autowired
    private VariationService variationService;

    @PostMapping
    public ResponseEntity<?> createVariation(@RequestBody @Valid VariationDTO variationDTO){
        Variation variation = variationService.createVariation(variationDTO);
        if(variation==null) return ResponseEntity.badRequest().body("Não foi possivel criar a variação.");
        else return ResponseEntity.ok().body(variation);
    }

    @PutMapping("/{variant_id}")
    public ResponseEntity<?> updateVariation(@PathVariable long variant_id, @RequestBody @Valid VariationDTO variationDTO){
        Variation variation = variationService.updateVariation(variant_id, variationDTO);
        if(variation==null) return ResponseEntity.badRequest().body("Update de dados não foi possível.");
        else return ResponseEntity.ok().body(variation);
    }

    @DeleteMapping("/{variant_id}")
    public ResponseEntity<?> deleteVariation(@PathVariable long variant_id){
        if(variationService.deleteVariation(variant_id)) return ResponseEntity.ok().body("Registro com o ID "+variant_id+" deletado com sucesso!");
        else return ResponseEntity.badRequest().body("Registro com o ID "+variant_id+" não existente.");
    }

    @GetMapping("/feign/{variant_id}")
    public VariationDTO getVariantFeign(@PathVariable long variant_id){
        return variationService.variationFeingFinder(variant_id);
    }

}
