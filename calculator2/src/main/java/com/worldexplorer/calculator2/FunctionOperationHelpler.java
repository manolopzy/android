package com.worldexplorer.calculator2;

import java.util.HashSet;
import java.util.Set;

/**
 * Esta clase es una ayudante para implementar las
 * operaciones de funciones como "sin, cos, tan, etc"
 */
public class FunctionOperationHelpler {
    //supported function operations like sin, cos, tan, exponent, ln, log
    private static final Set<String> functionOperators = new HashSet<>();
    static{
        functionOperators.add("sin");
        functionOperators.add("cos");
        functionOperators.add("log");
        functionOperators.add("ln");
        functionOperators.add("sqrt");
        functionOperators.add("tan");
    }
    private static final String SIN = "sin";
    private static final String COS = "cos";
    private static final String LOG = "log";
    private static final String LN = "ln";
    private static final String SQRT = "sqrt";
    private static final String TAN = "tan";
    public static double cal(String operator, double operand) {
        switch (operator) {
            case SIN:
                return Math.sin(operand);
            case COS:
                return Math.cos(operand);
            case LOG:
                return Math.log10(operand);
            case LN:
                return Math.log(operand);
            case SQRT:
                return Math.sqrt(operand);
            case TAN:
                return Math.tan(operand);
            default:
                return  0;
        }
    }
}
