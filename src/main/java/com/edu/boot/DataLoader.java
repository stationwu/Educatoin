package com.edu.boot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.edu.controller.WxErrorController;
import com.edu.dao.CourseRepository;
import com.edu.domain.Course;

@Component

public class DataLoader {
	private static final Logger logger = LoggerFactory.getLogger(WxErrorController.class);

	@Bean
	@Transactional
	public CommandLineRunner initialiseDataProviders(CourseRepository repository) {
		return (args) -> {
			if (0 == repository.count()) {
				LocalDate localDate = LocalDate.now();
				LocalTime timeFrom = LocalTime.of(9, 0);
				LocalTime timeTo = LocalTime.of(18, 0);
				LocalDateTime localDateTimeFrom = LocalDateTime.of(localDate, timeFrom);
				LocalDateTime localDateTimeTo = LocalDateTime.of(localDate, timeTo);
				ZoneId zone = ZoneId.systemDefault();
				Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
				Instant instantFrom = localDateTimeFrom.atZone(zone).toInstant();
				Instant instantTo = localDateTimeTo.atZone(zone).toInstant();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				for (int i = 0; i < 100; i++) {
					LocalDate date = localDate.plusDays(i);
					repository.save(new Course(date.toString(), date.toString(), timeFormat.format(Date.from(instantFrom)),
							timeFormat.format(Date.from(instantTo))));
				}
			}
		};
	}
}
