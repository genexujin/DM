package com.weitouch.dm.pojo;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value="receipt")
public class Receipt extends Transaction {

	
}
