package bootcamp2014.calc.src.service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvaluatorService {

    /*
     * For incomplete expressions below code will throw exceptions, e.g. 23 + or * 345 etc.
     * For exceptions unit test could be written using @Test(expected = ScriptException.class)
     */
    public static Double eval(String expr) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return (Double) engine.eval(expr);
    }

    public static double add(double arg1, double arg2) throws ScriptException {
        return arg1 + arg2;
    }

    public static double sub(double arg1, double arg2) throws ScriptException {
        return arg1 - arg2;
    }

    public static double mul(double arg1, double arg2) throws ScriptException {
        return arg1 * arg2;
    }

    public static double divide(double arg1, double arg2) throws ScriptException {
        return arg1 / arg2;
    }

    public static long pow(int n, int p) {
        long result = 1;
        for (int i = 0; i < p; i++) {
            result = result * n;
        }
        return result;
    }

    public static long fact(int n) {
        long result;
        if (n <= 1) {
            result = 1;
        } else {
            result = n;
        }
        while (n > 1) {
            n = n - 1;
            result = result * n;
        }
        return result;
    }
}


