package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

public abstract class Pregnancy {
	public static final int FIRST_TRIMESTER = 1;
	public static final int SECOND_TRIMESTER = 2;
	public static final int THIRD_TRIMESTER = 3;

	private int weeks = 0;
	private int days = 0;
	private Calendar startPoint = null, currentPoint = null;

	/**
	 * 
	 * @param start
	 *            start point of pregnancy, must be not null
	 */
	public Pregnancy(Calendar start) {
		this.startPoint = start;
		currentPoint = Calendar.getInstance();
		currentPoint.setTimeInMillis(start.getTimeInMillis());
	}

	/**
	 * 
	 * @param weeks
	 * @param days
	 * @param current
	 *            current date, must be not null
	 */
	public Pregnancy(int weeks, int days, Calendar current) {
		this.weeks = weeks;
		this.days = days;
		startPoint = (Calendar) current.clone();
		startPoint.add(Calendar.DAY_OF_MONTH, -getDurationInDays());
		currentPoint = current;
	}

	public void setCurrentPoint(Calendar current) {
		currentPoint = current;
		long difference = currentPoint.getTimeInMillis()
				- startPoint.getTimeInMillis();
		int days = (int) (difference / (1000l * 3600l * 24l));
		weeks = days / 7;
		this.days = days - weeks * 7;
	}

	public Calendar getCurrentPoint() {
		return currentPoint;
	}

	public Calendar getStartPoint() {
		return startPoint;
	}

	public Calendar getEndPoint() {
		Calendar endPoint = (Calendar) startPoint.clone();
		endPoint.add(Calendar.DAY_OF_MONTH, getFullDurationInDays());
		return endPoint;
	}

	/**
	 * Call {@code isCorrect()} before call of this method.
	 * 
	 * @return {@code true}, if value of weeks is less than
	 *         {@code getMaxUltrasoundAccuracy()}; {@code false} otherwise
	 */
	public boolean isAccurateForUltrasound() {
		return weeks < getMaxUltrasoundAccuracy();
	}

	/**
	 * Checker for the correctness of values of weeks and days.
	 * 
	 * @return {@code true}, if {@code weeks} and {@code days} values are
	 *         correct; {@code false}, otherwise
	 */
	public boolean isCorrect() {
		int duration = getDurationInDays();
		return duration >= 0 && duration <= getMaxDurationInDays();
	}

	/**
	 * 
	 * @return number of trimester, if weeks value is correct; -1, otherwise
	 */
	public int getTrimesterNumber() {
		if (weeks >= 0) {
			if (weeks <= getFirstTrimesterEndInclusive()) {
				return FIRST_TRIMESTER;
			} else if (weeks <= getSecondTrimesterEndInclusive()) {
				return SECOND_TRIMESTER;
			} else if (getDurationInDays() <= getMaxDurationInDays())
				return THIRD_TRIMESTER;
		}

		return -1;
	}

	public int getDurationInDays() {
		return weeks * 7 + days;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getDays() {
		return days;
	}

	public abstract int getFullDurationInDays();

	public int getFullDurationWeeks() {
		return getFullDurationInDays() / 7;
	}

	public int getFullDurationDays() {
		return getFullDurationInDays() - getFullDurationWeeks() * 7;
	}

	public int getRestInDays() {
		return getFullDurationInDays() - getDurationInDays();
	}

	public int getRestWeeks() {
		return getRestInDays() / 7;
	}

	public int getRestDays() {
		return getRestInDays() - getRestWeeks() * 7;
	}

	protected abstract int getMaxDurationInDays();

	protected abstract int getMaxUltrasoundAccuracy();

	protected abstract int getFirstTrimesterEndInclusive();

	protected abstract int getSecondTrimesterEndInclusive();
}