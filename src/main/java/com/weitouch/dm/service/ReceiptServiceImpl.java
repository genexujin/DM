package com.weitouch.dm.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weitouch.dm.Constants;
import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Sale;
import com.weitouch.dm.pojo.Shipment;





@Service("jpaReceiptService")
public class ReceiptServiceImpl extends AbstractService implements ReceiptService{
	
	private static Logger log = LoggerFactory.getLogger(ReceiptServiceImpl.class);

	public List<Shipment> listShipment(int pageNum, Distributor distributor) {

		TypedQuery<Shipment> query = em.createNamedQuery("Shipment.findByDistId",
				Shipment.class);

		query.setParameter("distId", distributor.getId());

		query.setFirstResult((pageNum - 1) * Constants.QUERY_PAGE_SIZE);
		query.setMaxResults(Constants.QUERY_PAGE_SIZE);

		return query.getResultList();

	}

	public Long getDistributorShipmentCounts(Distributor distributor) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Shipment> o = cq.from(Shipment.class);
		cq.select(cb.count(o));

		List<Predicate> criteria = new ArrayList<Predicate>();

		if (distributor != null) {
			criteria.add(cb.equal(o.get("toDistributor")
					.as(Distributor.class), distributor));
		}
		
		cq.where(criteria.get(0));

		TypedQuery<Long> q = em.createQuery(cq);

		return q.getSingleResult();

	}
		
}
