package com.witaless.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Stack;

public class Calculator {

    private static final char CHAR_MINUS = '−';
    private static final char CHAR_PLUS = '+';
    private static final char CHAR_DIVIDE = '÷';
    private static final char CHAR_MULTIPLY = '×';

    private static String TAG = Calculator.class.getSimpleName();

    public static ResultModel calculate(String input) {
        ResultModel result = new ResultModel();
        result.setResult("");
        result.setError(false);
        ArrayDeque<String> reversePolishNotation = getReversePolishNotation(input);

        if (reversePolishNotation.size() < 1) {
            result.setError(true);
            result.setResult("");

            return result;
        }

        try {
            result.setResult(calcRPNExpression(reversePolishNotation));
        } catch (Exception e) {
            Log.d(TAG, "Calc exception: " + e);
            result.setError(true);
            result.setResult("");

            return result;
        }

        return result;
    }

    private static ArrayDeque<String> getReversePolishNotation(String infixNotation) {
        ArrayDeque<String> reversePolishNotation = new ArrayDeque<>();
        Stack<Character> stack = new Stack<>();
        String toPush = "";
        int countOperand = 0;
        int countOperator = 0;

        for (int i = 0; i < infixNotation.length(); i++) {

            if ((i == 0 && infixNotation.charAt(i) == CHAR_MINUS) ||
                    (i > 0 && infixNotation.charAt(i) == CHAR_MINUS &&
                            getPriority(infixNotation.charAt(i - 1)) != -1 && toPush.length() < 1)) {

                toPush += '-';
            } else {

                if (getPriority(infixNotation.charAt(i)) == -1) {
                    toPush += infixNotation.charAt(i);
                } else {
                    reversePolishNotation.add(toPush);
                    countOperand++;
                    toPush = "";

                    if (stack.size() < 1) {
                        stack.push(infixNotation.charAt(i));
                        countOperator++;
                    } else if (getPriority(infixNotation.charAt(i)) <= getPriority(stack.peek())) {

                        while (stack.size() > 0) {
                            reversePolishNotation.add(stack.pop() + "");
                        }

                        stack.push(infixNotation.charAt(i));
                        countOperator++;
                    } else {
                        stack.push(infixNotation.charAt(i));
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
            reversePolishNotation.add(stack.pop() + "");
        }

        Log.d(TAG, "Operators:" + countOperator + " Operands:" + countOperand);

        if (countOperand - countOperator != 1) {
            return new ArrayDeque<>();
        }

        return reversePolishNotation;
    }

    private static String calcRPNExpression(ArrayDeque<String> reversePolishNotation) {
        String result = "";
        Stack<BigDecimal> stack = new Stack<>();

        while (reversePolishNotation.size() != 0) {

            if (getPriority(reversePolishNotation.peekFirst().charAt(0)) == -1) {
                stack.push(new BigDecimal(reversePolishNotation.pollFirst()));
            } else {
                BigDecimal rightOperand = stack.pop();
                BigDecimal leftOperand = stack.pop();

                switch (reversePolishNotation.pollFirst().charAt(0)) {
                    case CHAR_DIVIDE:
                        stack.push(leftOperand.divide(rightOperand, 20, BigDecimal.ROUND_HALF_DOWN));
                        break;

                    case CHAR_MULTIPLY:
                        stack.push(leftOperand.multiply(rightOperand));
                        break;

                    case CHAR_MINUS:
                        stack.push(leftOperand.subtract(rightOperand));
                        break;

                    case CHAR_PLUS:
                        stack.push(leftOperand.add(rightOperand));
                        break;

                    default:
                        break;
                }
            }
        }

        if (stack.size() > 0) {
            DecimalFormat df = new DecimalFormat("#,##0.0##################", DecimalFormatSymbols.getInstance(Locale.ROOT));

            if (stack.peek().toEngineeringString().contains("E")) {
                df = new DecimalFormat("0.#################E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
            }

            result = df.format(stack.pop());
        }

        return result;
    }

    private static int getPriority(char operand) {
        switch (operand) {
            case CHAR_PLUS:
                return 1;

            case CHAR_MINUS:
                return 1;

            case CHAR_MULTIPLY:
                return 2;

            case CHAR_DIVIDE:
                return 2;

            default:
                return -1;
        }
    }


}
