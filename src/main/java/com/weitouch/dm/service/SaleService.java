package com.weitouch.dm.service;

import java.util.List;

import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Receipt;
import com.weitouch.dm.pojo.Sale;



public interface SaleService extends BaseService{
	public List<Sale> listSales(int pageNum, Distributor distributor) ;
	public Long getDistributorSaleCounts(Distributor distributor);
}
