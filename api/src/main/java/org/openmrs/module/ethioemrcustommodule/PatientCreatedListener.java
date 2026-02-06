package org.openmrs.module.ethioemrcustommodule;

import org.openmrs.api.context.Context;
import org.openmrs.event.EventListener;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PatientCreatedListener implements EventListener {
	
	private static final Log log = LogFactory.getLog(PatientCreatedListener.class);
	
	@Override
	public void onMessage(Message message) {
		// Use log.error so it definitely shows up regardless of log levels
		log.info("*************************************************");
		log.info("EthioEMR Custom Module! Event caught by Subscriber!");
		log.info("*************************************************");
	}
}
