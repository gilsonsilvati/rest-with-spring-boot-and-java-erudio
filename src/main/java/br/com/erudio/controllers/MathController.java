package br.com.erudio.controllers;

import br.com.erudio.exceptions.UnsupportedMathOperationException;
import br.com.erudio.math.SimpleMath;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static br.com.erudio.converters.NumberConverter.convertToDouble;
import static br.com.erudio.converters.NumberConverter.isNotNumeric;

@RestController
public class MathController {

    private SimpleMath math = new SimpleMath();

    @RequestMapping(value = "sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        if (isNotNumeric(numberOne) || isNotNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return math.sum(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping(value = "subtraction/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double subtraction(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        if (isNotNumeric(numberOne) || isNotNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return math.subtraction(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping(value = "multiplication/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double multiplication(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        if (isNotNumeric(numberOne) || isNotNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return math.multiplication(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping(value = "division/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double division(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        if (isNotNumeric(numberOne) || isNotNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return math.division(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping(value = "average/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double average(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) {
        if (isNotNumeric(numberOne) || isNotNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return math.average(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping(value = "square-root/{number}", method = RequestMethod.GET)
    public Double squareRoot(@PathVariable("number") String number) {
        if (isNotNumeric(number)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }

        return math.squareRoot(convertToDouble(number));
    }
}
