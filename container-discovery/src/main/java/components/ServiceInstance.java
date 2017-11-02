package components;

public class ServiceInstance {
	private final String ip;
	private final String serviceRepoName;
	private final int port;
	private final String revision;
	private final Tags tags;

	public ServiceInstance(String ip, String serviceRepoName, int port, String revision, Tags tags) {
		this.ip = ip;
		this.serviceRepoName = serviceRepoName;
		this.port = port;
		this.revision = revision;
		this.tags = tags;
	}

	public String getIp() {
		return ip;
	}

	public String getServiceRepoName() {
		return serviceRepoName;
	}

	public int getPort() {
		return port;
	}

	public String getRevision() {
		return revision;
	}

	public Tags getTags() {
		return tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		result = prime * result + ((revision == null) ? 0 : revision.hashCode());
		result = prime * result + ((serviceRepoName == null) ? 0 : serviceRepoName.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
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
		if (revision == null) {
			if (other.revision != null)
				return false;
		} else if (!revision.equals(other.revision))
			return false;
		if (serviceRepoName == null) {
			if (other.serviceRepoName != null)
				return false;
		} else if (!serviceRepoName.equals(other.serviceRepoName))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

}
