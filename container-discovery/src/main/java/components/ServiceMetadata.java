package components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceMetadata  extends BaseMetadata {
	private String service;
	
	public ServiceMetadata(String env, String service, ServiceInstance[] instances) {
		super(env, instances);
		this.service = service;
	}
	
	@JsonProperty("service")
	public String getService() {
		return service;
	}
}