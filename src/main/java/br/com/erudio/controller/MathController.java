package br.com.erudio.controller;

import br.com.erudio.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MathController {

    @RequestMapping(value = "sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        if (isNotNumeric(numberOne) || isNotNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    private boolean isNotNumeric(String value) {
        if (Objects.isNull(value)) {
            return true;
        }

        var strNumber = getFormatted(value);

        var isNumeric = strNumber.matches("[-+]?\\d*\\.?\\d+");

        return !isNumeric;
    }

    private Double convertToDouble(String value) {
        return Double.parseDouble(getFormatted(value));
    }

    private String getFormatted(String value) {
        return value.replace(",", ".");
    }
}
