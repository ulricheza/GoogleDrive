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

@ManagedBean
@ViewScoped
public class DocumentController implements Serializable{    
    
    private final Document document;
    
    @EJB
    private DocumentService documentService;

    public DocumentController() {
        document = new Document("Untitled document");
    }
    
    
    /*public void validate(FacesContext context, UIComponent component, Object object) throws IOException{
        boolean notFound = false;
        String errorMsg;
        ExternalContext ec = context.getExternalContext();
        User loggedUser = (User) ec.getSessionMap().get("loggedUser");
        
        try{
            Long id = Long.parseLong(ec.getRequestParameterMap().get("id"));
            document = documentService.find(id);     
        }
        catch (NumberFormatException e){
            notFound = true;                        
        }
        
        notFound = notFound || (document == null);
        if (notFound){
            errorMsg = "Sorry, the file you requested does not exist"; 
            ec.responseSendError(HttpURLConnection.HTTP_NOT_FOUND,errorMsg);
        }
        else if (!document.getOwner().equals(loggedUser)){
            errorMsg = "You are not allowed to edit this document"; 
            ec.responseSendError(HttpURLConnection.HTTP_FORBIDDEN,errorMsg);            
        }        
    }*/
    
    public Document getDocument() {
        return document;
    }
    
   /* public String saveModifications(){
        return "/user/homepage?faces-redirect=true";
    }*/
    
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
            loggedUser.addToDocuments(document);
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Document created");
        }
        catch (Exception e){
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("An error occured. The document couldn't be created.");
        }
        finally{
            context.addMessage(null, msg);
        }
        
        return "/user/homepage?faces-redirect=true";
    }
}