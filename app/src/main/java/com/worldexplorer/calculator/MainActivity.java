package com.worldexplorer.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ctrl + alt + l to format
 * ctrl + alt + o to import
 * alt + enter
 * Simple calculator implementation
 * Calculadora de expresion matematica con dos terminos de numeros enteros
 * se puede realizar adicion, multiplicacion, substraccion, division
 *
 */
public class MainActivity extends AppCompatActivity {

    //the current operand
    private String operand = "";
    //+-*/
    private String operator = "";
    private String lastResult = "";
    //mostrar la expresion arimetica dinamicamente a medida que se cambie los entrantes
    private TextView dynamicExpression;
    //mostrar el resultado y la expresion
    private TextView staticExpression;

    //The whole expression elements list which stores numbers and operators according to their input order
    private List<String> expressionElements = new ArrayList<>();
    private static final Set<String> arithmeticOperators = new HashSet<>();
    private static final Set<Integer> numbers = new HashSet<>();
    private static final String decimal = ".";

    static {
        arithmeticOperators.add("+");
        arithmeticOperators.add("-");
        arithmeticOperators.add("×");
        arithmeticOperators.add("÷");
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
    }
//    private enum Progress{
//        LEFT, OPERATOR, RIGHT, EQUAL
//    };
//    private Progress currentProgress = Progress.LEFT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dynamicExpression = findViewById(R.id.dynamic_expression);
        staticExpression = findViewById(R.id.static_expression);

        //set click listener to each number button, from 0 to 9
        LinearLayout layout = findViewById(R.id.numbers1);
        for (int i = 0; i < layout.getChildCount(); i++) {
            Button button = (Button) layout.getChildAt(i);
            button.setOnClickListener(view -> {
                numberClicked(view);
            });
        }
        layout = findViewById(R.id.numbers2);
        for (int i = 0; i < layout.getChildCount(); i++) {
            Button button = (Button) layout.getChildAt(i);
            button.setOnClickListener(view -> {
                numberClicked(view);
            });
        }
        //set click listener to each operator button like +=*/
        layout = findViewById(R.id.operators);
        for (int i = 0; i < layout.getChildCount(); i++) {
            Button button = (Button) layout.getChildAt(i);
            button.setOnClickListener(view -> {
                operatorClicked(view);
            });
        }
    }

    private void operatorClicked(View view) {

        //we need at least one number to be operated
        if (expressionElements.isEmpty() && (operand == null || operand.isEmpty())) {
            return;
        }
        Button btn = ((Button) view);
        String text = (String) btn.getText();
        if (text.equals("=")) {
            Log.i("calculator", "start to calculate");
            if (!operator.isEmpty()) {//another number should be introduced
                return;
            }
            addOperand();
            calculate();
        } else {//+-*/
            addOperand();
            if (arithmeticOperators.contains(btn.getText().toString())) {
                if (!operator.isEmpty()) {
                    Log.i("calculator", "the operator is " + operator);
                    operator = text;
                    //replace the operator
                    String original = dynamicExpression.getText().toString();
                    Log.i("calculator", "the original is " + original);
                    String newStr = original.substring(0, original.length() - 1) + operator;
                    Log.i("calculator", "the new string is " + newStr);
                    dynamicExpression.setText(newStr);
                } else {
                    Log.i("calculator", "the operator is empty");
                    operator = text;
                    dynamicExpression.setText(dynamicExpression.getText() + "" + operator);
                }
            }
        }
    }

    private void addOperand() {
        //add the operand to the expression elements list
        if (operand != null && !operand.isEmpty()) {
            expressionElements.add(operand);
            operand = "";
        }
    }

    private void calculate() {
        Log.i("calculator", "start to calculate a result for " + expressionElements.size() + " elements");
        int result = 0;
        String original = dynamicExpression.getText().toString();
        if (expressionElements.size() == 1) {
            result = Integer.valueOf(expressionElements.get(0));
        } else if (expressionElements.size() >= 3) {
            int left = Integer.valueOf(expressionElements.get(0));
            String opt = expressionElements.get(1);
            int right = Integer.valueOf(expressionElements.get(2));
            result = arithmetic(left, opt, right);
            Log.i("calculator", "the result is " + result);
//            for (int i = 0; i < expressionElements.size(); i++) {
//                String element = expressionElements.get(i);
//                if (arithmeticOperators.contains(element)) {//it is an operator
//                } else {//it is a number
//                }
//            }
        } else {
            return;
        }

        lastResult = result + "";
        dynamicExpression.setText(lastResult);
        staticExpression.setText(original + "=" + lastResult);
        operator = "";
        operand = "";
        expressionElements.clear();
    }

    private int arithmetic(int left, String opt, int right) {
        int result = 0;
        switch (opt) {
            case "+":
                result = Integer.valueOf(left) + Integer.valueOf(right);
                break;
            case "-":
                result = Integer.valueOf(left) - Integer.valueOf(right);
                break;
            case "×":
                result = Integer.valueOf(left) * Integer.valueOf(right);
                break;
            case "÷":
                result = Integer.valueOf(left) / Integer.valueOf(right);
                break;
            default:
                result = 0;
        }
        return result;
    }

    private void numberClicked(View view) {
        //add the current operator to the expression list
        if (!operator.isEmpty()) {
            expressionElements.add(operator);
            operator = "";
        }
        //clear the last calculation result showing in the text view
        if(!lastResult.isEmpty()){
            lastResult = "";
            dynamicExpression.setText(lastResult);
        }

        CharSequence text = ((Button) view).getText();
        operand += text;
        dynamicExpression.setText(dynamicExpression.getText() + "" + text);
    }
}