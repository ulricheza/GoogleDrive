/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entity.Document;
import entity.Shared;
import entity.Starred;
import entity.User;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.DocumentDataModel;
import org.primefaces.context.RequestContext;
import service.DocumentService;
import service.SharedService;
import service.StarredService;
import service.UserService;

@ManagedBean
@ViewScoped
public class HomePageController implements Serializable {
    
    public static final String EDIT_DOCUMENT_PAGE = "/user/edit?faces-redirect=true";
    public static final String CREATE_DOCUMENT_PAGE = "/user/create?faces-redirect=true";
    
    private User loggedUser;
    private Document[] selectedDocuments;    
    private DocumentDataModel documentsModel;
    private MENU selectedMenu;    
    
    private List<User> shareList;
    
    @EJB
    private DocumentService documentService;
    @EJB
    private UserService userService;
    @EJB
    private SharedService sharedService;
    @EJB
    private StarredService starredService;    
    
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUser = (User)context.getExternalContext().getSessionMap().get("loggedUser");  
        
        selectedMenu = MENU.MY_DRIVE;
        refresh();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Menu Actions">    
    public String createDocument(){ 
        return CREATE_DOCUMENT_PAGE;
    }
    
    public void displayMyDrive(){
        selectedMenu = MENU.MY_DRIVE;
        
        selectedDocuments = null;
        documentsModel = new DocumentDataModel(loggedUser.getDocuments());
    }
    
    public void displayShareds(){
        selectedMenu = MENU.SHARED_WITH_ME;
        
        selectedDocuments = null;
        documentsModel = new DocumentDataModel(sharedService.findDocumentsByUser(loggedUser.getId()));        
    }
    
    public void displayStarreds(){
        selectedMenu = MENU.STARRED;
        
        selectedDocuments = null;
        documentsModel = new DocumentDataModel(starredService.findDocumentsByOwner(loggedUser.getId()));        
    }    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Toolbar Actions">    
    public void toggleStarredStatus(){
        
        FacesMessage msg = new FacesMessage();
        try{
            for (Document doc : selectedDocuments){
                Starred starred = starredService.findByOwnerAndDocument(loggedUser.getId(),doc.getId());
                if (starred == null){
                    starred = new Starred(loggedUser, doc);
                    starredService.create(starred);
                }
                else{
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
            context.addMessage("globalKey", msg);    
            
            if (selectedMenu.equals(MENU.STARRED)) refresh();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Share">
    public void share(){
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = new FacesMessage();
        
        try{
            for (User u : shareList){
                for (Document d : selectedDocuments){
                    Shared s = sharedService.findByUserAndDocument(u.getId(),d.getId());
                    if (s == null) sharedService.create(new Shared(u,d));
                }
            }
            
            msg.setSummary("Files shared");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            
            FacesContext.getCurrentInstance().addMessage("globalKey", msg);  
            context.addCallbackParam("success", true);  
        }
        catch (Exception e){
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary("An error occured. Files couldn't be shared");
            
            FacesContext.getCurrentInstance().addMessage(null, msg);  
            context.addCallbackParam("success", false);            
        }
    }
    
    public List<User> complete(String login){
        return userService.findByLoginLike(login);        
    }
    
    public List<User> getShareList(){
        return shareList;
    }
    public void setShareList(List<User> shareList){
        this.shareList = shareList;
    }   
    // </editor-fold>
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="DataTable model">
    public DocumentDataModel getDocumentsModel(){
        return documentsModel;
    }
    
    public Document[] getSelectedDocuments() {
        return selectedDocuments;
    }
    public void setSelectedDocuments(Document[] selectedDocuments) {
        this.selectedDocuments = selectedDocuments;
    } 
    
    public Document getSelectedDocument(){
        if (selectedDocuments == null) return null;        
        if (selectedDocuments.length != 1) return null;
        return selectedDocuments[0];
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Ajax refresh">
    private void refresh(){        
        switch(selectedMenu){
            case MY_DRIVE:
                displayMyDrive();
                break;
            case SHARED_WITH_ME:
                displayShareds();
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
            case SHARED_WITH_ME:
                return "Shared";
            case STARRED:
                return "Starred";
            default:
                return null;
        }
    }
    // </editor-fold>
}

enum MENU {MY_DRIVE, DRIVE_FOLDER, SHARED_WITH_ME, STARRED};