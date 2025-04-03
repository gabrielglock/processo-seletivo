package com.desafio.agenda.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) return false;


        cpf = cpf.replaceAll("[^\\d]", "");


        if (cpf.length() != 11) return false;


        if (cpf.matches("(\\d)\\1{10}")) return false;
//       LOGICA DE VALIDACAO DE CPF
        try {
            int sum = 0;

            for (int i = 0; i < 9; i++)
                sum += (cpf.charAt(i) - '0') * (10 - i);
            int firstCheck = 11 - (sum % 11);
            if (firstCheck >= 10) firstCheck = 0;
            if (firstCheck != (cpf.charAt(9) - '0')) return false;


            sum = 0;
            for (int i = 0; i < 10; i++)
                sum += (cpf.charAt(i) - '0') * (11 - i);
            int secondCheck = 11 - (sum % 11);
            if (secondCheck >= 10) secondCheck = 0;
            return secondCheck == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}

