package com.worldexplorer.calculator2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExpressionHelpler {
    /**
     * analyze the elements of a mathematical expression
     * Instead of creating a list each time to subtract and store a part of the whole
     * expression, one can use two indexes to mark the beginning and end of
     * the part expression
     * @param elements contains a number or a complete mathematical expression
     * @return
     */
    public static double cal(List<String> elements) {
        printList(elements);
        if(elements.size() == 0){
            Log.e("calculator", "none factors in the list", new Exception());
            return 0;
        }
        if(elements.size() == 1){
            return Double.valueOf(elements.get(0));
        }
        final String element = elements.get(0);
        //(a+0)-....
        if(element.equals("(")){
            List<String> list1 = new ArrayList<>();
            int index = 1;
            Expression expression = getBracketElements(index, elements, list1);
            index = expression.endIndex;
            //printList(list1);
            if(index < elements.size()){
                index++;
                String temp;
                List<String> newElements = new ArrayList<>();
                newElements.add(String.valueOf(cal(list1)));
                for (; index < elements.size(); index++) {
                    temp = elements.get(index);
                    newElements.add(temp);
                }
                return cal(newElements);
            }
            else{
                return cal(list1);
            }
        }
        else{
            //in this case, the element must be a number
            if(elements.size() < 3){
                Log.e("calculator", "the expression is not completed", new Exception());
                return 0;
            }
            String opr = elements.get(1);
            if(opr.equals("+")){
                //a + (...)
                //a + ...
                //los ambos casos son iguales
                return arithmeticOperation(element, opr, subElements(2, elements));
            }
            else if(opr.equals("-")){
                String rightOperand = elements.get(2);
                //a-(d+3)-3
                if(rightOperand.equals("(")){
                    List<String> bracketElements = new ArrayList<>();
                    int index = 3;
                    Expression expression = getBracketElements(index, elements, bracketElements);
                    index = expression.endIndex;
                    List<String> newElements = new ArrayList<>();
                    newElements.add(elements.get(0));
                    newElements.add(elements.get(1));
                    newElements.add(String.valueOf(cal(bracketElements)));
                    String temp;
                    index++;
                    for (; index < elements.size(); index++) {
                        temp = elements.get(index);
                        newElements.add(temp);
                    }
                    return cal(newElements);
                }
                else{
                    //a-3*5-...+..
                    //a-3*(5...
                    //a-3/5...
                    //a-b+4...
                    //a-b+(4...
                    //a-b-(4...
                    //a-b-4...
                    //2-9+5
                    if(elements.size() == 3){
                        return arithmeticOperation(elements.get(0), elements.get(1), elements.get(2));
                    }
                    else if(elements.size() > 3){
                        String tempOperator = elements.get(3);
                        if(tempOperator.equals("×") || tempOperator.equals("÷")){
                            //La tarea aqui es encontrar la parte de expresion contenida en la lista que tenga la prioridad mas alta que -

                            List<String> expression = new ArrayList<>();
                            expression.add(elements.get(2));
                            expression.add(elements.get(3));
                            String temp;
                            int brackets = 0;
                            int index = 4;
                            for (; index < elements.size(); index++) {
                                temp = elements.get(index);
                                if(temp.equals("+") || temp.equals("-") && brackets == 0){
                                    break;
                                }
                                else if(temp.equals("(")){
                                    brackets++;
                                    expression.add(temp);
                                }
                                else if(temp.equals(")")){
                                    brackets--;
                                    expression.add(temp);
                                }
                                else{
                                    expression.add(temp);
                                }
                            }
                            List<String> newElements = new ArrayList<>();
                            newElements.add(elements.get(0));
                            newElements.add(elements.get(1));
                            newElements.add(String.valueOf(cal(expression)));
                            for (; index < elements.size(); index++) {
                                newElements.add(elements.get(index));
                            }
                            return cal(newElements);
                        }
                        else{
                            List<String> newElements = new ArrayList<>();
                            newElements.add(String.valueOf(arithmeticOperation(elements.get(0), elements.get(1), elements.get(2))));
                            for (int index = 3; index < elements.size(); index++) {
                                newElements.add(elements.get(index));
                            }
                            return cal(newElements);
                        }
                    }
                    else {
                        Log.e("calculator", "the expression is not completed", new Exception());
                        return 0;
                    }
                }

            }
            else if(opr.equals("×") || opr.equals("÷")){
                //hay cuatro posibilidades aqui
                //a * ( ... ) ...
                //a / ( ... ) ...
                //a * b ...
                //a / b ...
                String rightOperand = elements.get(2);
                if(rightOperand.equals("(")){
                    List<String> bracketElements = new ArrayList<>();
                    int index = 3;
                    Expression expression = getBracketElements(index, elements, bracketElements);
                    index = expression.endIndex;
                    //printList(bracketElements);

                    if(index < elements.size()){
                        index++;
                        List<String> newElements = new ArrayList<>();
                        String temp;
                        newElements.add(String.valueOf(arithmeticOperation(element, opr, String.valueOf(cal(bracketElements)))));
                        for (; index < elements.size(); index++) {
                            temp = elements.get(index);
                            newElements.add(temp);
                        }
                        return cal(newElements);
                    }
                    else{
                        return cal(bracketElements);
                    }
                    //return arithmeticOperation(element, opr, subElements(2, elements));
                }
                else{
                    double tempResult = arithmeticOperation(element, opr, rightOperand);
                    List<String> newElements = new ArrayList<>();
                    newElements.add(String.valueOf(tempResult));
                    newElements.addAll(subElements(3, elements));
                    return cal(newElements);
                }
            }
            else{
                printList(elements);
                Log.e("calculator", "the expression is wrong", new Exception());
                return 0;
            }
        }
    }

    private static Expression getBracketElements(int startIndex, List<String> elements, List<String> bracketElements) {
        int endIndex = startIndex;
        int leftBrackets = 1;
        String temp;
        for (; endIndex < elements.size(); endIndex++) {
            temp = elements.get(endIndex);
            if(temp.equals("(")){
                leftBrackets++;
                bracketElements.add(temp);
            }
            else if(temp.equals(")")){
                leftBrackets--;
                if(leftBrackets == 0){
                    break;
                }
            }
            else{
                bracketElements.add(temp);
            }
        }
        return new Expression(startIndex, endIndex++);
    }


    private static List<String> subElements(int index, List<String> elements) {
        List<String> subList = new ArrayList<>();
        for (; index < elements.size(); index++) {
            subList.add(elements.get(index));
        }
        return subList;
    }

    private static double arithmeticOperation(String leftOperand, String opr, List<String> list2) {
        double result = 0;
        switch (opr) {
            case "+":
                result = Double.valueOf(leftOperand) + cal(list2);
                break;
            case "-":
                result = Double.valueOf(leftOperand) - cal(list2);
                break;
            case "×":
                result = Double.valueOf(leftOperand) * cal(list2);
                break;
            case "÷":
                result = Double.valueOf(leftOperand) / cal(list2);
                break;
            default:
                result = 0;
        }
        return result;
    }
    private static double arithmeticOperation(String leftOperand, String opr, String rightOperand) {
        Log.i("arithmetic operation", "expression = " + leftOperand + opr + rightOperand);
        double result = 0;
        switch (opr) {
            case "+":
                result = Double.valueOf(leftOperand) + Double.valueOf(rightOperand);
                break;
            case "-":
                result = Double.valueOf(leftOperand) - Double.valueOf(rightOperand);
                break;
            case "×":
                result = Double.valueOf(leftOperand) * Double.valueOf(rightOperand);
                break;
            case "÷":
                result = Double.valueOf(leftOperand) / Double.valueOf(rightOperand);
                break;
            default:
                result = 0;
        }
        return result;
    }
    public static void printList(List<String> list) {
        String exprs = "";
        for (String element :
                list) {
            exprs += element;
        }
        Log.i("calculator", "expression = " + exprs);

    }

    static class Expression{
        int startIndex;
        int endIndex;
        public Expression(int startIndex, int endIndex){
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }
}
