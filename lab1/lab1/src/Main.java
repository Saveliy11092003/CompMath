import java.util.Scanner;

public class Main {
    private static double epsilon;
    private static double delta;
    private static double a;
    private static double b;
    private static double c;

    private static final double INFINITY = 100000000.0;
    private static final double NEGATIVE_INFINITY = -100000000.0;

    private static final int MULTIPLICITY_ONE = 1;
    private static final int MULTIPLICITY_TWO = 2;
    private static final int MULTIPLICITY_THREE = 3;

    public static void main(String[] args) {

        System.out.println("Please type 'a > 0','b','c'");
        a = Double.parseDouble(args[0]);
        b = Double.parseDouble(args[1]);
        c = Double.parseDouble(args[2]);

        System.out.println("Please type delta: ");
        delta = Double.parseDouble(args[3]);

        System.out.println("Please type epsilon: ");
        epsilon = Double.parseDouble(args[4]);

        double aInFirstDer = 3;
        double bInFirstDer = 2 * a;
        double cInFirstDer = b;

        double discriminant = a * a - aInFirstDer * cInFirstDer;//Дискриминант для четного коэф. b

        if (discriminant < 0) {
            FunctionValue result = funcValueCases(0);
            if (result == FunctionValue.IN_EPSILON_SURROUNDINGS) {
                System.out.println("The answer is 0");
                checkStoppingCondition(0);
            } else if (result == FunctionValue.LESS_THEN_NEGATIVE_EPSILON) {
                double answer = bisection(0, INFINITY);
                System.out.println("The answer is " + answer);
                checkStoppingCondition(answer);
            } else {
                double answer = bisection(NEGATIVE_INFINITY, 0);
                System.out.println("The answer is " + answer);
                checkStoppingCondition(answer);
            }
        }else{
            double alpha = (-1 * bInFirstDer/2 - Math.sqrt(discriminant))/aInFirstDer;
            double betta = (-1 * bInFirstDer/2 + Math.sqrt(discriminant))/aInFirstDer;

            FunctionValue alphaValueFunc = funcValueCases(alpha);
            FunctionValue bettaValueFunc = funcValueCases(betta);

            if(alphaValueFunc == FunctionValue.IN_EPSILON_SURROUNDINGS && bettaValueFunc == FunctionValue.IN_EPSILON_SURROUNDINGS) {
                System.out.println("first case");
                double result = (alpha+betta)/2;
                System.out.println("The answer is: " + result + ". Multiplicity: 3");
                checkStoppingCondition(result);
            }

            if(alphaValueFunc == FunctionValue.MORE_THEN_EPSILON && bettaValueFunc == FunctionValue.MORE_THEN_EPSILON) {
                System.out.println("second case");
                double result = bisection(NEGATIVE_INFINITY, alpha);
                System.out.println("The answer is: " + result + ". Multiplicity: " + checkMultiplicity(result));
                checkStoppingCondition(result);
            }

            if(alphaValueFunc == FunctionValue.LESS_THEN_NEGATIVE_EPSILON && bettaValueFunc == FunctionValue.LESS_THEN_NEGATIVE_EPSILON) {
                System.out.println("third case");
                double result = bisection(betta, INFINITY);
                System.out.println("The answer is: " + result + ". Multiplicity: " + checkMultiplicity(result));
                checkStoppingCondition(result);
            }

            if(alphaValueFunc == FunctionValue.MORE_THEN_EPSILON && bettaValueFunc == FunctionValue.IN_EPSILON_SURROUNDINGS) {
                System.out.println("fourth case");
                double result = bisection(NEGATIVE_INFINITY, alpha);
                System.out.println("The answers are: " + betta + " and " + result +
                        ". Multiplicities: " + checkMultiplicity(betta) + " and " + checkMultiplicity(result));
                checkStoppingCondition(betta);
                checkStoppingCondition(result);
            }

            if(alphaValueFunc == FunctionValue.IN_EPSILON_SURROUNDINGS && bettaValueFunc == FunctionValue.LESS_THEN_NEGATIVE_EPSILON) {
                System.out.println("fifth case");
                double result = bisection(betta, INFINITY);
                System.out.println("The answers are: " + alpha + " and " + result +
                        ". Multiplicities: " + checkMultiplicity(alpha) + " and " + checkMultiplicity(result));
                checkStoppingCondition(alpha);
                checkStoppingCondition(result);
            }

            if(alphaValueFunc == FunctionValue.MORE_THEN_EPSILON && bettaValueFunc == FunctionValue.LESS_THEN_NEGATIVE_EPSILON) {
                System.out.println("sixth case");
                double result1 = bisection(NEGATIVE_INFINITY, alpha);
                System.out.print("The answers are: " + result1);
                double result2 = bisection(alpha, betta);
                System.out.print(" and " + result2);
                double result3 = bisection(betta, INFINITY);
                System.out.println(" and " + result3 + ". Multiplicities: 1");
                checkStoppingCondition(result1);
                checkStoppingCondition(result2);
                checkStoppingCondition(result3);
            }
        }
    }

