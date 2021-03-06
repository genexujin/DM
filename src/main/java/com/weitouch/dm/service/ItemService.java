package com.weitouch.dm.service;

import java.util.List;

import com.weitouch.dm.pojo.Item;


public interface ItemService extends BaseService{
	public Item saveItem(Item item);	
	public List<Item> findItemsByCode(String code);
	public List<Item> findItemsByCode(int pageNum, String code);
	public Long countItemsByCode(String code);
}
