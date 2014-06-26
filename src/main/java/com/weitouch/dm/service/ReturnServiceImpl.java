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
import com.weitouch.dm.pojo.Return;
import com.weitouch.dm.pojo.Sale;

@Service("jpaReturnService")
public class ReturnServiceImpl extends AbstractService implements ReturnService {

	private static Logger log = LoggerFactory.getLogger(ReturnServiceImpl.class);

	public List<Return> listReturns(int pageNum, Distributor distributor) {

		TypedQuery<Return> query = em.createNamedQuery("Return.findByDistId",
				Return.class);

		query.setParameter("distId", distributor.getId());

		query.setFirstResult((pageNum - 1) * Constants.QUERY_PAGE_SIZE);
		query.setMaxResults(Constants.QUERY_PAGE_SIZE);

		return query.getResultList();

	}

	public Long getDistributorReturnCounts(Distributor distributor) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Return> o = cq.from(Return.class);
		cq.select(cb.count(o));

		List<Predicate> criteria = new ArrayList<Predicate>();

		if (distributor != null) {
			criteria.add(cb.equal(o.get("fromDistributor")
					.as(Distributor.class), distributor));
		}
		criteria.add(cb.equal((o.get("toDistributor").get("id")),1));

		cq.where(cb.and(criteria.toArray(new Predicate[0])));

		TypedQuery<Long> q = em.createQuery(cq);

		return q.getSingleResult();

	}

}
