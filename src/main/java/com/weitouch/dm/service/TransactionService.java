package com.weitouch.dm.service;

import java.util.Date;
import java.util.List;

import com.weitouch.dm.pojo.Receipt;
import com.weitouch.dm.pojo.Transaction;



public interface TransactionService extends BaseService{
	
	public List<Transaction> queryByDateRangeAndTxnType(String txnType, Date startDate, Date endDate);
	public List sumByDateTxnTypeWithDistributor(String txnType,
			Date startDate, Date endDate, Long id);
}
