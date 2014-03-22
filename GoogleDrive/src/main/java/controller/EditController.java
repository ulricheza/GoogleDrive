/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entity.Document;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import service.DocumentService;
import service.ResourceBundleService;
import service.SharedService;

@ManagedBean
@ViewScoped
public class EditController implements Serializable{    
    
    private Document document;
    private User loggedUser;
    
    @EJB
    private DocumentService documentService;
    @EJB
    private SharedService sharedService;
   
    @PostConstruct
    public void validateId(){
        boolean notFound = false;
        String errorMsg; 
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        loggedUser = (User) ec.getSessionMap().get("loggedUser");
        
        try{
            Long id = Long.parseLong(ec.getRequestParameterMap().get("id"));
            document = documentService.find(id);     
        }
        catch (NumberFormatException e){
            Logger.getLogger(EditController.class.getName()).log(Level.SEVERE, null, e);
            
            notFound = true;                        
        }
        
        notFound = notFound || (document == null);
        try{
            if (notFound){
                errorMsg = ResourceBundleService.getString("file.does.not.exist",getLocale(),null);
                ec.responseSendError(HttpURLConnection.HTTP_NOT_FOUND,errorMsg);               
            }
            else if (!document.getOwner().equals(loggedUser) && !isShared(document)) {
                errorMsg = ResourceBundleService.getString("not.allowed.to.edit",getLocale(),null);
                ec.responseSendError(HttpURLConnection.HTTP_FORBIDDEN,errorMsg);            
            }   
        }
        catch (IOException e){
            Logger.getLogger(EditController.class.getName()).log(Level.SEVERE, null, e);
            
            FacesMessage msg = new FacesMessage();
            msg.setSummary(ResourceBundleService.getString("an.error.occured",getLocale(),null));
            msg.setDetail(e.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            
            context.addMessage(null, msg);
        }
    }
    
    public Document getDocument() {
        return document;
    }
    
    public void setDocument(Document d){
        this.document = d;
    }
    
    public String update(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.getFlash().setKeepMessages(true);
        
        FacesMessage msg = new FacesMessage();        
        try{
            document.setLastModified(new Date());
            documentService.edit(document);
            
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary(ResourceBundleService.getString("document.updated",getLocale(),null));
        }
        catch (Exception e){
            Logger.getLogger(EditController.class.getName()).log(Level.SEVERE, null, e);
           
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary(ResourceBundleService.getString("an.error.occured",getLocale(),null));
            msg.setDetail(e.toString());
        }
        finally{
            context.addMessage(null, msg);
        }
        
        return UserController.HOME_PAGE;
    }

    public boolean isShared(Document document) {
        return sharedService.findDocumentsByUser(loggedUser.getId()).contains(document);                   
    }
    
    public Locale getLocale(){
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
}