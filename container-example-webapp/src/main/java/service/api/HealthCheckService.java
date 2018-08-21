package service.api;

public interface HealthCheckService {
	String getStatus();

	void setStatus(String status);
}
