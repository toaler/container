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
	private final Map<String, Set<ServiceInstance>> registryByRepo;

	public ServiceRegistry() {
		registry = new ConcurrentHashMap<>();
		registryByRepo = new ConcurrentHashMap<>();
	}

	public void add(String serviceName, ServiceInstance instance) {

		Set<ServiceInstance> services = registry.get(serviceName);
		if (services == null) {
			registry.put(serviceName, services = new HashSet<ServiceInstance>());
		}
		services.remove(instance);
		services.add(instance);

		String repo = instance.getServiceRepoName();

		if (repo != null) {
			services = registryByRepo.get(repo);
			if (services == null) {
				registryByRepo.put(repo, services = new HashSet<ServiceInstance>());
			}
			services.remove(instance);
			services.add(instance);
		}
	}

	public Set<ServiceInstance> get(String serviceName) {
		return registry.getOrDefault(serviceName, EMPTY);
	}

	public Set<ServiceInstance> getByRepo(String repo) {
		return registryByRepo.getOrDefault(repo, EMPTY);
	}
}