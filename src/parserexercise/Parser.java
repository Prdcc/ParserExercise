package parserexercise;

import java.util.ArrayList;
import java.util.Arrays;
import parserexercise.ParserExercise.*;

/**
 *
 * @author Enrico
 */
public class Parser {

    public static final int MAX_PRIORITY = 2;
    public static final int MIN_PRIORITY = 1;

    public static enum BinaryOperation {
        PLUS("+", 2), MINUS("-", 2), TIMES("*", 1), DIVIDED("/", 1);

        String literal;
        int priority;

        private BinaryOperation(String literal, int priority) {
            this.literal = literal;
            this.priority = priority;
        }
    }

    public static int evaluate(Node<Token> tree) throws ParserException {
        int result = 0;
        Token currToken = tree.data;
        if (currToken.type == TokenType.NUMBER) {
            result = Integer.parseInt(currToken.data);
        } else {
            switch (currToken.data.charAt(0)) {
                case '+':
                    result = evaluate(tree.getChild(0)) + evaluate(tree.getChild(1));
                    break;
                case '*':
                    result = evaluate(tree.getChild(0)) * evaluate(tree.getChild(1));
                    break;
                case '-':
                    result = evaluate(tree.getChild(0)) - evaluate(tree.getChild(1));
                    break;
                case '/':
                    result = evaluate(tree.getChild(0)) / evaluate(tree.getChild(1));
                    break;
                default:
                    throw new ParserException(ParserException.ExceptionCode.UNSUPPORTEDBINARYOP);
            }
        }
        return result;
    }

    public static boolean setPriorities(ArrayList<Token> tokenList) {
        int counter = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            Token currToken = tokenList.get(i);
            if (currToken.type == TokenType.PARENTHESES) {
                if (currToken.data.equals("(")) {
                    counter++;
                } else if (--counter < 0) {
                    return false;
                }
                tokenList.remove(currToken);
                i--;
            } else if (currToken.type == TokenType.BINARYOP) {
                currToken.priority = Arrays.stream(BinaryOperation.values())
                        .filter(t -> t.literal.equals(currToken.data))
                        .findFirst().get().priority;
                currToken.priority -= counter * (MAX_PRIORITY - MIN_PRIORITY + 1);
            }
        }

        return counter == 0;
    }

    private static int getFirstOpPosition(ArrayList<Token> tokenList, int maxPriority) throws ParserException {
        int pos = 0;
        boolean isNumber = true;
        int maxEncounteredPriority = maxPriority;
        for (int currPos = tokenList.size() - 1; currPos > -1; currPos--) {
            Token currToken = tokenList.get(currPos);
            if (currToken.type == TokenType.BINARYOP) {
                isNumber = false;
                if (currToken.priority == maxPriority) {
                    pos = currPos;
                    break;
                } else if ((currToken.priority > maxEncounteredPriority) || (maxEncounteredPriority == maxPriority)) {
                    pos = currPos;
                    maxEncounteredPriority = currToken.priority;
                }
            }
        }
        if (isNumber && tokenList.size() != 1) {
            throw new ParserException(ParserException.ExceptionCode.TOOMANYNUMBERS);
        }
        return isNumber ? -1 : pos;
    }

    private static Node<Token> getTree(ArrayList<Token> tokenList, int maxPriority) throws ParserException {
        Node<Token> tree;
        int pos = getFirstOpPosition(tokenList, maxPriority);
        if (pos == -1) {
            tree = new Node<>(tokenList.get(0));
        } else {
            tree = new Node<>(tokenList.get(pos));
            int priority = tokenList.get(pos).priority;
            ArrayList<Token> leftTokens = new ArrayList<>(tokenList.subList(0, pos));
            Node<Token> leftTree = getTree(leftTokens, priority);
            tree.addChild(leftTree);
            ArrayList<Token> rightTokens = new ArrayList<>(tokenList.subList(pos + 1, tokenList.size()));
            Node<Token> rightTree = getTree(rightTokens, priority);
            tree.addChild(rightTree);
        }
        return tree;
    }

    public static Node<Token> parse(ArrayList<Token> tokenList) throws ParserException {
        if (!setPriorities(tokenList)) {
            throw new ParserException(ParserException.ExceptionCode.PARENTHESESWRONG);
        }
        Node<ParserExercise.Token> actionTree = getTree(tokenList, MAX_PRIORITY);

        return actionTree;
    }
}
