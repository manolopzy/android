package com.worldexplorer.calculator2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Note:
 * This version of calculator can deal with more complex expressions with brackets:
 * (2.3 - 1) * 4 * (3 + 3) - 4 ect.
 * but it is not full implemented, contains some little bugs.
 * As long as one inputs the expression correctly, it will act correctly.
 *
 * The algorithm principle used for analysing the mathematical expression is similar
 * to use a expression tree, but in a recursive way instead of constructing
 * the tree. I have ordered the operations according the operator's priority
 * using a recursive algorithm.
 * This algorithm can be improved by using indexes instead of creating
 * lots of arrays during the recursive construction of the expression.
 * See {@link ExpressionHelpler} for more details
 *
 * Esta version tambien apoya unas funciones como sin, cos, log, in, sqrt, etc
 *
 */
public class MainActivity2 extends AppCompatActivity {
    //the current operand
    private String operand = "";
    //+-*/
    private String operator = "";
    //function
    private String functionOperator = "";
    //this will be used to determine if it is a new operation
    private String lastResult = "";
    //mostrar la expresion arimetica dinamicamente a medida que se cambie los entrantes
    private TextView dynamicExpression;
    //mostrar el resultado y la expresion
    private TextView staticExpression;

    //We will use this boolean to mark whether we are dealing with a function operation or a simple arithmetic operation
    private boolean functionMode = false;
    private boolean arithmeticMode = false;
    //The whole expression elements list which stores numbers and operators according to their input order
    //it will be analysed by {@link ExpressionHelpler}
    private List<String> expressionElements = new ArrayList<>();
    //+-*/ operators
    private static final Set<String> arithmeticOperators = new HashSet<>();

    private static final Set<Integer> numbers = new HashSet<>();
    private static final String decimal = ".";

    private String tempExpression = "";
    static {
        arithmeticOperators.add("+");
        arithmeticOperators.add("-");
        arithmeticOperators.add("×");
        arithmeticOperators.add("÷");
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
    }
    private Button clearBtn;
    private Button leftBracketBtn;
    private Button rightBracketBtn;
//    private enum Progress{
//        LEFT, OPERATOR, RIGHT, EQUAL
//    };
//    private Progress currentProgress = Progress.LEFT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dynamicExpression = findViewById(R.id.dynamic_expression);
        staticExpression = findViewById(R.id.static_expression);

        clearBtn = findViewById(R.id.clear);
        leftBracketBtn = findViewById(R.id.left_bracket);
        rightBracketBtn = findViewById(R.id.right_bracket);

        //------------ add listeners for each button ------------
        clearBtn.setOnClickListener(view -> {
            clear(view);
        });

        leftBracketBtn.setOnClickListener(view -> {
            addLeftBracket(view);
        });
        rightBracketBtn.setOnClickListener(view -> {
            addRightBracket(view);
        });

        //set click listener to each number button, from 0 to 9
        TableLayout tableLayout = findViewById(R.id.numbersTable);

