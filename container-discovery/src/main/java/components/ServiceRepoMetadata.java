package components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRepoMetadata {

	private String env;
	private ServiceInstance[] instances;
	private String serviceRepoName;
	
	public ServiceRepoMetadata(String env, String serviceRepoName, ServiceInstance[] instances) {
		this.env = env;
		this.serviceRepoName = serviceRepoName;
		this.instances = instances;
	}
	
	@JsonProperty("env")
	public String getEnv() {
		return env;
	}

	@JsonProperty("hosts")
	public ServiceInstance[] getServiceInstances() {
		return instances;
	}

	@JsonProperty("service_repo_name")
	public String getServiceRepoName() {
		return serviceRepoName;
	}
}