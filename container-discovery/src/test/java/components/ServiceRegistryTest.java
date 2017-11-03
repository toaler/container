package components;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import components.ServiceInstance;
import components.ServiceRegistry;
import components.Tags;

public class ServiceRegistryTest {

	@Test
	public void testRegistryCreation() {
		ServiceRegistry reg = new ServiceRegistry();

		String service = "cat";
		String ip = "127.0.0.1";
		String serviceRepoName = "foo";
		String revision = "1213";

		String az = "az";
		String region = "region";
		String instanceId = "bar";
		boolean canary = false;
		int loadBalancingWeight = 100;

		Tags tags = new Tags(az, region, instanceId, canary, loadBalancingWeight);

		ServiceInstance instance1 = new ServiceInstance(service, ip, serviceRepoName, 8888, revision, tags);
		ServiceInstance instance2 = new ServiceInstance(service, ip, serviceRepoName, 9999, revision, tags);
		reg.add("foo", instance1);
		reg.add("foo", instance2);
		
		Set<ServiceInstance> expectedSet = new HashSet<>(Arrays.asList(new ServiceInstance[] { instance1, instance2 }));
		Set<ServiceInstance> actualSet = reg.get("foo");
		assertEquals(expectedSet, actualSet);
		
		instance1 = new ServiceInstance(service, ip, serviceRepoName, 8888, revision, tags);
		instance2 = new ServiceInstance(service, ip, serviceRepoName, 9999, revision, tags);
		reg.add("bar", instance1);
		reg.add("bar", instance2);
		
		expectedSet = new HashSet<>(Arrays.asList(new ServiceInstance[] { instance1, instance2 }));
		actualSet = reg.get("bar");
		assertEquals(expectedSet, actualSet);
		

	}
}
