package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.dto.RecipeApiResponse;
import com.upm.momcarerecommendation.service.RecipeApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class EdamamRecipeApiServiceImpl implements RecipeApiService {

    private final WebClient webClient;

    @Value("${edamam.api.base-url}")
    private String baseUrl;
    @Value("${edamam.api.app-id}")
    private String appId;
    @Value("${edamam.api.app-key}")
    private String appKey;

    public EdamamRecipeApiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<RecipeApiResponse> getRecipes(Map<String, List<String>> foodQueryMap) {
        MultiValueMap<String, String> foodQueryParam = getStringStringMultiValueMap();

        for (Map.Entry<String, List<String>> entry : foodQueryMap.entrySet()) {
            for (String value : entry.getValue()) {
                foodQueryParam.add(entry.getKey(), value);
            }
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParams(foodQueryParam);

        String url = uriBuilder.build().toString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(RecipeApiResponse.class);
    }

    // set the necessary param into the MultiValueMap as the queryParam
    private MultiValueMap<String, String> getStringStringMultiValueMap() {
        MultiValueMap<String, String> foodQueryParam = new LinkedMultiValueMap<>();

        // remember to insert the API_KEY & APP_ID from the Edamam API &
        // put inside the application.yml or application.properties
        foodQueryParam.add("app_id", appId);
        foodQueryParam.add("app_key", appKey);

        // by default both of these field are set as this
        foodQueryParam.add("type", "any");
        foodQueryParam.add("random", "true");

        // these are the fields wish to be extracted from the api
        foodQueryParam.add("field", "label");
        foodQueryParam.add("field", "image");
        foodQueryParam.add("field", "source");
        foodQueryParam.add("field", "url");
        foodQueryParam.add("field", "ingredientLines");
        foodQueryParam.add("field", "calories");
        foodQueryParam.add("field", "totalNutrients");
        foodQueryParam.add("field", "totalTime");
        foodQueryParam.add("field", "totalWeight");
        foodQueryParam.add("field", "cautions");
        foodQueryParam.add("field", "yield");
        foodQueryParam.add("field", "dietLabels");
        foodQueryParam.add("field", "healthLabels");
        return foodQueryParam;
    }
}