package springbook.learningtest.template;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;

public class CalcSumTest {
    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("/numbers.txt").getPath());
        assertThat(sum, is(10));
    }
}
