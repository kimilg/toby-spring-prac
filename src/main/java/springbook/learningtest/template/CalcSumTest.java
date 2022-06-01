package springbook.learningtest.template;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;
    
    @Before
    public void setUp(){
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }
    
    @Test
    public void sumOfNumbers() throws IOException {
        assertThat(this.calculator.calcSum(this.numFilepath), is(10));
    }
    
    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(this.calculator.calcMultiply(this.numFilepath), is(24));
    }
    
    @Test
    public void concatenateNumbers() throws IOException {
        assertThat(this.calculator.concatenateStrings(this.numFilepath), is("1234"));
    }
}



