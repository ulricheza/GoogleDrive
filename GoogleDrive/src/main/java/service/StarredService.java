/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.Document;
import entity.Starred;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class StarredService extends AbstractFacade<Starred> {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StarredService() {
        super(Starred.class);
    }

    public List<Document> findDocumentsByOwner(Long ownerId) {
        TypedQuery<Document> query = em.createNamedQuery("Starred.findDocumentsByOwner", Document.class);
        query.setParameter("owner_id", ownerId);
        
        return query.getResultList();   
    }
    
    public Starred findByOwnerAndDocument(Long ownerId, Long documentId) {
        TypedQuery<Starred> query = em.createNamedQuery("Starred.findByOwnerAndDocument", Starred.class);
        query.setParameter("owner_id", ownerId)
             .setParameter("document_id", documentId);
        
        List<Starred> results = query.getResultList();        
        if (results.isEmpty()) return null;
        return results.get(0);        
    }
    
    public void deleteByDocument(Long documentId) {
        Query query = em.createNamedQuery("Starred.deleteByDocument");
        query.setParameter("document_id", documentId);
        
        query.executeUpdate();
    }
    
    public void deleteByOwnerAndDocument(Long ownerId, Long documentId) {
        Query query = em.createNamedQuery("Starred.deleteByOwnerAndDocument");
        query.setParameter("owner_id", ownerId)
             .setParameter("document_id", documentId);
        
        query.executeUpdate();
    }
}