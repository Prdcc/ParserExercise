/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserexercise;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import parserexercise.ParserExercise.*;

/**
 *
 * @author Enrico
 */
public class Lexer {

    private final static char[] binaryOps = {'+', '-', '*', '/'};

    public static ArrayList<ParserExercise.Token> lexRegex(String input) {
        ArrayList<ParserExercise.Token> tokens = new ArrayList<>();
        StringBuilder tokenPatternsBuffer = new StringBuilder();
        for (ParserExercise.TokenType tokenType : ParserExercise.TokenType.values()) {
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        }
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            for (ParserExercise.TokenType tokenType : ParserExercise.TokenType.values()) {
                if (tokenType != ParserExercise.TokenType.WHITESPACE) {
                    if (matcher.group(tokenType.name()) != null) {
                        tokens.add(new ParserExercise.Token(tokenType, matcher.group(tokenType.name())));
                    }
                }
            }
        }
        return tokens;
    }

    public static boolean isBinaryOp(char character) {
        for (char op : binaryOps) {
            if (op == character) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<ParserExercise.Token> lex(String input) throws ParserException {
        ArrayList<ParserExercise.Token> tokens = new ArrayList<>();
        TokenType context = TokenType.WHITESPACE;
        StringBuilder builder = new StringBuilder();
        boolean hasDecimalPoint = false;
        
        for (int position = 0; position < input.length(); position++) {
            char currentChar = input.charAt(position);
            switch (context) {
                case WHITESPACE:
                    if (Character.isDigit(currentChar)) {
                        builder.append(currentChar);
                        context = TokenType.NUMBER;
                    } 
                    else if (isBinaryOp(currentChar)) {
                        tokens.add(new Token(TokenType.BINARYOP, currentChar));
                    } 
                    else if (currentChar == '(' || currentChar == ')') {
                        tokens.add(new Token(TokenType.PARENTHESES, currentChar));
                    }
                    else{
                        throw new ParserException(ParserException.ExceptionCode.UNRECOGNISEDCHARACTER);
                    }

                    break;
                case NUMBER:
                    if (Character.isDigit(currentChar)) {
                        builder.append(currentChar);
                    } 
                    else if(currentChar == '.'){
                        builder.append(currentChar);
                    }
                    else if (isBinaryOp(currentChar)) {
                        hasDecimalPoint = false;
                        tokens.add(new Token(TokenType.NUMBER, builder.toString()));
                        builder.setLength(0);
                        tokens.add(new Token(TokenType.BINARYOP, currentChar));
                        context = TokenType.WHITESPACE;
                    } 
                    else if (currentChar == '(' || currentChar == ')') {
                        hasDecimalPoint = false;
                        tokens.add(new Token(TokenType.NUMBER, builder.toString()));
                        builder.setLength(0);
                        tokens.add(new Token(TokenType.PARENTHESES, currentChar));
                        context = TokenType.WHITESPACE;
                    }
                    else{
                        throw new ParserException(ParserException.ExceptionCode.UNRECOGNISEDCHARACTER);
                    }
                    
                    break;

                default:
                    throw new UnsupportedOperationException("Not supported yet");
            }
        }
        if(builder.length() > 0){
            tokens.add(new Token(TokenType.NUMBER, builder.toString()));
        }
        return tokens;
    }

}
