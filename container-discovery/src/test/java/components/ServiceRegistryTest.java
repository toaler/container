package components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		reg.add(service, instance1);
		reg.add(service, instance2);
		
		Set<ServiceInstance> expectedSet = new HashSet<>(Arrays.asList(new ServiceInstance[] { instance1, instance2 }));
		Set<ServiceInstance> actualSet = reg.get(service);
		assertEquals(expectedSet, actualSet);
		
		instance1 = new ServiceInstance(service, ip, serviceRepoName, 8888, revision, tags);
		instance2 = new ServiceInstance(service, ip, serviceRepoName, 9999, revision, tags);
		reg.add("bar", instance1);
		reg.add("bar", instance2);
		
		expectedSet = new HashSet<>(Arrays.asList(new ServiceInstance[] { instance1, instance2 }));
		actualSet = reg.get("bar");
		assertEquals(expectedSet, actualSet);
	}
	
	@Test
	public void testRegistryDelete() {
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
		reg.add(instance1.getService(), instance1);
		
		Set<ServiceInstance> expectedSet = new HashSet<>(Arrays.asList(new ServiceInstance[] { instance1 }));
		Set<ServiceInstance> actualSet = reg.get(service);
		assertEquals(expectedSet, actualSet);
		assertEquals(1, reg.get(service).size());
		
		assertTrue(reg.delete(service, ip));
		assertEquals(0, reg.get(service).size());
		
	}
}
