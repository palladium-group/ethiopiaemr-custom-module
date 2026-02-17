package org.openmrs.module.ethiopiaemrcustommodule.api.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.openmrs.Encounter;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutbox;
import org.openmrs.module.ethiopiaemrcustommodule.PrescriptionOutboxStatus;

import java.util.Date;
import java.util.List;

public class PrescriptionOutboxDao {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public PrescriptionOutbox savePrescriptionOutbox(PrescriptionOutbox outbox) {
		outbox.setDateChanged(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(outbox);
		return outbox;
	}
	
	public PrescriptionOutbox getPrescriptionOutboxByUuid(String uuid) {
		String hql = "FROM PrescriptionOutbox p WHERE p.uuid = :uuid";
		
		Query<PrescriptionOutbox> query = sessionFactory.getCurrentSession().createQuery(hql, PrescriptionOutbox.class);
		query.setParameter("uuid", uuid);
		
		return query.uniqueResult();
	}
	
	public List<PrescriptionOutbox> getPendingPrescriptions(Integer limit) {
		String hql = "FROM PrescriptionOutbox p WHERE p.status = :status ORDER BY p.dateCreated ASC";
		
		Query<PrescriptionOutbox> query = sessionFactory.getCurrentSession().createQuery(hql, PrescriptionOutbox.class);
		
		query.setParameter("status", PrescriptionOutboxStatus.PENDING);
		
		if (limit != null && limit > 0) {
			query.setMaxResults(limit);
		}
		
		return query.getResultList();
	}
	
	public PrescriptionOutbox getLatestOutboxByEncounter(Encounter encounter) {
		String hql = "FROM PrescriptionOutbox p WHERE p.encounter = :encounter ORDER BY p.dateCreated DESC";
		
		return sessionFactory.getCurrentSession().createQuery(hql, PrescriptionOutbox.class)
		        .setParameter("encounter", encounter).setMaxResults(1).uniqueResult();
	}
	
}
