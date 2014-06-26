package com.weitouch.dm.service;

import java.util.List;

public interface BaseService {
	
	public <T> List<T> listItems(int pageNum, Class<T> type);
	
	public <T> Long getCounts( Class<T> type);
	
	public <T> T findById(int id, Class<T> type);
	
	public <T> void delete(long id, Class<T> type);
	
	public <T> T save(T object, Class<T> type);
	
	public <T> List<T> findAll( Class<T> type);

}
