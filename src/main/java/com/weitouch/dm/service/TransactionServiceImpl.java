package com.weitouch.dm.service;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weitouch.dm.pojo.Transaction;
import com.weitouch.dm.pojo.Users;





@Service("jpaTransactionService")
public class TransactionServiceImpl extends AbstractService implements TransactionService{
	
	private static Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

	public List<Transaction> queryByDateRangeAndTxnType(String txnType, Date startDate, Date endDate){
		
		
		
		log.debug("start to query transaction with paramter:");
		log.debug("txnType= " + txnType);
		log.debug("startDate= " + startDate);
		log.debug("endDate= " + endDate);		
		
    	String hql = "from Transaction where ship_type =:txnType and shipDate>=:startDate and shipDate<=:endDate";
    	Query query = em.createQuery(hql);
    	query.setParameter("txnType", txnType);
    	query.setParameter("startDate", startDate);
    	query.setParameter("endDate", endDate);
    	List<Transaction> transactions =  query.getResultList();
    	
    	log.debug("transactions size: " + transactions.size());
    	
    	return transactions;		
	}
		
}
