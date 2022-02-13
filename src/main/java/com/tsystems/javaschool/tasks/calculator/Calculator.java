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
        if (statement == null || statement.isEmpty()) {
            return null;
        }
        Calc c = new Calc();
        List<String> tokenizeList = tokenize(statement);
        if (!testForEvaluate(tokenizeList)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean lastToken = false;
        for (int i = 0; i < tokenizeList.size(); i++) {
            if (tokenizeList.get(i).length() == 1 && "+-*/()".contains(tokenizeList.get(i))) {
                if (i == tokenizeList.size()-1) {
                    lastToken = true;
                }
                if (!c.op(tokenizeList.get(i).charAt(0), lastToken)) {
                    return null;
                }
            } else if (tokenizeList.get(i).equals(".")) {
                sb.append(tokenizeList.get(i));
            } else {
                sb.append(tokenizeList.get(i));
                if (i != tokenizeList.size() - 1 && !tokenizeList.get(i + 1).equals(".")) {
                    if (sb.toString().startsWith(".")) {
                        return null;
                    }
                    c.num(Double.parseDouble(sb.toString()));
                    sb.setLength(0);
                }
                if (i == tokenizeList.size() - 1) {
                    c.num(Double.parseDouble(sb.toString()));
                    if (sb.toString().startsWith(".")) {
                        return null;
                    }
                }
            }
        }
        return c.eval();
    }

    public boolean testForEvaluate(List<String> list) {
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1 && list.get(i).equals(list.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    public static List<String> tokenize(String expr) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[-+\\*/()\\.\\d]+");
        if (!pattern.matcher(expr).matches()) {
            return null;
        }
        Matcher m = Pattern.compile("[-+\\*/()\\.]|[\\d]+").matcher(expr);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    private static class Calc {
        private Stack<Double> nums = new Stack<Double>();
        private Stack<Character> ops = new Stack<Character>();

        public boolean op(char c, boolean lastChar) {
            int count = 0;
            if (c == '(') {
                count++;
                ops.push(c);
            } else if (c == ')') {
                count--;
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
            if (lastChar && count != 0) {
                return false;
            }
            return true;
        }

        public void num(double v) {
            nums.push(v);
        }

        public String eval() {
            while (!ops.empty()) {
                if (!apply(ops.pop())) {
                    return null;
                }
            }
            double result = nums.pop();
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#.####", otherSymbols);
            String stringResult = decimalFormat.format(result);
            return stringResult;
        }

        private int priority(char c) {
            if (c == '+') {
                return 1;
            }
            if (c == '-') {
                return 1;
            }
            if (c == '*') {
                return 2;
            }
            if (c == '/') {
                return 2;
            }
            return 0;
        }

        private boolean apply(char c) {
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
                if (b == 0) {
                    return false;
                }
                nums.push(a / b);
            }
            return true;
        }
    }

}
