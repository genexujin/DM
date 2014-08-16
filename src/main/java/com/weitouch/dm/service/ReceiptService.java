package com.weitouch.dm.service;

import java.util.List;

import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Receipt;
import com.weitouch.dm.pojo.Shipment;



public interface ReceiptService extends BaseService{
	
	public Long getDistributorShipmentCounts(Distributor distributor);
	public List<Shipment> listShipment(int pageNum, Distributor distributor);
		
}
