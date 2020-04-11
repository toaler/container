package component;

public interface HealthCheckService {
    String getStatus();

    void setStatus(String status);
}