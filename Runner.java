/* ATiCS Lambda Lab 2024
 * Anagha Ajesh & Ella Marmol
 */

import java.util.HashSet;

public class Runner {
    public static final HashSet<String> freeVarNames = new HashSet<>();

    private static Expression runHelper(Expression exp) {
        if (exp instanceof Application app) {
            if (app.left instanceof Function func) {
                Variable var = func.variable;
                Expression funcExp = func.expression;
                Expression subExp = app.right.copy();
                if (subExp instanceof Variable freeVar) {
                    freeVarNames.add(freeVar.name);
                }
                return funcExp.sub(var, subExp);
            } else {
                Expression temp = runHelper(app.left);
                if (temp != null) {
                    app.left = temp;
                    return app;
                }
                temp = runHelper(app.right);
                if (temp != null) {
                    app.right = temp;
                    return app;
<<<<<<< HEAD
=======
                } else {
                    tempSub = processExp(app.right);
                    if (tempSub != null) {
                        app.right = tempSub;
                        return app;
                    }
>>>>>>> parent of c751b6e (stuff)
                }
            }
        } else if (exp instanceof Function func) {
            Expression temp = runHelper(func.expression);
            if (temp != null) {
                func.expression = temp;
                return func;
            }
        }
        return null;
    }

    public static Expression run(Expression exp) {
        freeVarNames.clear();
        Expression subExp = runHelper(exp);
        while (subExp != null) {
            exp = subExp;
            subExp = runHelper(exp);
        }
<<<<<<< HEAD
        // compare this expression against everything in stored variables. If this
        // evaluates to an expression that is identical to a stored variable, just
        // return that variable
        for (String key : Parser.storedVariables.keySet()) {

            if (exp.equals(Parser.storedVariables.get(key))) {
                return new Variable(key);
=======

        //check for existing in storedVariables 
        for (String var : Parser.storedVariables.keySet()) {
            if (exp.equals(Parser.storedVariables.get(var))) {
                return new Variable(var);
>>>>>>> parent of c751b6e (stuff)
            }
        }
        return exp;
    }
}
