package org.javaee7.extra.camel;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.QueueConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.jms.JmsComponent;


@ApplicationScoped
@Startup
@ContextName("cdi-context")
public class Bootstrap extends RouteBuilder {

    @Resource(lookup = "jms/acmaQCF")
    private QueueConnectionFactory connectionFactory;
    
    @Inject
    @ContextName("cdi-context")
    CamelContext context;
	
	@PostConstruct
	public void init() {
		context.addComponent("webspheremq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
	}

	@Override
	public void configure() throws Exception {
		
		onException(Exception.class).handled(true).maximumRedeliveries(1).to("direct:handleFail");

		from("timer://timer1?period=1000").routeId("InitialRoute").startupOrder(1)
		.to("bean:helloCamel?method=sayHello(\"Camel User\")") // invoke bean every second
		.log(">> Response : ${body}");
		
		from ("webspheremq:queue:JACO_ESB_REQ?concurrentConsumers=5").routeId("ReadJMSRoute").startupOrder(2) // use 5 async consumers
		.to("log:org.javaee7.extra.camel?showAll=true&multiline=true") //log jms message to console
		.to("bean:myConsumer?method=process") //pretty print the message in a bean class
        .to("webspheremq:queue:JACO_ESB_RSP") //move to another queue
        .log("finished routing queue");
		
		from("timer://timer1?period=60000").routeId("SendJMSRoute").startupOrder(3)
		.to("bean:myProducer?method=sendJmsMessage") // write jms message to wmq queue every 60 seconds
		.log("finished sending messages to queue");
		
	}

}
