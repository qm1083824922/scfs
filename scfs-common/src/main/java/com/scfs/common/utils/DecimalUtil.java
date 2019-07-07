package com.scfs.common.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class DecimalUtil {

    private static final DecimalFormat FORMAT = new DecimalFormat("0.00000000");

    static {
        FORMAT.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    private static final int SCALE = 2;

    public static BigDecimal format(double value) {
        return new BigDecimal(FORMAT.format(value));
    }

    public static BigDecimal format(long value) {
        return new BigDecimal(FORMAT.format(value));
    }

    public static BigDecimal formatScale2(BigDecimal value) {
        return value.setScale(SCALE,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal format(BigDecimal value) {
        return value.setScale(8,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal add(BigDecimal d1, BigDecimal d2) {
        return format((null == d1 ? BigDecimal.ZERO : d1).add(null == d2 ? BigDecimal.ZERO : d2));
    }

    public static BigDecimal add(BigDecimal v1, BigDecimal... vs) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : vs) {
            sum = add(sum, v);
        }
        return add(v1, sum);
    }

    public static BigDecimal subtract(BigDecimal v1, BigDecimal... vs) {
        BigDecimal sum = add(BigDecimal.ZERO, vs);
        return subtract(v1, sum);
    }

    public static BigDecimal subtract(BigDecimal d1, BigDecimal d2) {
        return format((null == d1 ? BigDecimal.ZERO : d1).subtract(null == d2 ? BigDecimal.ZERO : d2));
    }

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        return format(v1.divide(v2, MathContext.DECIMAL128));
    }

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2, int roundingMode) {
        return v1.divide(v2, 2, roundingMode);
    }

    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        return format((null == v1 ? BigDecimal.ZERO : v1).multiply(null == v2 ? BigDecimal.ZERO : v2));
    }

    private static int compareBigDecimal(BigDecimal v1, BigDecimal v2) {
        return format(v1).compareTo(format(v2));
    }

    public static boolean gt(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) > 0;
    }

    public static boolean ge(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) >= 0;
    }

    public static boolean eq(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) == 0;
    }

    public static boolean lt(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) < 0;
    }

    public static boolean le(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) <= 0;
    }

    public static boolean ne(BigDecimal v1, BigDecimal v2) {
        return compareBigDecimal(v1, v2) != 0;
    }

	public static String toQuantityString(BigDecimal val) {
		if (val == null)
			return "0";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("###,###,###,##0.####");
		return format.format(val.setScale(4, BigDecimal.ROUND_HALF_UP));
	}

	public static String toQuantityString1(BigDecimal val) {
		if (val == null)
			return "0";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("###########0.####");
		return format.format(val.setScale(4, BigDecimal.ROUND_HALF_UP));
	}

	public static String toQuantityString(Long val) {
		if (val == null)
			return "0";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("###,###,###,##0.####");
		return format.format(new BigDecimal(val.longValue()).setScale(4,
				BigDecimal.ROUND_HALF_UP));
	}

	public static String toPriceString(BigDecimal val) {
		if (val == null)
			return "0.00";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("###,###,###,##0.00######");
		return format.format(val.setScale(8, BigDecimal.ROUND_HALF_UP));
	}

	public static String toRateString(BigDecimal val) {
		if (val == null)
			return "0.00";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("###,###,###,##0.#########");
		return format.format(val.setScale(9, BigDecimal.ROUND_HALF_UP));
	}

	public static String toAmountString(BigDecimal val) {
		if (val == null)
			return "0.00";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("###,###,###,##0.00");
		return format.format(val.setScale(2, BigDecimal.ROUND_HALF_UP));
	}

	public static String toDigitalString(BigDecimal val) {
		if (val == null)
			return "0.00";

		NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
		((DecimalFormat) format).applyPattern("############.#########");
		return format.format(val);
	}
	
	public static String toPercentString(BigDecimal val) {
		return toPercent(val).toString() + "%";
	}
	
	public static BigDecimal toPercent(BigDecimal val) {
		if (val == null)
			return BigDecimal.ZERO;
		val = DecimalUtil.multiply(val, new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
		return val;
	}
	
}
