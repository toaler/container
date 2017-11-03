package components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRepoMetadata extends BaseMetadata {
	private String serviceRepoName;
	
	public ServiceRepoMetadata(String env, String serviceRepoName, ServiceInstance[] instances) {
		super(env, instances);
		this.serviceRepoName = serviceRepoName;
	}
	
	@JsonProperty("service_repo_name")
	public String getServiceRepoName() {
		return serviceRepoName;
	}
}