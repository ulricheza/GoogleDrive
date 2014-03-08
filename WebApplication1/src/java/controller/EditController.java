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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import service.DocumentService;
import service.SharedService;
import service.UserService;

@ManagedBean
@ViewScoped
public class EditController implements Serializable{    
    
    private Document document;
    private User loggedUser;
    
    @EJB
    private DocumentService documentService;
    @EJB
    private UserService userService;
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
            notFound = true;                        
        }
        
        notFound = notFound || (document == null);
        try{
            if (notFound){
                errorMsg = "Sorry, the file you requested does not exist"; 
                ec.responseSendError(HttpURLConnection.HTTP_NOT_FOUND,errorMsg);
            }
            else if (!document.getOwner().equals(loggedUser) && !isShared(document)) {
                errorMsg = "You are not allowed to edit this document"; 
                ec.responseSendError(HttpURLConnection.HTTP_FORBIDDEN,errorMsg);            
            }   
        }
        catch (IOException e){
            FacesMessage msg = new FacesMessage("An error occured");
            msg.setDetail(e.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            
            context.addMessage(null, msg);
        }
    }
    
    public Document getDocument() {
        return document;
    }
    
    public String update(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.getFlash().setKeepMessages(true);
        
        FacesMessage msg = new FacesMessage();        
        try{
            document.setLastModified(new Date());
            documentService.edit(document);
           // userService.edit(document.getOwner());
            
           /* User owner = userService.find(document.getOwner().getId());
            //System.out.println("Owner = " + owner.getLogin() + " " + owner.getId());
            System.out.println(owner.getDocuments().size());
            System.out.println(loggedUser.getDocuments().size());
          //  owner.removeFromDocuments(document);
          // owner.addToDocuments(document);
           userService.edit(owner);*/
            
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            msg.setSummary("Document updated");
       }
       catch (Exception e){
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("An error occured. The document couldn't be updated.");
            msg.setDetail(e.toString());
        }
        finally{
            context.addMessage(null, msg);
        }
        
        return "/user/homepage?faces-redirect=true";
    }

    private boolean isShared(Document document) {
        return sharedService.findDocumentsByUser(loggedUser.getId()).contains(document);                   
    }
}