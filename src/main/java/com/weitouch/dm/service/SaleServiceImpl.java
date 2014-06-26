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
import com.weitouch.dm.pojo.Inventory;
import com.weitouch.dm.pojo.Sale;

@Service("jpaSaleService")
public class SaleServiceImpl extends AbstractService implements SaleService {

	private static Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

	public List<Sale> listSales(int pageNum, Distributor distributor) {

		TypedQuery<Sale> query = em.createNamedQuery("Sale.findByDistId",
				Sale.class);

		query.setParameter("distId", distributor.getId());

		query.setFirstResult((pageNum - 1) * Constants.QUERY_PAGE_SIZE);
		query.setMaxResults(Constants.QUERY_PAGE_SIZE);

		return query.getResultList();

	}

	public Long getDistributorSaleCounts(Distributor distributor) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Sale> o = cq.from(Sale.class);
		cq.select(cb.count(o));

		List<Predicate> criteria = new ArrayList<Predicate>();

		if (distributor != null) {
			criteria.add(cb.equal(o.get("fromDistributor")
					.as(Distributor.class), distributor));
		}
		criteria.add(cb.isNull(o.get("toDistributor")));

		cq.where(cb.and(criteria.toArray(new Predicate[0])));

		TypedQuery<Long> q = em.createQuery(cq);

		return q.getSingleResult();

	}

}
