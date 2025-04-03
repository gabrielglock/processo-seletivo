package com.desafio.agenda.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CnpjValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {
    String message() default "CNPJ inválido. Verifique se está corretamente formatado e válido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
