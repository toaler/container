package components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseMetadata {
	private String env;
	private ServiceInstance[] instances;
	
	public BaseMetadata(String env, ServiceInstance[] instances) {
		this.env = env;
		this.instances = instances;
	}
	
	public String getEnv() {
		return env;
	}

	@JsonProperty("hosts")
	public ServiceInstance[] getServiceInstances() {
		return instances;
	}
}