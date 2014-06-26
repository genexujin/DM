package com.weitouch.dm.service;

import java.util.List;

import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Inventory;
import com.weitouch.dm.pojo.Item;


public interface InventoryService extends BaseService{
	public void receivGoods(Item item, int amount);
	public void receivGoods(Distributor dist, Item item, int amount);	
	public Long getDistributorItemsCounts( Distributor distributor);
	public  List<Inventory> listItems(int pageNum, Distributor distributor);
}
