package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.domain.model.RecipeApiResponse;
import com.upm.momcarerecommendation.service.FoodApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class SpoonacularFoodApiServiceImpl implements FoodApiService {
    private WebClient webClient;
    @Value("${spoonacular.api.base-url}")
    private String baseUrl;
    @Value("${spoonacular.api.api-key}")
    private String apiKey;

    public SpoonacularFoodApiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<RecipeApiResponse> getFoodRecipe(Map<String, List<String>> foodQueryMap) {
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

        // remember to insert the API_KEY & APP_ID from the API &
        // put inside the application.yml or application.properties
        foodQueryParam.add("apiKey", apiKey);

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
        foodQueryParam.add("field", "instructionLines");
        foodQueryParam.add("field", "yield");
        foodQueryParam.add("field", "dietLabels");
        foodQueryParam.add("field", "healthLabels");
        return foodQueryParam;
    }
}
