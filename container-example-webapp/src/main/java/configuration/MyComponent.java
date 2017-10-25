package configuration;

import org.springframework.stereotype.Component;

@Component(value = "MyComponent")
public class MyComponent {

	public String message() {
		return "This is Mycomponent";
	}
}
