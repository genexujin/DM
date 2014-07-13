package com.weitouch.dm.service;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weitouch.dm.Constants;
import com.weitouch.dm.pojo.Inventory;
import com.weitouch.dm.pojo.Item;

@Service("jpaItemService")
public class ItemServiceImpl extends AbstractService implements ItemService {

	private static Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Override
	public Item saveItem(Item item) {
		if (item.getId() == null) {
			log.info("Inserting new item ...");
			em.persist(item);
		} else {
			log.info("Updating item...");
			em.merge(item);
		}
		log.info("Item saved with id: " + item.getId());
		return item;

	}

	public List<Item> findItemsByCode(String code) {

		code = code.toLowerCase();
		TypedQuery<Item> query = em.createNamedQuery("Item.findByItemCode",
				Item.class);
		query.setParameter("code", "%" + code + "%");
		List<Item> items = query.getResultList();
		return items;
	}

	public List<Item> findItemsByCode(int pageNum, String code) {

		code = code.toLowerCase();
		TypedQuery<Item> query = em.createNamedQuery("Item.findByItemCode",
				Item.class);
		query.setParameter("code", "%" + code + "%");
		query.setFirstResult((pageNum - 1) * Constants.QUERY_PAGE_SIZE);
		query.setMaxResults(Constants.QUERY_PAGE_SIZE);
		List<Item> items = query.getResultList();
		return items;
	}

	public Long countItemsByCode(String code) {

		code = code.toLowerCase();
		TypedQuery<Long> query = em.createNamedQuery("Item.countByItemCode",
				Long.class);
		query.setParameter("code", "%" + code + "%");
		Long count = query.getSingleResult();

		return count;
	}

}
