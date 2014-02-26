/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entity.Document;
import entity.Starred;
import entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.DocumentDataModel;
import service.DocumentService;
import service.StarredService;

@ManagedBean
@ViewScoped
public class HomePageController implements Serializable {
    
    public static final String EDIT_PAGE = "/user/edit?faces-redirect=true";
    public static final String CREATE_PAGE = "/user/create?faces-redirect=true";
    
    private User loggedUser;
    private Document[] selectedDocuments;    
    private DocumentDataModel documentsModel;
    private MENU selectedMenu;    
    
    @EJB
    private DocumentService documentService;  
    @EJB
    private StarredService starredService;
    
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUser = (User)context.getExternalContext().getSessionMap().get("loggedUser");  
        
        selectedMenu = MENU.MY_DRIVE;
        refresh();
    }
    
    public String createDocument(){ 
        return CREATE_PAGE;
    }
    
    public void displayStarreds(){
        selectedMenu = MENU.STARRED;
        
        List<Document> starreds = new ArrayList();
        for (Starred s : loggedUser.getStarreds()) starreds.add(s.getDocument());
        
        selectedDocuments = null;
        documentsModel = new DocumentDataModel(starreds);        
    }
    
    public void myDrive(){
        selectedMenu = MENU.MY_DRIVE;
        selectedDocuments = null;
        documentsModel = new DocumentDataModel(loggedUser.getDocuments());
    }
    
    public void toggleStarredStatus(){
        
        FacesMessage msg = new FacesMessage();
        try{
            for (Document doc : selectedDocuments){
                Starred starred = starredService.find(loggedUser.getId(),doc.getId());
                if (starred == null){
                    starred = new Starred(loggedUser, doc);
                    starredService.create(starred);
                    loggedUser.addToStarreds(starred);
                }
                else{
                    loggedUser.removeFromStarreds(starred);
                    starredService.remove(starred);
                }
            }
            
            msg.setSummary("Starred files updated");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
        }
        catch (Exception e){            
            msg.setSummary("An error occured");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);            
        }
        finally{            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, msg);    
            
            if (selectedMenu.equals(MENU.STARRED)) refresh();
        }
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
    
    private void refresh(){
        
        switch(selectedMenu){
            case MY_DRIVE:
                myDrive();
                break;
            case STARRED:
                displayStarreds();
                break;
        }        
    }
    
    public String getCurrentMenu() {        
        switch(selectedMenu){
            case MY_DRIVE:
                return "My Drive";
            case STARRED:
                return "Starred";
            default:
                return null;
        }
    }
}

enum MENU {MY_DRIVE, STARRED, DRIVE_FOLDER};