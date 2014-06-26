package com.weitouch.dm.service;

import java.util.List;

import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Return;



public interface ReturnService extends BaseService{
	public List<Return> listReturns(int pageNum, Distributor distributor) ;
	public Long getDistributorReturnCounts(Distributor distributor);
}
