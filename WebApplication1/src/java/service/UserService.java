/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UserService extends AbstractFacade<User> {
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserService() {
        super(User.class);
    }
    
    public boolean signIn(User user){
        
        User registeredUser = findByLogin(user.getLogin());        
        if (registeredUser == null)
            return false;
        else
            return registeredUser.getPassword().equals(user.getPassword()); 
    }
    
    public User findByLogin(String login){
        
        TypedQuery<User> query = em.createNamedQuery("User.findByLogin", User.class);
        query.setParameter("login", login);        
        List<User> results = query.getResultList();
        
        if (results.isEmpty())
            return null;
        else
            return results.get(0);        
    }
}