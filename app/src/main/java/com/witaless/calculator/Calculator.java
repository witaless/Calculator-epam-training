package com.witaless.calculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Stack;

public class Calculator {

    private static final String EXPONENT = "E";
    private static final String NO_EXPONENT_PATTERN = "#,##0.0##################";
    private static final String EXPONENT_PATTERN = "0.#################E0";
    private static final int NOT_OPERATOR = -1;
    private static final char MINUS = '-';

    public static ResultModel calculate(String input) {
        ResultModel result = new ResultModel();
        result.setResult(Const.STRING_EMPTY);
        result.setError(false);
        ArrayDeque<String> reversePolishNotation = getReversePolishNotation(input);

        if (reversePolishNotation.size() < 1) {
            result.setError(true);
            result.setResult(Const.STRING_EMPTY);

            return result;
        }

        try {
            result.setResult(calculateReversePolishNotationExpression(reversePolishNotation));
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(true);
            result.setResult(Const.STRING_EMPTY);

            return result;
        }

        return result;
    }

    private static ArrayDeque<String> getReversePolishNotation(String infixNotation) {
        ArrayDeque<String> reversePolishNotation = new ArrayDeque<>();
        Stack<Character> stack = new Stack<>();
        String toPush = Const.STRING_EMPTY;
        int countOperand = 0;
        int countOperator = 0;

        for (int i = 0; i < infixNotation.length(); i++) {
            char currentChar = infixNotation.charAt(i);
            if (i == 0 && isMinus(currentChar)) {

                toPush += MINUS;
            } else if (i > 0 && isMinus(currentChar) && !isNotOperator(infixNotation.charAt(i - 1))  && toPush.length() < 1) {

                toPush += MINUS;
            } else {

                if (getPriority(currentChar) == NOT_OPERATOR) {
                    toPush += currentChar;
                } else {
                    reversePolishNotation.add(toPush);
                    countOperand++;
                    toPush = Const.STRING_EMPTY;

                    if (stack.size() < 1) {
                        stack.push(currentChar);
                        countOperator++;
                    } else if (getPriority(currentChar) <= getPriority(stack.peek())) {

                        while (stack.size() > 0) {
                            reversePolishNotation.add(stack.pop() + Const.STRING_EMPTY);
                        }

                        stack.push(currentChar);
                        countOperator++;
                    } else {
                        stack.push(currentChar);
                        countOperator++;
                    }
                }
            }

        }

        if (toPush.length() > 0) {
            reversePolishNotation.add(toPush);
            countOperand++;
        }

        while (stack.size() > 0) {
            reversePolishNotation.add(stack.pop() + Const.STRING_EMPTY);
        }

        if (countOperand - countOperator != 1) {
            return new ArrayDeque<>();
        }

        return reversePolishNotation;
    }

    private static boolean isNotOperator(char symbol){
        return getPriority(symbol) == NOT_OPERATOR;
    }

    private static boolean isMinus(char symbol){
        return symbol == Const.CHAR_MINUS;
    }

    private static String calculateReversePolishNotationExpression(ArrayDeque<String> reversePolishNotation) {
        String result = Const.STRING_EMPTY;
        Stack<BigDecimal> stack = new Stack<>();

        while (reversePolishNotation.size() != 0) {

            if (getPriority(reversePolishNotation.peekFirst().charAt(0)) == NOT_OPERATOR) {
                stack.push(new BigDecimal(reversePolishNotation.pollFirst()));
            } else {
                BigDecimal rightOperand = stack.pop();
                BigDecimal leftOperand = stack.pop();

                switch (reversePolishNotation.pollFirst().charAt(0)) {
                    case Const.CHAR_DIVIDE:
                        stack.push(leftOperand.divide(rightOperand, 20, BigDecimal.ROUND_HALF_DOWN));
                        break;

                    case Const.CHAR_MULTIPLY:
                        stack.push(leftOperand.multiply(rightOperand));
                        break;

                    case Const.CHAR_MINUS:
                        stack.push(leftOperand.subtract(rightOperand));
                        break;

                    case Const.CHAR_PLUS:
                        stack.push(leftOperand.add(rightOperand));
                        break;

                    default:
                        break;
                }
            }
        }

        if (stack.size() > 0) {
            DecimalFormat df = new DecimalFormat(NO_EXPONENT_PATTERN, DecimalFormatSymbols.getInstance(Locale.ROOT));

            if (stack.peek().toEngineeringString().contains(EXPONENT)) {
                df = new DecimalFormat(EXPONENT_PATTERN, DecimalFormatSymbols.getInstance(Locale.ROOT));
            }

            result = df.format(stack.pop());
        }

        return result;
    }

    private static int getPriority(char symbol) {
        switch (symbol) {
            case Const.CHAR_PLUS:
                return 1;

            case Const.CHAR_MINUS:
                return 1;

            case Const.CHAR_MULTIPLY:
                return 2;

            case Const.CHAR_DIVIDE:
                return 2;

            default:
                return NOT_OPERATOR;
        }
    }

}
