package com.ninjaone.dundie_awards.validator;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UUIDValidatorTest {

    private final UUIDValidator validator = new UUIDValidator();

    @Test
    void isValid_returnsTrueForValidUUID() {
        assertThat(validator.isValid(UUID.randomUUID(), null)).isTrue();
    }

    @Test
    void isValid_returnsFalseForNullUUID() {
        assertThat(validator.isValid(null, null)).isFalse();
    }
}
