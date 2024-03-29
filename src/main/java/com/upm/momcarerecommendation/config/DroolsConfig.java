package com.upm.momcarerecommendation.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {
    private static final String DISEASE_DETECTION_RULES_PATH = "rules/diseaseDetection.drl";
    private static final String FOOD_RECOMMENDATION_RULES_PATH = "rules/foodRecommendation.drl";
    private static final String EXERCISE_RECOMMENDATION_RULES_PATH = "rules/exerciseRecommendation.drl";
    private static final KieServices kieServices = KieServices.Factory.get();

    @Bean
    public KieContainer dietKieContainer() {
        return getKieContainer(FOOD_RECOMMENDATION_RULES_PATH);
    }

    @Bean
    public KieContainer exerciseKieContainer() {
        return getKieContainer(EXERCISE_RECOMMENDATION_RULES_PATH);
    }


    private KieContainer getKieContainer(String rulePath) {

        // Get Base Kie Container
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(DISEASE_DETECTION_RULES_PATH));
        kieFileSystem.write(ResourceFactory.newClassPathResource(rulePath));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}