/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserexercise;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserExercise {

    public static enum TokenType {
        NUMBER("-?[0-9]+"), BINARYOP("[*|/|+|-]"), WHITESPACE("[ \t\f\r\n]+"), PARENTHESES("[(|)]");

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class Token {

        public TokenType type;
        public String data;
        public int priority;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }
        
        public Token(TokenType type, char data) {
            this(type, String.valueOf(data));
        }
        @Override
        public String toString() {
            if(type != TokenType.BINARYOP){
                return "[" + type.name() + ": " + data + "]";
            }
            else{
                return "[" + type.name() + ": " + data + "; Priority : "+ this.priority+ "]";
            }
            
        }
    }

    
    
    public static void printTokenList(ArrayList<Token> tokenList){
        tokenList.stream().forEach((token) -> {
            System.out.println(token);
        });
        System.out.println("---------------------------------------------------");
    }
    

    public static void main(String args[]) throws ParserException {
        String input = "(3+2*(5+6))-(4+2123*3)";
        ArrayList<Token> tokens = Lexer.lex(input);
        Node<Token> tree = null;
        try {
            tree = Parser.parse(tokens);
        } catch (ParserException ex) {
            Logger.getLogger(ParserExercise.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        
        try {
            System.out.println(input + " = " + Parser.evaluate(tree));
        } catch (ParserException ex) {
            Logger.getLogger(ParserExercise.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

}
