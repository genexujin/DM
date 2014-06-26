package com.weitouch.dm.service;

import java.util.List;


import com.weitouch.dm.pojo.Users;


public interface UserService extends BaseService{
    
    public List<Users> findAll();
    
    public Users save(Users user);
    
    public void delete(Users user);
    
    public Users findByName(String mobile);

}
