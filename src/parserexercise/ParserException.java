/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserexercise;

/**
 *
 * @author Enrico
 */
public class ParserException extends Exception {

    /**
     * Creates a new instance of <code>ParserException</code> without detail
     * message.
     */
    public static enum ExceptionCode{
        PARENTHESESWRONG("Mismatched parentheses"), TOOMANYNUMBERS("Unexpected number"), UNSUPPORTEDBINARYOP("Unsupported binary operation");
        String message;
        private ExceptionCode(String msg){
            message = msg;
        }
    }
    public ParserException() {
    }

    /**
     * Constructs an instance of <code>ParserException</code> with the specified
     * detail message.
     *
     * @param exc
     */
    public ParserException(ExceptionCode exc) {
        super(exc.message);
    }
}
