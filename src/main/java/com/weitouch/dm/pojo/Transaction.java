package com.weitouch.dm.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;



@Entity
@Table(name = "shipments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ship_type", discriminatorType = DiscriminatorType.STRING)
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@Column(name = "ship_no")
	private String shipNo;
	
	
	
	@ManyToOne
    @JoinColumn(name="from_distributor_id")
    private Distributor fromDistributor;
	
	@ManyToOne
    @JoinColumn(name="to_distributor_id")
    private Distributor toDistributor;
	
	@Column(name = "ship_date")
	private Date shipDate;
	
	private String remark;
	
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,
            CascadeType.MERGE }, mappedBy = "shipments")
    //@OrderBy("price ASC")
    private Set<ShipLine> lines = new HashSet<ShipLine>();
	
	public double getTotalPrice(){
		
		double totalPrice = 0;
		
		for(ShipLine line: lines){
			totalPrice += line.getPrice()* line.getAmount();
		}
		
		return totalPrice;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getShipNo() {
		return shipNo;
	}

	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}

	

	public Distributor getFromDistributor() {
		return fromDistributor;
	}

	public void setFromDistributor(Distributor fromDistributor) {
		this.fromDistributor = fromDistributor;
	}

	public Distributor getToDistributor() {
		return toDistributor;
	}

	public void setToDistributor(Distributor toDistributor) {
		this.toDistributor = toDistributor;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<ShipLine> getLines() {
		return lines;
	}

	public void setLines(Set<ShipLine> lines) {
		this.lines = lines;
	}
	
	
	
}
