package components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceInstance {
	private final String service;
	private final String ip;
	private final String serviceRepoName;
	private final int port;
	private final String revision;
	private final Tags tags;
	private final String lastCheckIn;

	public ServiceInstance(String service, String ip, String serviceRepoName, int port, String revision, Tags tags) {
		this.service = service;
		this.ip = ip;
		this.serviceRepoName = serviceRepoName;
		this.port = port;
		this.revision = revision;
		this.tags = tags;
		this.lastCheckIn = String.valueOf(System.currentTimeMillis());
	}
	
	@JsonProperty("service")
	public String getService() {
		return service;
	}
	
	@JsonProperty("last_check_in")
	public String getLastCheckIn() {
		return lastCheckIn;
	}

	@JsonProperty("ip_address")
	public String getIp() {
		return ip;
	}

	@JsonProperty("service_repo_name")
	public String getServiceRepoName() {
		return serviceRepoName;
	}

	@JsonProperty("port")
	public int getPort() {
		return port;
	}

	@JsonProperty("revision")
	public String getRevision() {
		return revision;
	}

	@JsonProperty("tags")
	public Tags getTags() {
		return tags;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		return result;
	}
 
	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		ServiceInstance other = (ServiceInstance) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}