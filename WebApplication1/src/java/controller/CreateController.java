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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import service.DocumentService;
import service.UserService;

@ManagedBean
@ViewScoped
public class CreateController implements Serializable{    
    
    private final Document document;
    
    @EJB
    private DocumentService documentService;
    @EJB
    private UserService userService;

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
           /* loggedUser.addToDocuments(document);
            userService.edit(loggedUser);*/
            
          /*  User owner = userService.find(document.getOwner().getId());
            //System.out.println("Owner = " + owner.getLogin() + " " + owner.getId());
            System.out.println(owner.getDocuments().size());
            System.out.println(loggedUser.getDocuments().size());*/
            
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Document created");
        }
        catch (Exception e){
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setDetail(e.toString());
            msg.setSummary("An error occured. The document couldn't be created.");
        }
        finally{
            context.addMessage(null, msg);
        }
        
        return "/user/homepage?faces-redirect=true";
    }
}