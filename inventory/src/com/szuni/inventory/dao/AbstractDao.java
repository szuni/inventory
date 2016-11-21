package com.szuni.inventory.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.szuni.inventory.model.DBO;

import inventory.Activator;

public abstract class AbstractDao<DBO_CLASS extends DBO> {

	protected final EntityManager entityManager;
	private EntityTransaction transaction;
	private final Class<DBO_CLASS> dboClass;

	protected AbstractDao(Class<DBO_CLASS> dboClass) {
		this.dboClass = dboClass;
		entityManager = Activator.getEntityManager();
	}

	public List<DBO_CLASS> findAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DBO_CLASS> cq = cb.createQuery(dboClass);
		Root<DBO_CLASS> rootEntry = cq.from(dboClass);
		CriteriaQuery<DBO_CLASS> all = cq.select(rootEntry);
		TypedQuery<DBO_CLASS> allQuery = entityManager.createQuery(all);

		return allQuery.getResultList();
	}

	public DBO_CLASS findById(Object id) {
		return entityManager.find(dboClass, id);
	}

	public DBO_CLASS findByAttribute(String attributeName, Object attributeValue) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DBO_CLASS> cq = cb.createQuery(dboClass);
		Root<DBO_CLASS> rootEntry = cq.from(dboClass);
		CriteriaQuery<DBO_CLASS> query = cq.select(rootEntry)
				.where(cb.equal(rootEntry.get(attributeName), attributeValue));
        return entityManager.createQuery(query).getSingleResult();
	}

	public void save(DBO_CLASS dbo) {
		transaction = entityManager.getTransaction();
		transaction.begin();
		if (dbo.getId() != null) {
			entityManager.merge(dbo);
		} else {
			entityManager.persist(dbo);
		}
		transaction.commit();
	}

}