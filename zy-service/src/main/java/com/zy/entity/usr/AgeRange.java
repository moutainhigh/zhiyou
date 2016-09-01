package com.gc.entity.usr;

import java.util.Calendar;
import java.util.Date;

public enum AgeRange {
	A17(Integer.MIN_VALUE, 17, "17岁以下"), A18_25(18, 25, "18至25岁"), A26_30(26, 30, "26至30岁"), A31_35(31, 35, "31至35岁"), A36_40(36, 40, "36至40岁"), A41_50(41, 50,
			"41至50岁"), A51(51, Integer.MAX_VALUE, "51岁以上");

	private AgeRange(int lowerLimit, int upperLimit, String label) {
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
		this.label = label;
	}

	private final int upperLimit;
	private final int lowerLimit;
	private final String label;

	public static AgeRange valueOf(Date birthday) {
		int age;
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday)) {
			return null;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		age = yearNow - yearBirth;

		if (monthNow < monthBirth) {
			age--;
		} else if (monthNow == monthBirth) {
			if (dayOfMonthNow < dayOfMonthBirth) {
				age--;
			}
		}

		for (AgeRange ageRange : AgeRange.values()) {
			if (age >= ageRange.lowerLimit && age <= ageRange.upperLimit) {
				return ageRange;
			}
		}
		return null;
	}

	public Integer getUpperLimit() {
		return upperLimit;
	}

	public Integer getLowerLimit() {
		return lowerLimit;
	}

	public String getLabel() {
		return label;
	}
	
}