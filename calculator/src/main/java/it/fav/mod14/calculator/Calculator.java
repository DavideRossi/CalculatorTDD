package it.fav.mod14.calculator;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	private static final String FIRST_RE = "^\\d{1,3}";
	private static final String REST_RE = "^(\\+|\\-|\\*|\\/|\\*\\*)(\\d{1,3})";

	static class OperatorFactory {
		private static OperatorFactory instance = new OperatorFactory();
		public static OperatorFactory getInstance() {
			return instance;
		}
		private Map<String, BiFunction<Integer, Integer, Integer>> operatorMap;
		private OperatorFactory() {
			this.operatorMap = Map.of(
					"+", (op1, op2) -> op1 + op2,
					"-", (op1, op2) -> op1 - op2,
					"*", (op1, op2) -> op1 * op2,
					"/", (op1, op2) -> op1 / op2,
					"**", (op1, op2) -> (int)(Math.pow(op1, op2) + 0.5)
			);
		}
		public BiFunction<Integer, Integer, Integer> create(String operator) {
			return this.operatorMap.get(operator);
		}
	}

	public int compute(String expression) {
		if(expression == null) {
			throw new IllegalArgumentException();
		} else {
			int result = -1;
			boolean firstToken = true;
			Pattern pattern = Pattern.compile(FIRST_RE);
			while(!"".equals(expression)) {
				Matcher matcher = pattern.matcher(expression);
				if(!matcher.find()) {
					throw new IllegalArgumentException();
				}
				expression = expression.substring(matcher.end());
				if(firstToken) {
					firstToken = false;
					result = Integer.parseInt(matcher.group());
					pattern = Pattern.compile(REST_RE);
				} else {
					String operatorString = matcher.group(1);
					String valueString = matcher.group(2);
					BiFunction<Integer, Integer, Integer> operator = OperatorFactory.getInstance().create(operatorString);
					result = operator.apply(result, Integer.parseInt(valueString));
				}
			}
			return result;
		}
	}
}
