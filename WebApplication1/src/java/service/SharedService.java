/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.Document;
import entity.Shared;
import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class SharedService extends AbstractFacade<Shared> {
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SharedService() {
        super(Shared.class);
    }
    
    public List<User> findUsersByDocument (Long documentId){
        TypedQuery<User> query = em.createNamedQuery("Shared.findUsersByDocument", User.class);
        query.setParameter("document_id", documentId);
        
        return query.getResultList();
    }
    
    public List<Document> findDocumentsByUser(Long userId) {
        TypedQuery<Document> query = em.createNamedQuery("Shared.findDocumentsByUser", Document.class);
        query.setParameter("user_id", userId);
        
        return query.getResultList();
    }

    public Shared findByUserAndDocument(Long userId, Long documentId) {
        TypedQuery<Shared> query = em.createNamedQuery("Shared.findByUserAndDocument", Shared.class);
        query.setParameter("user_id", userId)
             .setParameter("document_id", documentId);
        List<Shared> results = query.getResultList();
        
        if (results.isEmpty())return null;
        return results.get(0);     
    }
}