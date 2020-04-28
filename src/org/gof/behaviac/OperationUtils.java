package org.gof.behaviac;

import java.util.List;
import java.util.function.Function;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean Compare(Object left, Object right, EOperatorType comparisonType) {

		boolean bLeftNull = (left == null);
		boolean bRightNull = (right == null);

		if (bLeftNull && bRightNull) // both are null
		{
			if (comparisonType == EOperatorType.E_EQUAL) {
				return true;
			} else if (comparisonType == EOperatorType.E_NOTEQUAL) {
				return false;
			} else {
				Debug.Check(false);
			}
		} else if (bLeftNull || bRightNull) // one is null and the other is not null
		{
			if (comparisonType == EOperatorType.E_EQUAL) {
				return false;
			} else if (comparisonType == EOperatorType.E_NOTEQUAL) {
				return true;
			} else {
				Debug.Check(false);
			}
		}

		if (left.getClass().isEnum()) {
			switch (comparisonType) {
			case E_EQUAL:
				return left.equals(right);

			case E_NOTEQUAL:
				return !left.equals(right);
			default:
				Debug.Check(false);
			}
		}

		if (left instanceof List) {
			var leftList = (List) left;
			var rightList = (List) right;
			Function<Void, Boolean> func = (Void) -> {
				if (leftList.size() != rightList.size())
					return false;

				for (int i = 0; i < leftList.size(); ++i) {
					var lv = leftList.get(i);
					var rv = rightList.get(i);
					if (!Compare(lv, rv, EOperatorType.E_EQUAL))
						return false;
				}
				return true;
			};

			switch (comparisonType) {
			case E_EQUAL: {
				return func.apply(null);
			}

			case E_NOTEQUAL: {
				return !func.apply(null);
			}
			default:
				Debug.Check(false);
			}
		}

		switch (comparisonType) {
		case E_EQUAL:
			return ((Comparable) left).compareTo(right) == 0;

		case E_NOTEQUAL:
			return ((Comparable) left).compareTo(right) != 0;

		case E_GREATER:
			return ((Comparable) left).compareTo(right) > 0;

		case E_GREATEREQUAL:
			return ((Comparable) left).compareTo(right) >= 0;

		case E_LESS:
			return ((Comparable) left).compareTo(right) < 0;

		case E_LESSEQUAL:
			return ((Comparable) left).compareTo(right) <= 0;

		default:
			Debug.Check(false);
			break;
		}
		return false;
	}

	public static <T> T Compute(T left, T right, EOperatorType computeType) {
		if (!(left instanceof Number)) {
			Debug.Check(false, left.getClass().getName());
		}
		var clazz = left.getClass();
		if (clazz == double.class || clazz == float.class) {
			switch (computeType) {
			case E_ADD:
				// return (T)(GetDoubleValue(left) + GetDoubleValue(right));
			case E_SUB:
			case E_MUL:
			case E_DIV:

			}
		}
		Debug.Check(false, "TODO Compute");
		return right;
	}

	public static long GetLongValue(Object v) {
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		var clazz = v.getClass();
		Debug.Check(false, clazz.getName());
		return 0;
	}

	public static double GetDoubleValue(Object v) {
		if (v instanceof Number) {
			return ((Number) v).doubleValue();
		}
		var clazz = v.getClass();
		Debug.Check(false, clazz.getName());
		return 0;
	}
}
