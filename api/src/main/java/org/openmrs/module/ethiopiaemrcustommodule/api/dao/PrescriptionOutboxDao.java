package org.openmrs.module.ethiopiaemrcustommodule.api.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
		return (PrescriptionOutbox) sessionFactory.getCurrentSession().createCriteria(PrescriptionOutbox.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
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
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PrescriptionOutbox.class);
		crit.add(Restrictions.eq("encounter", encounter));
		crit.addOrder(Order.desc("dateCreated"));
		crit.setMaxResults(1);
		return (PrescriptionOutbox) crit.uniqueResult();
	}
	
}
