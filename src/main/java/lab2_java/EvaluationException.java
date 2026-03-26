package lab2_java;

/**
 * Исключение, выбрасываемое при ошибках парсинга и вычисления математического выражения.
 */
public class EvaluationException extends Exception 
{
	/**
     * Конструктор исключения с сообщением об ошибке.
     *
     * @param message подробное сообщение об ошибке.
     */
    public EvaluationException(String message) 
    {
        super(message);
    }
}