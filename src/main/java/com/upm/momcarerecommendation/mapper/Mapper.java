package com.upm.momcarerecommendation.mapper;

public interface Mapper<A, B> {

    B mapToEntity(A a);
    A mapToModel(B b);

}
