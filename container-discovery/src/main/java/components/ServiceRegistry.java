package components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ServiceRegistry {
	private static final Set<ServiceInstance> EMPTY = Collections.emptySet();
	private final Map<String, Set<ServiceInstance>> registry;

	public ServiceRegistry() {
		registry = new ConcurrentHashMap<>();
	}

	public void add(String serviceName, ServiceInstance instance) {
		registry.computeIfAbsent(serviceName, i -> new HashSet<>()).add(instance);
	}

	public Set<ServiceInstance> get(String serviceName) {
		return registry.getOrDefault(serviceName, EMPTY);
	}

}
