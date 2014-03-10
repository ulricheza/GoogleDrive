/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.Remember;
import entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RememberService extends AbstractFacade<Remember> {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RememberService() {
        super(Remember.class);
    }
    
    public void deleteByUser (User user){        
        Query query = em.createNamedQuery("Remember.DeleteByUser");
        query.setParameter("user_id", user.getId());
        
        query.executeUpdate();
    }
}