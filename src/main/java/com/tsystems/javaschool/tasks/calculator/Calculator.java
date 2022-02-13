package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(calculator.evaluate("(1+38)*4-5"));
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        // TODO: Implement the logic here
        if (statement == null || statement.isEmpty()){
            return null;
        }
        Calc c = new Calc();
        List<String> tokenizeList = tokenize(statement);
        if(!testForEvaluate(tokenizeList)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokenizeList.size(); i++) {
            if (tokenizeList.get(i).length() == 1 && "+-*/()".contains(tokenizeList.get(i))) {
                c.op(tokenizeList.get(i).charAt(0));
            } else if (tokenizeList.get(i).equals(".")) {
                sb.append(tokenizeList.get(i));
            } else {
                sb.append(tokenizeList.get(i));
                if (i != tokenizeList.size() - 1 && !tokenizeList.get(i+1).equals(".")) {
                    c.num(Double.parseDouble(sb.toString()));
                    sb.setLength(0);
                }
                if (i == tokenizeList.size() - 1) {
                    c.num(Double.parseDouble(sb.toString()));
                }
            }
        }
        double result = c.eval();

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.####", otherSymbols);
        String secondResult = decimalFormat.format(result);
        return secondResult;
    }

    public boolean testForEvaluate(List<String> list){
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1 && list.get(i).equals(list.get(i + 1))){
                return false;
            }
        }
        return true;
    }

    // выделяет числа и символы
    public static List<String> tokenize(String expr) {
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("[-+\\*/()\\.]|[\\d]+").matcher(expr);
        while (m.find()) {
            list.add(m.group());
        }
        System.out.println(list);
        return list;
    }

    private static class Calc {
        private Stack<Double> nums = new Stack<Double>();
        private Stack<Character> ops = new Stack<Character>();

        // операции
        public void op(char c) {
            if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (true) {
                    char cc = ops.pop();
                    if (cc == '(') {
                        break;
                    }
                    apply(cc);
                }
            } else {
                while (!ops.empty() && priority(ops.peek()) >= priority(c)) {
                    apply(ops.pop());
                }
                ops.push(c);
            }
        }

        // числа
        public void num(double v) {
            nums.push(v);
        }

        // получение значения выражения
        public Double eval() {
//            try {
                while (!ops.empty()) {
                    apply(ops.pop());
                }
                return nums.pop();
//            } catch (ArithmeticException ex) {
//                return null;
//            }

        }

        private int priority(char c) {
            if (c == '+') { return 1; }
            if (c == '-') { return 1; }
            if (c == '*') { return 2; }
            if (c == '/') { return 2; }
            return 0;
        }

        // сердце калькулятора
        private void apply(char c) {
            if (c == '+') {
                double b = nums.pop();
                double a = nums.pop();
                nums.push(a + b);
            } else if (c == '-') {
                double b = nums.pop();
                double a = nums.pop();
                nums.push(a - b);
            } else if (c == '*') {
                double b = nums.pop();
                double a = nums.pop();
                nums.push(a * b);
            } else if (c == '/') {
                double b = nums.pop();
                double a = nums.pop();
                nums.push(a / b);
            }
        }
    }

}
