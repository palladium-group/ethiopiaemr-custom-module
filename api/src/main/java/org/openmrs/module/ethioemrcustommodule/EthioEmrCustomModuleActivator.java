/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ethioemrcustommodule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.DrugOrder;
import org.openmrs.event.Event;
import org.openmrs.event.EventListener;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class EthioEmrCustomModuleActivator extends BaseModuleActivator implements DaemonTokenAware {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private EventListener drugOrderCreatedListener;
	
	private EventListener drugOrderUpdatedListener;
	
	private DaemonToken daemonToken;
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Started EthioEMR Custom Module");
		// SUBSCRIBE HERE: Listen for Patient Created events
		Event.subscribe(Patient.class, Event.Action.CREATED.name(), new PatientCreatedListener());
		// SUBSCRIBE HERE: Listen for Drug Order Created events
		drugOrderCreatedListener = new DrugPrescriptionCreatedListener(daemonToken);
		Event.subscribe(DrugOrder.class, Event.Action.CREATED.name(), drugOrderCreatedListener);
		// SUBSCRIBE HERE: Listen for Drug Order Updated events
		drugOrderUpdatedListener = new DrugPrescriptionCreatedListener(daemonToken);
		Event.subscribe(DrugOrder.class, Event.Action.UPDATED.name(), drugOrderUpdatedListener);
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		if (drugOrderCreatedListener != null) {
			Event.unsubscribe("Drug order created", drugOrderCreatedListener);
		}
		if (drugOrderUpdatedListener != null) {
			Event.unsubscribe("Drug order updated", drugOrderUpdatedListener);
		}
		log.info("Shutdown EthioEMR Custom Module");
	}
	
	@Override
	public void setDaemonToken(DaemonToken token) {
		daemonToken = token;
	}
	
}
