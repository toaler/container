package components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceMetadata {


	private String env;
	private ServiceInstance[] instances;
	private String service;
	
	public ServiceMetadata(String env, String service, ServiceInstance[] instances) {
		this.env = env;
		this.service = service;
		this.instances = instances;
	}
	
	public String getEnv() {
		return env;
	}

	@JsonProperty("hosts")
	public ServiceInstance[] getServiceInstances() {
		return instances;
	}

	public String getService() {
		return service;
	}
}