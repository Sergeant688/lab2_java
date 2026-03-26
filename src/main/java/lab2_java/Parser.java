package lab2_java;

import java.util.Map;

/**
 * Класс для парсинга и вычисления математических выражений методом рекурсивного спуска.
 * Поддерживает базовые арифметические операции, скобки, математические функции и переменные.
 */
public class Parser 
{
    private final String expression;
    private final Map<String, Double> variables;
    private int pos = -1;
    private int ch;

    /**
     * Конструктор парсера.
     *
     * @param expression строка с математическим выражением.
     * @param variables  cловарь значений переменных. Может быть пустой, если переменных нет.
     */
    public Parser(String expression, Map<String, Double> variables) 
    {
        this.expression = expression;
        this.variables = variables;
    }

    /**
     * Считывает следующий символ из выражения, игнорируя пробелы.
     */
    private void nextChar() 
    {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    /**
     * Проверяет, совпадает ли текущий символ с ожидаемым, и если да, съедает его.
     *
     * @param charToEat ожидаемый символ.
     * @return true, если символ совпал и был пропущен, иначе false.
     */
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

    /**
     * Запускает процесс парсинга и вычисления выражения.
     *
     * @return результат вычисления выражения.
     * @throws EvaluationException если выражение содержит синтаксические ошибки или неизвестные элементы.
     */
    public double parse() throws EvaluationException
    {
        nextChar();
        double x = parseExpression();       
        return x;
    }

    private double parseExpression() throws EvaluationException 
    {
        double x = parseTerm();
        for (;;) 
        {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() throws EvaluationException 
    {
        double x = parseFactor();
        for (;;) 
        {
            if (eat('*')) x *= parseFactor(); 
            else if (eat('/')) 
            {
                double divisor = parseFactor();
                if (divisor == 0) throw new EvaluationException("Деление на ноль!");
                x /= divisor;
            }
            else return x;
        }
    }

    private double parseFactor() throws EvaluationException 
    {
    	if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();
       
        double x;
        int startPos = this.pos;
        
        if (eat('(')) 
        {
            x = parseExpression();
            if (!eat(')')) throw new EvaluationException("Ожидалась закрывающая скобка ')'");
        } 
        else if ((ch >= '0' && ch <= '9') || ch == '.') 
        {
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(expression.substring(startPos, this.pos));
        } 
        else if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') 
        {
            while (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') nextChar();
            String name = expression.substring(startPos, this.pos);
            
            if (eat('(')) 
            {
                x = parseExpression();
                if (!eat(')')) throw new EvaluationException("Ожидалась закрывающая скобка ')' после функции " + name);
                
                switch (name.toLowerCase()) 
                {
                    case "sqrt": x = Math.sqrt(x); break;
                    case "sin": x = Math.sin(x); break;
                    case "cos": x = Math.cos(x); break;
                    case "tan": x = Math.tan(x); break;
                    default: throw new EvaluationException("Неизвестная функция: " + name);
                }
            } 
            else 
            {
                if (variables != null && variables.containsKey(name)) 
                {
                    x = variables.get(name);
                } 
                
                else throw new EvaluationException("Неизвестная переменная: " + name);
            }
        }
        else throw new EvaluationException("Неожиданный символ: " + (char) ch);

        if (eat('^')) x = Math.pow(x, parseFactor());

        return x;
    }
}