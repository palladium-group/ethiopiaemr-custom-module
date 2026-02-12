package org.openmrs.module.ethiopiaemrcustommodule.api.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Encounter;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutbox;

import java.util.List;

public class PrescriptionOutboxDao {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public PrescriptionOutbox savePrescriptionOutbox(PrescriptionOutbox outbox) {
		sessionFactory.getCurrentSession().saveOrUpdate(outbox);
		return outbox;
	}
	
	public PrescriptionOutbox getPrescriptionOutboxByUuid(String uuid) {
		return (PrescriptionOutbox) sessionFactory.getCurrentSession().createCriteria(PrescriptionOutbox.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<PrescriptionOutbox> getPendingPrescriptions(Integer limit) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PrescriptionOutbox.class);
		crit.add(Restrictions.eq("status", "PENDING"));
		crit.addOrder(Order.asc("dateCreated"));
		if (limit != null) {
			crit.setMaxResults(limit);
		}
		return crit.list();
	}
	
	public PrescriptionOutbox getLatestOutboxByEncounter(Encounter encounter) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PrescriptionOutbox.class);
		crit.add(Restrictions.eq("encounter", encounter));
		crit.addOrder(Order.desc("dateCreated"));
		crit.setMaxResults(1);
		return (PrescriptionOutbox) crit.uniqueResult();
	}
	
}
