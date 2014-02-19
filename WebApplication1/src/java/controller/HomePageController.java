/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entity.Document;
import entity.User;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import service.DocumentService;

@ManagedBean
@SessionScoped
public class HomePageController implements Serializable {
    
    public static final String EDIT_PAGE = "/user/edit?faces-redirect=true";
    
    @EJB
    private DocumentService documentService;    
    
    public String createDocument(){ 
        Document document = new Document();      
        document.setTitle("Untitled document");   
        document.setLastModified(new Date());
        
        FacesContext context = FacesContext.getCurrentInstance();
        User loggedUser = (User)context.getExternalContext().getSessionMap().get("loggedUser"); 
        document.setOwner(loggedUser);
        
        documentService.create(document);        
        return EDIT_PAGE + "&id=" + document.getId();
    }
}