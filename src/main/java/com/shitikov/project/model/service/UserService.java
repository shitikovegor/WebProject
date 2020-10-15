package com.shitikov.project.model.service;

import com.shitikov.project.model.entity.User;
import com.shitikov.project.model.entity.type.RoleType;
import com.shitikov.project.model.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    boolean add(Map<String, String> parameters) throws ServiceException;
    boolean remove(String login) throws ServiceException;
    boolean removeById(String id) throws ServiceException;
    Optional<User> findById(String id) throws ServiceException;
    Optional<User> findByLogin(String login) throws ServiceException;
    List<User> findAll() throws ServiceException;
    boolean checkLogin(String login) throws ServiceException;
    boolean checkPassword(String login, String password) throws ServiceException;
    RoleType findRole(String login) throws ServiceException;
    boolean updatePassword(String newPassword) throws ServiceException;
    boolean activate(String login) throws ServiceException;
}
