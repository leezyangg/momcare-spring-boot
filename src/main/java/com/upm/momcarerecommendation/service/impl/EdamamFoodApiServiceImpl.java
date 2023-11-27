package com.upm.momcarerecommendation.service.impl;

import com.upm.momcarerecommendation.service.EdamamFoodApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.util.Map;

@Service
public class EdamamFoodApiServiceImpl implements EdamamFoodApiService {

    private final WebClient webClient;

    @Value("${edamam.api.base-url}")
    private String baseUrl;
    @Value("${edamam.api.app-id}")
    private String appId;
    @Value("${edamam.api.app-key}")
    private String appKey;

    public EdamamFoodApiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public String getFoodRecipe(Map<String, String> foodQueryMap) {
        MultiValueMap<String, String> foodQueryParam = getStringStringMultiValueMap();

        for (Map.Entry<String, String> entry : foodQueryMap.entrySet()) {
            foodQueryParam.add(entry.getKey(), entry.getValue());
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParams(foodQueryParam);

        String url = uriBuilder.build().toString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // set the necessary param into the MultiValueMap as the queryParam
    private MultiValueMap<String, String> getStringStringMultiValueMap() {
        MultiValueMap<String, String> foodQueryParam = new LinkedMultiValueMap<>();

        // remember to insert the API_KEY & APP_ID from the Edamam API &
        // put inside the application.yml
        foodQueryParam.add("app_id", appId);
        foodQueryParam.add("app_key", appKey);

        // by default both of these field are set as this
        foodQueryParam.add("type", "any");
        foodQueryParam.add("random", "true");

        // this is the field wish to extract from the api
        foodQueryParam.add("field", "label");
        foodQueryParam.add("field", "image");
        foodQueryParam.add("field", "url");
        foodQueryParam.add("field", "ingredientLines");
        foodQueryParam.add("field", "calories");
        foodQueryParam.add("field", "totalNutrients");
        return foodQueryParam;
    }
}