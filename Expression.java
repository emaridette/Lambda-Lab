/* ATiCS Lambda Lab 2024
 * Anagha Ajesh & Ella Marmol
 */

public interface Expression {
    Expression copy();
    Expression sub(Variable v, Expression e);
    boolean equals(Expression other);
}