    public static void checkStoppingCondition(double x){
        System.out.println("Stopping condition for " + x + " - " + Math.abs(funcValue(x)));
    }

    private static FunctionValue funcValueCases(double num) {
        double func = funcValue(num);
        if (Math.abs(func) < epsilon) {
            return FunctionValue.IN_EPSILON_SURROUNDINGS;
        } else if (func >= epsilon) {
            return FunctionValue.MORE_THEN_EPSILON;
        } else {
            return FunctionValue.LESS_THEN_NEGATIVE_EPSILON;
        }
    }

    private static double funcValue(double num) {
        double value = num * num * num + a * num * num + b * num + c;
        return value;
    }

    private static double bisection(double a, double b) {
        if (a == NEGATIVE_INFINITY) {
            double border = b - delta;
            int i = 1;
            double value = funcValue(b);
            if (value < 0) {
                while (funcValue(border) < 0) {
                    i++;
                    border = b - i * delta;
                }
                a = border;
                b = b - (i - 1) * delta;
            } else {
                while (funcValue(border) > 0) {
                    i++;
                    border = b - i * delta;
                }
                a = border;
                b = b - (i - 1) * delta;
            }
        } else if (b == INFINITY) {
            double border = a + delta;
            int i = 1;
            double value = funcValue(a);
            if (value > 0) {
                while (funcValue(border) > 0) {
                    i++;
                    border = a + i * delta;
                }
                b = border;
                a = a + (i - 1) * delta;
            } else {
                while (funcValue(border) < 0) {
                    i++;
                    border = a + i * delta;
                }
                b = border;
                a = a + (i - 1) * delta;
            }
        }
        double c = (a + b) / 2;
        FunctionValue result = funcValueCases(c);
        while (result != FunctionValue.IN_EPSILON_SURROUNDINGS) {
            if (funcValue(c) * funcValue(b) < 0) {
                a = c;
            } else {
                b = c;
            }
            c = (a + b) / 2;
            result = funcValueCases(c);
        }
        return c;
    }

    private static boolean checkNullFunc(double arg) {
        return Math.abs(funcValue(arg)) <= epsilon;
    }

    private static boolean checkNullFirstDer(double arg) {
        return Math.abs(3 * arg * arg + 2 * a * arg + b) <= epsilon;
    }


    private static boolean checkNullSecDer(double arg) {
        return Math.abs(6 * arg + 2 * a) <= epsilon;
    }
    private static int checkMultiplicity(double arg) {
        boolean isFuncNull = checkNullFunc(arg);
        boolean isFirstNull = checkNullFirstDer(arg);
        boolean isSecondNull = checkNullSecDer(arg);

        if(isFuncNull && isFirstNull && isSecondNull) {
            return MULTIPLICITY_THREE;
        }else if(isFuncNull && isFirstNull){
            return MULTIPLICITY_TWO;
        }else {
            return MULTIPLICITY_ONE;
        }
    }

}