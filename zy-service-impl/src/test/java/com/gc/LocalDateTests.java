package com.zy;

import java.text.ParseException;
import java.time.*;
import java.util.Date;

/**
 * Created by freeman on 16/7/15.
 */
public class LocalDateTests {

	public void testLocalDate() throws ParseException {
		Period between = Period.between(LocalDate.of(2016,8,4),LocalDate.of(2016,8,3));
		System.out.println(between.getDays());
		Instant instant = new Date().toInstant();
		LocalDate localDate = LocalDateTime.ofInstant(instant,ZoneId.systemDefault()).toLocalDate();
		System.out.println(localDate);
	}

}
