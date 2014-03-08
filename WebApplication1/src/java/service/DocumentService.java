/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.Document;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class DocumentService extends AbstractFacade<Document> {
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentService() {
        super(Document.class);
    }

    public List<Document> findByOwner(Long ownerId) {
        TypedQuery<Document> query = em.createNamedQuery("Document.findByOwner", Document.class);
        query.setParameter("owner_id", ownerId);
        
        return query.getResultList();
    }
}