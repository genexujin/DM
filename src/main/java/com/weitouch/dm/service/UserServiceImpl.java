package com.weitouch.dm.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weitouch.dm.pojo.Users;


@Service("jpaUserService")
public class UserServiceImpl extends AbstractService implements UserService {
    
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    
    @Override
    public List<Users> findAll() {
        log.info("Now is in user findAll()");
        List<Users> users = em.createNamedQuery("User.findAll", Users.class).getResultList();
        return users;
    }

    @Override
    public Users save(Users user) {
        if (user.getId() == null) {
            log.info("Inserting new record...");
            em.persist(user);
        } else {
            log.info("Updating new record...");
            em.merge(user);
        }
        
        log.info("Record saved with id: " + user.getId());
        return user;
    }

    @Override
    public void delete(Users user) {
        Users mergedUser = em.merge(user);
        em.remove(mergedUser);
        log.info("Record with id: " + user.getId() + " deleted successfully");
        
    }

	@SuppressWarnings("unchecked")
	@Override
	public Users findByName(String name) {
		
    	log.info("Now is find by the user name method...");
    	String hql = "from Users where name =:name";
    	Query query = em.createQuery(hql);
    	query.setParameter("name", name);
    	List<Users> users =  query.getResultList();
    	if(users.size() == 0){
    		return null;
    	}else{
    		return users.get(0);
    	}
	}
	
	


}
