package service.impl;

import java.time.Instant;

import org.springframework.stereotype.Component;

import service.api.TimeService;

@Component(value = "TimeService")
public class TimeServiceImpl implements TimeService {

	@Override
	public String getTime() {
		return Instant.now().toString();
	}
}
