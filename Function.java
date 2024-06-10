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

    public Expression sub(Variable oldVariable, Expression newExpression) {
        if (this.variable.equals(oldVariable)) {
            return this;
        } else {
            Function func = this.copy();
            if (newExpression instanceof Variable var && Runner.freeVarNames.contains(var.name)) {
                String newVarName = this.variable.name;
                int count = 0;
                while (Runner.freeVarNames.contains(newVarName)) {
                    ++count;
                    newVarName = this.variable.name + count;
                }
                func.variable.name = newVarName;
                func.expression = func.expression.sub(this.variable, new Variable(newVarName));
            }
            func.expression = func.expression.sub(oldVariable, newExpression);
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