        // ------ add listener for number and equal buttons ---------
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button button = (Button) row.getChildAt(j);
                if(button.getText().equals("=")){
                    button.setOnClickListener(view -> {
                        equalClicked(view);
                    });
                }else{
                    button.setOnClickListener(view -> {
                        numberClicked(view);
                    });
                }
            }
        }

        //set click listener to each operator button like +=*/
        LinearLayout layout = findViewById(R.id.operators);
        for (int i = 0; i < layout.getChildCount(); i++) {
            Button button = (Button) layout.getChildAt(i);
            button.setOnClickListener(view -> {
                arithmeticOperatorClicked(view);
            });
        }

        //set up click listener to each function operator button like sin, cos, ...
        layout = findViewById(R.id.functions);
        for (int i = 0; i < layout.getChildCount(); i++) {
            Button button = (Button) layout.getChildAt(i);
            button.setOnClickListener(view -> {
                functionOperatorClicked(view);
            });
        }
    }

    private void addRightBracket(View view) {
        addOperand();
        addElement(")");
        CharSequence text = ((Button) view).getText();
        dynamicExpression.setText(dynamicExpression.getText() + "" + text);
    }

    private void addElement(String element) {
        tempExpression += element;
        expressionElements.add(element);
    }

    private void addLeftBracket(View view) {
        newOperation();
        addOperator();
        addElement("(");
        CharSequence text = ((Button) view).getText();
        dynamicExpression.setText(dynamicExpression.getText() + "" + text);
    }

    private void clear(View view) {
        clearState();
        lastResult = "";
        staticExpression.setText("");
        dynamicExpression.setText("");
    }

    private void functionOperatorClicked(View view) {
        if(arithmeticMode){
            return;
        }
        if(!functionMode){
            functionMode = true;

        }
        String text = ((Button) view).getText().toString();
        functionOperator = text;
        dynamicExpression.setText(text + "()");

    }

    private void equalClicked(View view) {
        if(!arithmeticMode && !functionMode){
            return;
        }
        addOperand();
        ExpressionHelpler.printList(expressionElements);
        double result = ExpressionHelpler.cal(expressionElements);
        if(functionMode){
            result = FunctionOperationHelpler.cal(functionOperator, result);
        }
        String original = dynamicExpression.getText().toString();
        lastResult = result + "";
        dynamicExpression.setText(lastResult);
        staticExpression.setText(original + "=" + lastResult);
        clearState();
    }

    private void clearState() {
        tempExpression = "";
        functionOperator = "";
        operand = "";
        operator = "";
        functionMode = false;
        arithmeticMode = false;
        expressionElements.clear();
    }


    private void arithmeticOperatorClicked(View view) {
        //we need at least one number to be operated
        if (expressionElements.isEmpty() && (operand == null || operand.isEmpty())) {
            return;
        }
        Button btn = ((Button) view);
        String text = btn.getText().toString();

        if(!arithmeticOperators.contains(text)){
            return;
        }
        addOperand();
        if (!operator.isEmpty()) {//replace the current operator
            Log.i("calculator", "the old operator is " + operator);
            operator = text;
            Log.i("calculator", "the new operator is " + operator);

            if(functionMode){
                dynamicExpression.setText(functionOperator + "(" + tempExpression + operator +  ")");
            }
            else{
                //replace the operator
                String oldExpression = dynamicExpression.getText().toString();
                Log.i("calculator", "the original expression is " + oldExpression);
                String newExpression = oldExpression.substring(0, oldExpression.length() - 1) + operator;
                Log.i("calculator", "the new expression is " + newExpression);
                dynamicExpression.setText(newExpression);
            }


        } else {//set a new operator
            Log.i("calculator", "new operator is " + text);
            operator = text;
            if(functionMode){
                dynamicExpression.setText(functionOperator + "(" + tempExpression + operator +  ")");
            }
            else{
                dynamicExpression.setText(dynamicExpression.getText() + "" + operator);
            }

        }
    }

    /**
     *
     * @param view
     */
    private void numberClicked(View view) {
        if(!functionMode && !arithmeticMode){
            arithmeticMode = true;
        }
        addOperator();
        newOperation();

        String text = ((Button) view).getText().toString();
        if(!text.equals(decimal) && !numbers.contains(Integer.valueOf(text))){
            Log.i("calculator", "invalid numbers = " + operand + text);
            return;
        }

        operand += text;
        if(functionMode){
            dynamicExpression.setText(functionOperator + "(" + tempExpression + operand +  ")");
        }
        else{
            dynamicExpression.setText(dynamicExpression.getText() + "" + text);
        }
    }

    /**
     * Clean the old result
     */
    private void newOperation() {
        //clear the last calculation result showing in the text view
        if(!lastResult.isEmpty()){
            lastResult = "";
            dynamicExpression.setText(lastResult);
        }
    }

    private void addOperator() {
        //add the current operator to the expression list
        if (!operator.isEmpty()) {
            addElement(operator);
            operator = "";
        }
    }

    private void addOperand() {
        //add the operand to the expression elements list
        if (!operand.isEmpty()) {
            addElement(operand);
            operand = "";
        }
    }
}