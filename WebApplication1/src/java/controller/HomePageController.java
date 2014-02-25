/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entity.Document;
import entity.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import model.DocumentDataModel;
import service.DocumentService;

@ManagedBean
public class HomePageController implements Serializable {
    
    public static final String EDIT_PAGE = "/user/edit?faces-redirect=true";
    public static final String CREATE_PAGE = "/user/create?faces-redirect=true";
    
    private User loggedUser;
    private Document[] selectedDocuments;    
    private DocumentDataModel documentsModel;
    
    @EJB
    private DocumentService documentService;  
    
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUser = (User)context.getExternalContext().getSessionMap().get("loggedUser");  
        
        documentsModel = new DocumentDataModel(loggedUser.getDocuments());
    }
    
    public String createDocument(){ 
        return CREATE_PAGE;
    }
    
    public Document[] getSelectedDocuments() {
        return selectedDocuments;
    }
    public void setSelectedDocuments(Document[] selectedDocuments) {
        this.selectedDocuments = selectedDocuments;
    } 
    
    public Document getSingleSelectedDocument(){
        if (selectedDocuments == null) return null;
        
        if (selectedDocuments.length != 1)
            return null;
        else
            return selectedDocuments[0];
    }
    
    public DocumentDataModel getDocumentsModel(){
        return documentsModel;
    }
}