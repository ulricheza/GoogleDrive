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
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import service.DocumentService;

@ManagedBean
@ViewScoped
public class DocumentController implements Serializable{    
    
    private Document document;
    
    @EJB
    private DocumentService documentService;
    
    public void validate(FacesContext context, UIComponent component, Object object) throws IOException{
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
    }
    
    public Document getDocument() {
        return document;
    }
    
    public void saveModifications(){
        
    }
}