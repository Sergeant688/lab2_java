package lab2_java;

import java.util.Map;

public class Parser 
{
    private final String expression;
    private final Map<String, Double> variables;
    private int pos = -1;
    private int ch;

    public Parser(String expression, Map<String, Double> variables) 
    {
        this.expression = expression;
        this.variables = variables;
    }

    
    private void nextChar() 
    {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) 
    {
        while (ch == ' ') nextChar();
        if (ch == charToEat) 
        {
            nextChar();
            return true;
        }
        return false;
    }

    
    public double parse() throws Exception 
    {
        nextChar();
        double x = parseExpression();
        return x;
    }

    private double parseExpression() throws Exception 
    {
        double x = parseTerm();
       
        if (eat('+')) x = x + parseTerm();
        else if (eat('-')) x = x - parseTerm();
        
        return x;
    }

    private double parseTerm() throws Exception 
    {
        double x = parseFactor();
        if (eat('*')) x = x * parseFactor(); 
        else if (eat('/')) 
        {
            x = x / parseFactor(); 
        }
        return x;
    }

    private double parseFactor() throws Exception 
    {

        double x = 0;
        int startPos = this.pos;
        
        if (eat('(')) 
        { 
            x = parseExpression();
            eat(')'); 
        } 
        else if (ch >= '0' && ch <= '9') 
        { 
            while (ch >= '0' && ch <= '9') nextChar();
            x = Double.parseDouble(expression.substring(startPos, this.pos));
        } 
        else 
        {
            throw new Exception("Ошибка парсинга"); 
        }


        return x;
    }
}