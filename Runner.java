/* ATiCS Lambda Lab 2024
 * Anagha Ajesh & Ella Marmol
 */

import java.util.HashSet;

public class Runner {
    public static final HashSet<String> freeVariables = new HashSet<>();

    private static Expression processExp(Expression exp) {
        if (exp instanceof Application app) {
            if (app.left instanceof Function func) {
                Variable var = func.variable;
                Expression funcExp = func.expression;
                Expression subExp = app.right.copy();
                if (subExp instanceof Variable freeVar) {
                    freeVariables.add(freeVar.name);
                }
                return funcExp.sub(var, subExp);
            } else {
                Expression temp = processExp(app.left);
                if (temp != null) {
                    app.left = temp;
                    return app;
                }
                temp = processExp(app.right);
                if (temp != null) {
                    app.right = temp;
                    return app;
                }
            }
        } else if (exp instanceof Function func) {
            Expression temp = processExp(func.expression);
            if (temp != null) {
                func.expression = temp;
                return func;
            }
        }
        return null;
    }

    public static Expression run(Expression exp) {
        freeVariables.clear();
        Expression subExp = processExp(exp);
        while (subExp != null) {
            exp = subExp;
            subExp = processExp(exp);
        }
        // compare this expression against everything in stored variables. If this
        // evaluates to an expression that is identical to a stored variable, just
        // return that variable
        for (String key : Parser.storedVariables.keySet()) {

            if (exp.equals(Parser.storedVariables.get(key))) {
                return new Variable(key);
            }
        }
        return exp;
    }
}
