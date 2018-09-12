package org.javaee7.extra.camel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;

@Named("myProducer")
public class MyProducer {
	
	private static final Logger LOGGER = Logger.getLogger(MyProducer.class.getName());
	
	@Inject
	@Uri("webspheremq:queue:JACO_ESB_REQ.COPY")
	Endpoint endpoint;

	@Inject
	@ContextName("cdi-context")
	private CamelContext context;

	public void sendJmsMessage() throws Exception {
		ProducerTemplate template = context.createProducerTemplate();
		for (int i = 0; i < 10; i++) {
			template.asyncSendBody(endpoint, "Test Message: " + i);
		}
		LOGGER.log(Level.INFO, "sendJmsMessage - done processing");
	}

}
