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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.DocumentDataModel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import service.DocumentService;
import service.ResourceBundleService;
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
    
    public void handleFileUpload(FileUploadEvent event) { 
        try {
            UploadedFile file = event.getFile();
            String fileName = file.getFileName();
            InputStream stream = file.getInputstream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder content = new StringBuilder();
            
            String line;   
            while (( line = reader.readLine()) != null){
                content.append(line);
                content.append("<br/>");
            }
            
            Document d = new Document(fileName.substring(0,fileName.lastIndexOf(".")));
            d.setContent(content.toString());
            d.setOwner(loggedUser);
            d.setLastModified(new Date());
            
            documentService.create(d);
        } 
        catch (IOException e) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, e);
        }
    } 
    
    public void displayMyDrive(){
        selectedMenu = MENU.MY_DRIVE;
        
        selectedDocuments = null;
        documentsModel = new DocumentDataModel(documentService.findByOwner(loggedUser.getId()));
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
            
            msg.setSummary(ResourceBundleService.getString("starred.files.updated",getLocale(),null));
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
        }
        catch (Exception e){
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, e);
            
            msg.setSummary(ResourceBundleService.getString("an.error.occured",getLocale(),null));
            msg.setDetail(e.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);            
        }
        finally{            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, msg);    
            
            if (selectedMenu.equals(MENU.STARRED)) refresh();
        }
    }
    
    public void delete(){       
        FacesMessage msg = new FacesMessage();
        
        try{
            for (Document d : getSelectedDocuments()){
                if (d.getOwner().equals(loggedUser)){                    
                    sharedService.deleteByDocument(d.getId());
                    starredService.deleteByDocument(d.getId());
                    documentService.remove(d);
                }
                else{
                    sharedService.deleteByUserAndDocument(loggedUser.getId(),d.getId());
                    starredService.deleteByOwnerAndDocument(loggedUser.getId(),d.getId()); 
                }
            }
            
            String key = (getSelectedDocuments().length < 2) ? "file.deleted" : "files.deleted";
            msg.setSummary(ResourceBundleService.getString(key,getLocale(),null));
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
        }       
        catch (Exception e){
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, e);
            
            msg.setSummary(ResourceBundleService.getString("an.error.occured",getLocale(),null));
            msg.setDetail(e.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);            
        }
        finally{
            FacesContext.getCurrentInstance().addMessage(null, msg);
            refresh();
        }
    }
    
    public String edit(){
        Document d = getSelectedDocument();
        if (d == null){
            return null;
        }
        
        return EDIT_DOCUMENT_PAGE + "&id="+d.getId();        
    }
    
    // <editor-fold defaultstate="collapsed" desc="Share">
    public void share(){
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = new FacesMessage();
        
        try{
            shareList.remove(loggedUser);
            for (User u : shareList){
                for (Document d : selectedDocuments){
                    Shared s = sharedService.findByUserAndDocument(u.getId(),d.getId());
                    if (s == null) sharedService.create(new Shared(u,d));
                }
            }
            
            String key = (shareList.size() < 2) ? "file.shared" : "files.shared";
            msg.setSummary(ResourceBundleService.getString(key,getLocale(),null));
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            
            FacesContext.getCurrentInstance().addMessage(null, msg);  
            context.addCallbackParam("success", true);  
        }
        catch (Exception e){
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, e);
            
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            msg.setSummary(ResourceBundleService.getString("an.error.occured",getLocale(),null));
            msg.setDetail(e.toString());
            
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
    
    public StreamedContent getFile(){
        Document d = getSelectedDocument();
        InputStream stream = new ByteArrayInputStream(d.getContent().getBytes()); 
        
        return new DefaultStreamedContent(stream, "text/html", d.getTitle());
    }
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
    
    /*public void onCellEdit(CellEditEvent event) {  
        String oldValue = (String) event.getOldValue();  
        String newValue = (String) event.getNewValue();
        
        if(!newValue.isEmpty() && !newValue.equals(oldValue)) {
            List<Document> docs = (List<Document>) documentsModel.getWrappedData();  
            Document d = docs.get(event.getRowIndex());
            
            d.setTitle(newValue);
            d.setLastModified(new Date());            
            
            documentService.edit(d);      
            User owner = d.getOwner();
            owner.removeFromDocuments(d);
            owner.addToDocuments(d);
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);  
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }  
        
        //refresh();
    }*/
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
                return  ResourceBundleService.getString("my.drive",getLocale(),null); 
            case SHARED_WITH_ME:
                return ResourceBundleService.getString("shared.with.me",getLocale(),null); 
            case STARRED:
                return ResourceBundleService.getString("starred",getLocale(),null);
            default:
                return null;
        }
    }
    
    private Locale getLocale(){
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
    // </editor-fold>
}

enum MENU {MY_DRIVE, DRIVE_FOLDER, SHARED_WITH_ME, STARRED};