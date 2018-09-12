package org.javaee7.extra.camel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Named("myConsumer")
public class MyConsumer implements Processor {

	private static final Logger LOGGER = Logger.getLogger(MyConsumer.class.getName());

	public void process(Exchange exchange) throws Exception {
		LOGGER.log(Level.INFO, "Entering MyConsumer:process");
		LOGGER.log(Level.INFO, "{0}", exchange.getIn().getBody().getClass());
		LOGGER.log(Level.INFO, "{0}", exchange.getIn().getBody());
	}

}
