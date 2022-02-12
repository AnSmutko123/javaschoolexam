package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayList;
import java.util.List;
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
        Calc c = new Calc();
        for (String token : tokenize(statement)) {
            if (token.length() == 1 && "+-*/()".contains(token)) {
                c.op(token.charAt(0));
            } else {
                c.num(Integer.parseInt(token));
            }
        }
        return c.eval();
    }

    // выделяет числа и символы
    public static List<String> tokenize(String expr) {
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("[-+\\*/()]|[\\d]+").matcher(expr);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    private static class Calc {
        private Stack<Integer> nums = new Stack<Integer>();
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
        public void num(int v) {
            nums.push(v);
        }

        // получение значения выражения
        public String eval() {
            while (!ops.empty()) {
                apply(ops.pop());
            }
            return nums.pop().toString();
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
                int b = nums.pop();
                int a = nums.pop();
                nums.push(a + b);
            } else if (c == '-') {
                int b = nums.pop();
                int a = nums.pop();
                nums.push(a - b);
            } else if (c == '*') {
                int b = nums.pop();
                int a = nums.pop();
                nums.push(a * b);
            } else if (c == '/') {
                int b = nums.pop();
                int a = nums.pop();
                nums.push(a / b);
            }
        }
    }

}
