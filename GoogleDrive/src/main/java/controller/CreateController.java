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
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import service.DocumentService;
import service.ResourceBundleService;

@ManagedBean
@ViewScoped
public class CreateController implements Serializable{    
    
    private final Document document;
    
    @EJB
    private DocumentService documentService;

    public CreateController() {
        document = new Document("Untitled document");
    }
    
    public Document getDocument() {
        return document;
    }
    
    public String save(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.getFlash().setKeepMessages(true);
        
        User loggedUser = (User)ec.getSessionMap().get("loggedUser"); 
        document.setOwner(loggedUser);
        document.setLastModified(new Date());
        
        FacesMessage msg = new FacesMessage();        
        try{
            documentService.create(document);
            
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary(ResourceBundleService.getString("document.created",getLocale(),null));
        }
        catch (Exception e){
            Logger.getLogger(CreateController.class.getName()).log(Level.SEVERE, null, e);
            
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary(ResourceBundleService.getString("an.error.occured",getLocale(),null));
            msg.setDetail(e.toString());
        }
        finally{
            context.addMessage(null, msg);
        }
        
        return "/user/homepage?faces-redirect=true";
    }
    
    public Locale getLocale(){
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
}