package com.hoya.core.controller;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;

@Data
public class TestModel {

    public TestModel() {

    }

    public TestModel(String s, Integer i) {
        this.s = s;
        this.i = i;
    }

    @NotNull
    private String s;

    @NotNull
    private Integer i;

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
