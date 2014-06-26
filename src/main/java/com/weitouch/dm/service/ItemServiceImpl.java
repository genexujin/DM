package com.weitouch.dm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import com.weitouch.dm.pojo.Item;





@Service("jpaItemService")
public class ItemServiceImpl extends AbstractService implements ItemService{
	
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

		
}
