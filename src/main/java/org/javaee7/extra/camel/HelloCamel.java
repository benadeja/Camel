package org.javaee7.extra.camel;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.camel.cdi.ContextName;

@Named("helloCamel")
public class HelloCamel {
	
	@Inject
    @ContextName("cdi-context")
    private CamelContext context;

    public String sayHello(@Body String message) {
        return ">> Hello " + message + " user.";
    }
}
