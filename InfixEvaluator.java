/**
 * Project from Elliot Koffman and Paul Wolfgang's book "Data Structures: Abstraction and Design Using Java"
 * Structure and data fields from their book all methods made by me
 * Used to evaluate a given infix expression in string form
 * Will take the four basic operations as well as exponents(^), parenthesis(()) and mod(%)
 **/

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

/**
 * A class that solves a given infix expression as a string.
 **/
public class InfixEvaluator {
	
	// Data fields
	/**
	 * String of all known operators.
	 **/
	private static final String OPERATORS = "+-*/%^()";
	/**
	 * Array of all the precedence values of each operator.
	 **/
	private static final int[] PRECEDENCE = {1, 1, 2, 2, 2, 3, 0, 0};
	/**
	 * Stack of integers used to store operands.
	 **/
	private static Stack<Integer> operandStack = new Stack<Integer>();
	/**
	 * Stack of characters used to store operators.
	 **/
	private static Stack<Character> operatorStack = new Stack<Character>();
	
	/**
	 * Evaluates a given infix expression
	 * 
	 * @param expression the infix to be evaluated
	 * @return the solution of the expression
	 * throws SyntaxErrorException if any part of expression is invalid
	 */
	public static int evaluate(String expression) throws SyntaxErrorException {
		// Ensures the stacks are empty before use
		emptyStacks();
		// Temporary variables used to evaluate expression
		String temp;
		char ch;
		// Infix expression's solution to be returned once found
		int finalReturn;
		// Puts the infix into a scanner to evaluate it
		Scanner sc = new Scanner(expression);
		// Determines if infix is in the proper form with acceptable inputs
		while (sc.hasNext()) {
			temp = sc.next();
			ch = temp.charAt(0);
			// Determines if the char is an operator
			if (isOperator(ch)) {
				try {
					processOperator(ch);
				} catch (NumberFormatException e) {
					throw new SyntaxErrorException("Invalid operator");
				}
			// Determines if char is an integer if so puts it on the stack
			} else if (Character.isDigit(ch)){
				try {
					operandStack.push(Integer.parseInt(temp));
				} catch (NumberFormatException e) {
					throw new SyntaxErrorException("Invalid syntax");
				}
			} else {
				throw new SyntaxErrorException("Invalid expression");
			}
		}
		// Evaluates the infix
		while (!operatorStack.empty()) {
			operandStack.push(evalOp(operatorStack.pop()));
		}
		// Should be left with 1 operand
		finalReturn = operandStack.pop();
		if (!operandStack.empty()) {
			throw new SyntaxErrorException("Too many operands");
		}
		return finalReturn;
	}
	
	/**
	 * Takes a given operator and inserts it into the operator stack or
	 * solves the stacks if conditions met
	 * 
	 * @param op the operator to process
	 */
	private static void processOperator(char op) throws SyntaxErrorException {
		// Temporary values used in process
		int result = 0;
		char tempOp;
		// Puts operator onto stack if not processes parenthesis then standard operators
		if (operatorStack.empty()) {
			operatorStack.push(op);
		} else if (op == ')') {
			try {
				while (operatorStack.peek() != '(') {
					tempOp = operatorStack.pop();
					result = evalOp(tempOp);
					operandStack.push(result);
				}
			} catch(EmptyStackException e) {
				throw new SyntaxErrorException("No open parenthesis '('");
			}
			operatorStack.pop();
		} else if (op == '(') {
			operatorStack.push(op);
		} else {
			while (!operatorStack.empty() && precedence(operatorStack.peek()) >= precedence(op)) {
				tempOp = operatorStack.pop();
				result = evalOp(tempOp);
				operandStack.push(result);
			}
			operatorStack.push(op);
		}
	}
	
	/**
	 * Determines the precedence of a give operator
	 * 
	 * @param op operator to find the precedence of
	 * @return the precedence of op as an integer
	 */
	private static int precedence (char op) {
		return PRECEDENCE[OPERATORS.indexOf(op)];
	}
	
	/**
	 * Takes the first two operands in the stack and performs
	 * the operation corresponding to op
	 * 
	 * @param op the operator used to determine the operation
	 * @return the solution of the given formula
	 */
	private static int evalOp(char op) {
		// Used to solve for a given op using the operand stack
		int rhs;
		int lhs;
		try {
		rhs = operandStack.pop();
		lhs = operandStack.pop();
		} catch (EmptyStackException e) {
			throw new SyntaxErrorException("Too many operands/operators or parenthesis mismatch");
		}
		int result = 0;
		switch (op) {
		case '+' : result = lhs + rhs;
				   break;
		case '-' : result = lhs - rhs;
				   break;
		case '/' : result = lhs / rhs;
				   break;
		case '*' : result = lhs * rhs;
				   break;
		case '^' : result = (int) Math.pow(lhs, rhs);
				   break;
		case '%' : result = lhs % rhs;
				   break;
	}
		return result;
	}
	
	/**
	 * Determines if a given char is a known operator
	 * 
	 * @param ch the char to be tested
	 * @return true if ch is a known operator or false if
	 * not
	 */
	private static boolean isOperator(char ch) {
		return OPERATORS.indexOf(ch) != -1;
	}
	
	/**
	 * Empties both stacks
	 */
	private static void emptyStacks() {
		// Empties the operand stack
		while (!operandStack.empty()) {
			operandStack.pop();
		}
		// Empties the operator stack
		while (!operatorStack.empty()) {
			operatorStack.pop();
		}
	}
}