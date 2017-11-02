package components;

public class ServiceMetadata {


	private String env;
	private ServiceInstance[] serviceInstances;
	private String service;
	
	public ServiceMetadata(String service) {
		this.service = service;
	}
	
	public String getEnv() {
		return env;
	}

	public ServiceInstance[] getServiceInstances() {
		return serviceInstances;
	}

	public String getService() {
		return service;
	}
}
