/* ATiCS Lambda Lab 2024
 * Anagha Ajesh & Ella Marmol
 */

public class Function implements Expression {
    public Variable variable;
    public Expression expression;

    public Function(Variable variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public Function copy() {
        return new Function(variable.copy(), expression.copy());
    }

    public Expression sub(Variable oldVar, Expression newExp) {
        if (this.variable.equals(oldVar)) {
            return this;
        } else {
            Function func = this.copy();
            if (newExp instanceof Variable var && Runner.freeVariables.contains(var.name)) {
                String newVarName = this.variable.name;
                int counter = 0;
                while (Runner.freeVariables.contains(newVarName)) {
                    ++counter;
                    newVarName = this.variable.name + counter;
                }
                func.variable.name = newVarName;
                func.expression = func.expression.sub(this.variable, new Variable(newVarName));
            }
            func.expression = func.expression.sub(oldVar, newExp);
            return func;
        }
    }

    public String toString() {
        return "(λ" + variable + "." + expression + ")";
    }

    public boolean equals(Expression other) {
        if (other instanceof Function func) {
            return this.variable.equals(func.variable) && this.expression.equals(func.expression);
        }
        return false;
    }
}
