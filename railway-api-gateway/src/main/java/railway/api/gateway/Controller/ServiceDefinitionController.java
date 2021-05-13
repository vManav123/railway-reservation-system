package railway.api.gateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import railway.api.gateway.Configuration.SwaggerConfiguration.ServiceDefinitionsContext;


@RestController
public class ServiceDefinitionController {
	
	@Autowired
	private ServiceDefinitionsContext definitionContext;
	
	@GetMapping("/service/{serviceName}")
	public String getServiceDefinition(@PathVariable String serviceName){
		
		return definitionContext.getSwaggerDefinition(serviceName);
		
	}
}
