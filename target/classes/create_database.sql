-- select the database
USE dm;

CREATE  TABLE IF NOT EXISTS `dm`.`items` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `code` VARCHAR(100) NOT NULL ,
  `name` VARCHAR(200) NULL ,
  `model` VARCHAR(200) NULL,
  `remark` VARCHAR(500) NULL,
   PRIMARY KEY (`id`) );

CREATE  TABLE IF NOT EXISTS `dm`.`distributors` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(500) NULL,
  `address` VARCHAR(500) NULL,
  `phone` VARCHAR(50)  NULL , 
  `remark` VARCHAR(500) NULL,
  PRIMARY KEY (`id`) ,
  INDEX `distributors_idx` (`name` ASC));


CREATE  TABLE IF NOT EXISTS `dm`.`inventory` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `item_id`INT NOT NULL ,
  `distributor_id` INT NOT NULL ,
  `amount` INT NOT NULL ,  
  PRIMARY KEY (`id`) ,
  CONSTRAINT `item_fk`
    FOREIGN KEY (`item_id` )
    REFERENCES `dm`.`items` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE  TABLE IF NOT EXISTS `dm`.`shipments` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `ship_no` VARCHAR(100) NULL ,
  `from_distributor_id` INT NULL ,
  `to_distributor_id` INT NULL ,
  `ship_date` DATETIME NULL ,
  `ship_type` VARCHAR(100) NULL ,
  `remark` VARCHAR(500) NULL,
  PRIMARY KEY (`id`) );
  
 
  CREATE  TABLE IF NOT EXISTS `dm`.`shipment_line` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `ship_id` INT NOT NULL ,
  `item_id` INT NOT NULL ,
  `amount` INT NOT NULL ,
  `price` DECIMAL(10,2) NOT NULL , 
  `remark` VARCHAR(500) NULL,
  PRIMARY KEY (`id`) ,
  INDEX `ship_item_idx` (`item_id` ASC),
   CONSTRAINT `ship_fk`
    FOREIGN KEY (`ship_id` )
    REFERENCES `dm`.`shipments` (`id` )
    ON DELETE CASCADE
    ON UPDATE NO ACTION);  
    
    
CREATE  TABLE IF NOT EXISTS `dm`.`salelogs` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `distributor_id` INT NOT NULL ,
  `item_id` INT NOT NULL ,
  `amount` INT NOT NULL ,
  `price` DECIMAL(10,2) NOT NULL ,
  `selling_date` DATETIME NOT NULL ,
  `remark` VARCHAR(500) NULL,
  PRIMARY KEY (`id`) ,
  INDEX `saleorder_idx` (`distributor_id` ASC));
  
CREATE  TABLE IF NOT EXISTS `dm`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NULL ,
  `password` VARCHAR(200) NULL ,
  `distributor_id` INT NOT NULL,
  `is_master` VARCHAR(2) NOT NULL,
    PRIMARY KEY (`id`) );
  
  



