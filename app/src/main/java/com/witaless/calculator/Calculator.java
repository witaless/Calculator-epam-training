package com.witaless.calculator;

import android.util.Log;

import java.math.BigDecimal;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Stack;

public class Calculator {
    private static final char CHAR_MINUS='−';
    private static final char CHAR_PLUS='+';
    private static final char CHAR_DIVIDE='÷';
    private static final char CHAR_MULTIPLY='×';

    private static String TAG =Calculator.class.getSimpleName();
    public static Result calculate(String input){
        Result result = new Result();
        result.setResult("");
        result.setError(false);
        ArrayDeque<String> rpn = getReversePolishNotation(input);
        if(rpn.size()<1){
            result.setError(true);
            result.setResult("");
            return result;
        }

        try{
            result.setResult(calcRPNExpression(rpn));
        }catch (Exception e){
            //Log.d(TAG,"Calc exception: "+e);
            result.setError(true);
            result.setResult("");
            return result;
        }
        return result;
    }

    private static ArrayDeque<String> getReversePolishNotation(String infixNotation){
        ArrayDeque<String> rpn = new ArrayDeque<>();
        Stack<Character> stack = new Stack<>();
        String toPush="";
        int countOperand=0;
        int countOperator=0;
        for(int i =0;i<infixNotation.length();i++){
            //Log.d(TAG,"i="+i+" symbol="+infixNotation.charAt(i) +" toPush="+toPush+ " rpn="+rpn  +" stack="+stack);
            //Log.d(TAG,"FIRST SYMBOL IS MiNUS="+(i==0&&infixNotation.charAt(i)==CHAR_MINUS));
            if((i==0&&infixNotation.charAt(i)==CHAR_MINUS )|| (i>0 && infixNotation.charAt(i)==CHAR_MINUS && getPriority(infixNotation.charAt(i-1))!=-1 && toPush.length()<1 )){
                toPush += '-';
            } else {
            if(getPriority(infixNotation.charAt(i))==-1){
                toPush += infixNotation.charAt(i);
            } else {
                rpn.add(toPush);
                countOperand++;
                toPush="";
                if(stack.size()<1){
                    stack.push(infixNotation.charAt(i));
                    countOperator++;
                } else if(getPriority(infixNotation.charAt(i))<=getPriority(stack.peek())) {
                    while (stack.size()>0){
                        rpn.add(stack.pop()+"");
                    }
                    stack.push(infixNotation.charAt(i));
                    countOperator++;
                } else {
                    stack.push(infixNotation.charAt(i));
                    countOperator++;
                }
            }
            }

            //Log.d(TAG,"i="+i+" symbol="+infixNotation.charAt(i) +" toPush="+toPush+ " rpn="+rpn  +" stack="+stack);

        }
        if(toPush.length()>0){
            rpn.add(toPush);
            countOperand++;
        }
        while (stack.size()>0){
             rpn.add(stack.pop()+"");
        }
        Log.d(TAG,"Operators:"+countOperator+" Operands:"+countOperand);
        if(countOperand-countOperator!=1){
            return new ArrayDeque<>();
        }
        return rpn;
    }
    private static String calcRPNExpression(ArrayDeque<String> rpn){
        String result="";
        Stack<BigDecimal> stack=new Stack<>();
        while(rpn.size()!=0){
            Log.d(TAG,"<-rpn:"+rpn+" current:"+rpn.getFirst() +" stack:" +stack);
            if(getPriority(rpn.peekFirst().charAt(0))==-1){
                Log.d(TAG,"prior:"+getPriority(rpn.peekFirst().charAt(0)));
                stack.push(new BigDecimal(rpn.pollFirst()));
            } else {
                BigDecimal rightOperand =stack.pop();
                BigDecimal leftOperand =stack.pop();
                switch (rpn.pollFirst().charAt(0)){
                    case CHAR_DIVIDE:
                        stack.push(leftOperand.divide(rightOperand,20,BigDecimal.ROUND_HALF_DOWN));break;
                    case  CHAR_MULTIPLY:
                        stack.push(leftOperand.multiply(rightOperand));break;
                    case CHAR_MINUS:
                        stack.push(leftOperand.subtract(rightOperand));break;
                    case CHAR_PLUS:
                        stack.push(leftOperand.add(rightOperand));break;
                        default:
                            break;
                }
            }
        }
        if(stack.size()>0){
            DecimalFormat df = new DecimalFormat("#,##0.0##################", DecimalFormatSymbols.getInstance(Locale.ROOT));
            if(stack.peek().toEngineeringString().contains("E")) { //check for exponent
                df = new DecimalFormat("0.#################E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
            }
            result=df.format(stack.pop());
        }
        return result;
    }

    private static int getPriority(char operand){
        switch (operand){
            case CHAR_PLUS: return 1;
            case CHAR_MINUS: return 1;
            case CHAR_MULTIPLY: return 2;
            case CHAR_DIVIDE: return 2;
            default: return -1;
        }
    }
    public static class Result{
        private String result;
        private boolean error;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }
    }
}
