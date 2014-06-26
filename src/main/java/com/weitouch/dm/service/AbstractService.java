package com.weitouch.dm.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weitouch.dm.Constants;




public abstract class AbstractService {
    
    @PersistenceContext
    protected EntityManager em;
    
    private static Logger log = LoggerFactory.getLogger(AbstractService.class);
    
    public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	protected Long getCount(String entityName) {
        return em.createQuery(getCountQueryString(entityName), Long.class).getSingleResult();
    }
    
    protected String getCountQueryString(String entityName) {
        return String.format(Constants.COUNT_QUERY_STRING, entityName, entityName);
    }
    
    protected String generateReadableId(Long id, String prefix) {
        return prefix + String.format("%1$08d", id);
    }
    
    public <T> List<T> listItems(int pageNum, Class<T> type){		
						
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		Root<T> o = cq.from(type);
		cq.select(o);
		
		TypedQuery<T> q = em.createQuery(cq);
		q.setFirstResult((pageNum - 1) * Constants.QUERY_PAGE_SIZE);
		q.setMaxResults(Constants.QUERY_PAGE_SIZE);	
		
		return q.getResultList();
		
	}
    
    
    public <T> List<T> findAll( Class<T> type){		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		Root<T> o = cq.from(type);
		cq.select(o);
		TypedQuery<T> q = em.createQuery(cq);
		return q.getResultList();
		
	}
    
    public <T> void delete(long id, Class<T> type){
    	T o = em.find(type, id);
    	em.remove(o);
    }
    
    public <T> T findById(int id, Class<T> type){
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		Root<T> o = cq.from(type);		
		List<Predicate> criteria = new ArrayList<Predicate>();
		criteria.add(cb.equal(o.get("id"), id));
		cq.where(criteria.get(0));		
		TypedQuery<T> q = em.createQuery(cq);
		List<T> result = q.getResultList();
		if(result!=null)
			return result.get(0);
		else
			return null;
    }
    
    public <T> Long getCounts( Class<T> type){
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> o = cq.from(type);
		cq.select(cb.count(o));
		
		TypedQuery<Long> q = em.createQuery(cq);
		
		return q.getSingleResult();
		
    	
    }
    
    
    public <T> T save(T object, Class<T> type) {
    	
    	try {

			Method m1 = type.getMethod("getId");
			Long id = (Long) m1.invoke(object);
			if (id == null) {
				log.info("Inserting new object of type: " + type);
				em.persist(object);
			} else {
				log.info("Updating object of type: " + type);
				em.merge(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
		
	}
    
    
   

}
