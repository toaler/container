package service.impl;


import org.springframework.stereotype.Component;

import service.api.HealthCheckService;

@Component(value = "HealthCheckService")
public class HealthCheckServiceImpl implements HealthCheckService {
	
	private Status status;
	
	public HealthCheckServiceImpl() {
		System.out.println("IN HealthCheckServiceImpl ctor");
		status = Status.ALIVE;
	}

	@Override
	public String getStatus() {
		return status.name();
	}
	
	@Override
	public void setStatus(String status) {
		Status.valueOf(status);
	}
	
	private enum Status  {
		ALIVE, DEAD;
	}
}
