package com.weitouch.dm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public abstract class BaseController {
	
	  @Autowired
	  protected JpaTransactionManager tx;
	  
	  protected TransactionStatus status;
	  
	  protected void beginTransaction(){
		  status =  tx.getTransaction(new DefaultTransactionDefinition());  
	  }
	  
	  protected void commitTransction(){
		  tx.commit(status);
	  }
	  
	  protected void rollbackTransction(){
		  tx.rollback(status);
	  }

}
