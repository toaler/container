package components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tags {

	private String az;
	private String region;
	private String instanceId;
	private boolean canary = false;
	private int loadBalancingWeight = 100;

	public Tags() {
		
	}
	protected Tags(String az, String region, String instanceId, boolean canary, int loadBalancingWeight) {
		this.az = az;
		this.region = region;
		this.instanceId = instanceId;
		this.canary = canary;
		this.loadBalancingWeight = loadBalancingWeight;
	}

	@JsonProperty("az")
	public String getAz() {
		return az;
	}

	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	@JsonProperty("instance_id")
	public String getInstanceId() {
		return instanceId;
	}

	public void setAz(String az) {
		this.az = az;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setCanary(boolean canary) {
		this.canary = canary;
	}

	public void setLoadBalancingWeight(int loadBalancingWeight) {
		this.loadBalancingWeight = loadBalancingWeight;
	}

	@JsonProperty("canary")
	public boolean isCanary() {
		return canary;
	}

	@JsonProperty("load_balancing_weight")
	public int getLoadBalancingWeight() {
		return loadBalancingWeight;
	}

	@Override
	public String toString() {
		return "Tags [az=" + az + ", region=" + region + ", instanceId=" + instanceId + ", canary=" + canary
				+ ", loadBalancingWeight=" + loadBalancingWeight + "]";
	}
}
