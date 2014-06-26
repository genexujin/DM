package com.weitouch.dm.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.weitouch.dm.Constants;
import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Inventory;
import com.weitouch.dm.pojo.Item;

@Service("jpaInventoryService")
public class InventoryServiceImpl extends AbstractService implements
		InventoryService {

	private static Logger log = LoggerFactory
			.getLogger(InventoryServiceImpl.class);
	
	/**
	 * 增加或者减少批发商的库存
	 */
	@Override
	public void receivGoods(Item item, int amount) {

		log.debug("Now is in receivGoods()");
		TypedQuery<Inventory> query = em.createNamedQuery(
				"Inventory.findByItemId", Inventory.class);
		query.setParameter("itemId", item.getId());
		query.setParameter("distId", Constants.HOME_INVENTORY_ID);// 需要数据库初始化时，设置home
																	// inventory
		List<Inventory> inventory = query.getResultList();
		
		
		if (!inventory.isEmpty()) {//如果已有库存
			Inventory inv = inventory.get(0);
			inv.setAmount(inv.getAmount() + amount);
			em.merge(inv);
		} else {//如果没有库存			
			Inventory newInv = new Inventory();
			Distributor homeDistributor = new Distributor();
			homeDistributor.setId(1L);
			newInv.setDistributor(homeDistributor);
			newInv.setItem(item);
			newInv.setAmount(amount);
			em.persist(newInv);
		}

	}
	
	/**
	 * 增加或者减少分销商的库存
	 */
	@Override
	public void receivGoods(Distributor dist, Item item, int amount) {
		log.debug("Now is in receivGoods()");
		TypedQuery<Inventory> query = em.createNamedQuery(
				"Inventory.findByItemId", Inventory.class);
		query.setParameter("itemId", item.getId());
		query.setParameter("distId", dist.getId());// 需要数据库初始化时，设置home
																	// inventory
		List<Inventory> inventory = query.getResultList();
		
		
		if (!inventory.isEmpty()) {//如果已有库存
			Inventory inv = inventory.get(0);
			inv.setAmount(inv.getAmount() + amount);
			em.merge(inv);
		} else {//如果没有库存			
			Inventory newInv = new Inventory();			
			newInv.setDistributor(dist);
			newInv.setItem(item);
			newInv.setAmount(amount);
			em.persist(newInv);
		}
		
	}
	
	public Long getDistributorItemsCounts( Distributor distributor){
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Inventory> o = cq.from(Inventory.class);
		cq.select(cb.count(o));
		
		List<Predicate> criteria = new ArrayList<Predicate>();
		
		if (distributor != null) {
            criteria.add(cb.equal(o.get("distributor").as(Distributor.class), distributor));
        }
		cq.where(criteria.get(0));
		
		TypedQuery<Long> q = em.createQuery(cq);
		
		return q.getSingleResult();
		
    	
    }
	
	
	public  List<Inventory> listItems(int pageNum, Distributor distributor){		
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
		Root<Inventory> o = cq.from(Inventory.class);
		cq.select(o);
		
		List<Predicate> criteria = new ArrayList<Predicate>();
		
		if (distributor != null) {
            criteria.add(cb.equal(o.get("distributor").as(Distributor.class), distributor));
        }
		cq.where(criteria.get(0));
		cq.orderBy(cb.desc(o.get("amount")));
		
		TypedQuery<Inventory> q = em.createQuery(cq);
		
		q.setFirstResult((pageNum - 1) * Constants.QUERY_PAGE_SIZE);
		q.setMaxResults(Constants.QUERY_PAGE_SIZE);	
		
		return q.getResultList();
		
	}

}
