package component;

import org.springframework.stereotype.Component;

@Component(value = "healthCheckService")
public class HealthCheckServiceImpl implements HealthCheckService {
	
	private Status status;
	
	public HealthCheckServiceImpl() {
		status = Status.ALIVE;
	}

	@Override
	public String getStatus() {
		return status.name();
	}
	
	@Override
	public void setStatus(String status) {
		this.status = Status.valueOf(status);
	}
	
	private enum Status  {
		ALIVE, DEAD;
	}
}