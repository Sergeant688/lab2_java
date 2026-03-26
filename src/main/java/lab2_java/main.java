package lab2_java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main 
{

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---Калькулятор математических выражений---");
        System.out.println("Поддерживаются: +, -, *, /, ^, (), функции (sin, cos, sqrt, tan) и переменные.");
        System.out.println("Для выхода введите 'exit'.\n");


        while (true) 
        {
            System.out.print("Введите выражение: ");
            String expression = scanner.nextLine().trim();

            if (expression.equalsIgnoreCase("exit")) {
                System.out.println("Завершение работы программы.");
                break;
            }
            
            if (expression.isEmpty()) continue;

            try 
            {
                Set<String> variableNames = extractVariables(expression);
                Map<String, Double> variables = new HashMap<>();

                for (String varName : variableNames) 
                {
                    System.out.print("  Введите значение для переменной '" + varName + "': ");
                    while (!scanner.hasNextDouble()) 
                    {
                        System.out.println("  Ошибка! Введите корректное число (например, 2,5 или 2.5 в зависимости от локали ОС): ");
                        scanner.next();
                    }
                    variables.put(varName, scanner.nextDouble());
                }
                if (!variableNames.isEmpty()) 
                {
                    scanner.nextLine(); 
                }

                Parser parser = new Parser(expression, variables);
                double result = parser.parse();

                System.out.println("Результат: " + result + "\n");

            } 
            catch (EvaluationException e) 
            {
            	System.err.println("Ошибка в выражении: " + e.getMessage() + "\n");
            }
        
            catch (Exception e) 
            {
            	System.err.println("Критическая ошибка: " + e.getMessage() + "\n");
            }
        }
    
        scanner.close();

    }

    public static Set<String> extractVariables(String expression) 
    {
        Set<String> variables = new HashSet<>();
        Pattern pattern = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
        Matcher matcher = pattern.matcher(expression);
        
        while (matcher.find()) 
        {
            String var = matcher.group();
            if (!Arrays.asList("sin", "cos", "tan", "sqrt").contains(var.toLowerCase())) 
            {
                variables.add(var);
            }
        }
        return variables;
    }
}