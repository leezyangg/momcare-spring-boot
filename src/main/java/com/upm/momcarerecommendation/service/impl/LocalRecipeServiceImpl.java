package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import com.upm.momcarerecommendation.domain.entity.RecipeEntity;
import com.upm.momcarerecommendation.mapper.Mapper;
import com.upm.momcarerecommendation.domain.repository.RecipeRepository;
import com.upm.momcarerecommendation.service.LocalRecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocalRecipeServiceImpl implements LocalRecipeService {
    private final RecipeRepository recipeRepository;
    private final Mapper<RecipeApiResponse.Recipe, RecipeEntity> recipeMapper;
    public LocalRecipeServiceImpl(RecipeRepository recipeRepository, Mapper<RecipeApiResponse.Recipe, RecipeEntity> recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }


    @Override
    @Transactional
    public List<RecipeEntity> findRecipesByCriteria(Map<String, Object> criteria) {
        // dynamic query local database using JPA Criteria API
        return recipeRepository.findRecipesByCriteria(criteria);
    }

    @Override
    public void saveRecipes(List<RecipeEntity> recipeEntities) {
        recipeRepository.saveAll(recipeEntities);
    }

    @Override
    @Transactional
    public void processAndSaveRecipes(RecipeApiResponse recipeApiResponse) {
        List<RecipeEntity> recipeEntities = recipeApiResponse.getHits()
                .stream()
                .map(RecipeApiResponse.Hit::getRecipe)
                .map(recipeMapper::mapToEntity)
                .filter(recipeEntity -> !recipeRepository.existsByLabel(recipeEntity.getLabel()))
                .collect(Collectors.toList());
        saveRecipes(recipeEntities);
    }









/*    @Override
    @Transactional
    public void processAndSaveRecipes(RecipeApiResponse recipeApiResponse) {
        List<RecipeEntity> recipeEntities = recipeApiResponse.getHits()
                .stream()
                .map(RecipeApiResponse.Hit::getRecipe)
                .map(recipe -> {
                    RecipeEntity recipeEntity = recipeMapper.mapToEntity(recipe);
                    if (!recipeRepository.existsByLabel(recipeEntity.getLabel())) { saveImageAndSetPath(recipeEntity); }
                    return recipeEntity;
                })
                .collect(Collectors.toList());
        saveRecipes(recipeEntities);
    }

    private void saveImageAndSetPath(RecipeEntity recipeEntity) {
        String imageUrl = recipeEntity.getImage();
        String imageName = recipeEntity.getLabel();
        String savedImagePath = imageStorageService.saveImage(imageUrl, imageName)
                .block(); // This blocks until the image is saved, TODO: make it async
        recipeEntity.setImage(savedImagePath);
    }*/
}
