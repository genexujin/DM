package com.weitouch.dm.service;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weitouch.dm.pojo.Return;
import com.weitouch.dm.pojo.Shipment;
import com.weitouch.dm.pojo.Transaction;

@Service("jpaTransactionService")
public class TransactionServiceImpl extends AbstractService implements
		TransactionService {

	private static Logger log = LoggerFactory
			.getLogger(TransactionServiceImpl.class);

	public List<Transaction> queryByDateRangeAndTxnType(String txnType,
			Date startDate, Date endDate) {

		log.debug("start to query transaction with paramter:");
		log.debug("txnType= " + txnType);
		log.debug("startDate= " + startDate);
		log.debug("endDate= " + endDate);

		String hql = "from Transaction where ship_type =:txnType and shipDate>=:startDate and shipDate<=:endDate";
		Query query = em.createQuery(hql);
		query.setParameter("txnType", txnType);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Transaction> transactions = query.getResultList();

		log.debug("transactions size: " + transactions.size());

		return transactions;
	}

	// 查询供应商的出货/退货总数
	public List sumByDateTxnTypeWithDistributor(String txnType, Date startDate,
			Date endDate, Long id) {

		log.debug("start to sumByDateTxnTypeWithDistributor with paramter:");
		log.debug("txnType= " + txnType);
		log.debug("startDate= " + startDate);
		log.debug("endDate= " + endDate);
		log.debug("dist id= " + id);
		String hql = null;
		if (txnType.equals("shipment"))
			hql = "select l.item.code, sum(l.amount),  l.item.name, l.item.model,avg(l.price) from ShipLine l where "
					+ " l.shipments.class =:txnType "
					+ " and l.shipments.shipDate>=:startDate "
					+ " and l.shipments.shipDate<=:endDate "
					+ " and l.shipments.toDistributor.id =:id "
					+ " group by l.item.code";
		else
			hql = "select l.item.code, sum(l.amount),  l.item.name, l.item.model,avg(l.price) from ShipLine l where "
					+ " l.shipments.class =:txnType "
					+ " and l.shipments.shipDate>=:startDate "
					+ " and l.shipments.shipDate<=:endDate "
					+ " and l.shipments.fromDistributor.id =:id "
					+ " group by l.item.code";
		Query query = em.createQuery(hql);
		if (txnType.equals("shipment")) {
			query.setParameter("txnType", "shipment");
		} else {
			query.setParameter("txnType", "return");
		}
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("id", id);

		List lineSummaries = query.getResultList();

		log.debug("transactions size: " + lineSummaries.size());

		return lineSummaries;
	}

}
