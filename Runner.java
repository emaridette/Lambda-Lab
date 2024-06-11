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
                //processing left and right of the app, respectively 
                Expression tempSub = processExp(app.left);
                if (tempSub != null) {
                    app.left = tempSub;
                    return app;
                } else {
                    tempSub = processExp(app.right);
                    if (tempSub != null) {
                        app.right = tempSub;
                        return app;
                    }
                }

            }
        } else if (exp instanceof Function func) {
            //recursive function processing
            Expression tempSub = processExp(func.expression);
            if (tempSub != null) {
                func.expression = tempSub;
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

        //check for existing in storedVariables 
        for (String var : Parser.storedVariables.keySet()) {
            if (exp.equals(Parser.storedVariables.get(var))) {
                return new Variable(var);
            }
        }
        return exp;
    }
}
