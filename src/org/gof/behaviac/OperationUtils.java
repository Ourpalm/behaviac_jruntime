package org.gof.behaviac;

public class OperationUtils {
	public static EOperatorType ParseOperatorType(String operatorType) {
		switch (operatorType) {
		case "Invalid":
			return EOperatorType.E_INVALID;

		case "Assign":
			return EOperatorType.E_ASSIGN;

		case "Assignment":
			return EOperatorType.E_ASSIGN;

		case "Add":
			return EOperatorType.E_ADD;

		case "Sub":
			return EOperatorType.E_SUB;

		case "Mul":
			return EOperatorType.E_MUL;

		case "Div":
			return EOperatorType.E_DIV;

		case "Equal":
			return EOperatorType.E_EQUAL;

		case "NotEqual":
			return EOperatorType.E_NOTEQUAL;

		case "Greater":
			return EOperatorType.E_GREATER;

		case "GreaterEqual":
			return EOperatorType.E_GREATEREQUAL;

		case "Less":
			return EOperatorType.E_LESS;

		case "LessEqual":
			return EOperatorType.E_LESSEQUAL;
		}

		Debug.Check(false);
		return EOperatorType.E_INVALID;
	}
}
