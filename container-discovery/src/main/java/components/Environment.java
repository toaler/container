package components;

import org.springframework.stereotype.Component;

@Component
public class Environment {
	private String type;
	
	public Environment(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
