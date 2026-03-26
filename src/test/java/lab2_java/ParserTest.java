package lab2_java;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest 
{

    private double evaluate(String expression) throws EvaluationException 
    {
        return new Parser(expression, Collections.emptyMap()).parse();
    }

    private double evaluate(String expression, Map<String, Double> vars) throws EvaluationException 
    {
        return new Parser(expression, vars).parse();
    }

    @Test
    void testBasicArithmetic() throws EvaluationException
    {
        assertEquals(4.0, evaluate("2 + 2"), 0.0001);
        assertEquals(14.0, evaluate("2 + 3 * 4"), 0.0001);
        assertEquals(20.0, evaluate("(2 + 3) * 4"), 0.0001);
        assertEquals(0.5, evaluate("1 / 2"), 0.0001);
    }

    @Test
    void testPowerAndUnary() throws EvaluationException 
    {
        assertEquals(8.0, evaluate("2 ^ 3"), 0.0001);
        assertEquals(-2.0, evaluate("-2"), 0.0001);
        assertEquals(2.0, evaluate("--2"), 0.0001);
        assertEquals(-6.0, evaluate("-2 * 3"), 0.0001);
    }

    @Test
    void testFunctions() throws EvaluationException
    {
        assertEquals(2.0, evaluate("sqrt(4)"), 0.0001);
        assertEquals(0.0, evaluate("sin(0)"), 0.0001);
        assertEquals(1.0, evaluate("cos(0)"), 0.0001);
    }

    @Test
    void testVariables() throws EvaluationException 
    {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 10.0);
        vars.put("y", 5.0);
        vars.put("PI", Math.PI);
        
        assertEquals(15.0, evaluate("x + y", vars), 0.0001);
        assertEquals(50.0, evaluate("x * y", vars), 0.0001);
        assertEquals(-1.0, evaluate("cos(PI)", vars), 0.0001); 
    }

    @Test
    void testExceptions() 
    {
        EvaluationException ex = assertThrows(EvaluationException.class, () -> evaluate("(2 + 2"));
        assertTrue(ex.getMessage().contains("Ожидалась закрывающая скобка"));

        ex = assertThrows(EvaluationException.class, () -> evaluate("2 + @"));
        assertTrue(ex.getMessage().contains("Неожиданный символ"));

        ex = assertThrows(EvaluationException.class, () -> evaluate("2 + a"));
        assertTrue(ex.getMessage().contains("Неизвестная переменная"));
        
        ex = assertThrows(EvaluationException.class, () -> evaluate("5 / 0"));
        assertTrue(ex.getMessage().contains("Деление на ноль"));
    }
}