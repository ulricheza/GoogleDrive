/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.Starred;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class StarredService extends AbstractFacade<Starred> {
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StarredService() {
        super(Starred.class);
    }

    public Starred find(Long ownerId, Long documentId) {
        TypedQuery<Starred> query = em.createNamedQuery("Starred.findByOwnerAndDocument", Starred.class);
        query.setParameter("owner_id", ownerId)
             .setParameter("document_id", documentId);
        
        List<Starred> results = query.getResultList();        
        if (results.isEmpty())
            return null;
        else
            return results.get(0);        
    }
}