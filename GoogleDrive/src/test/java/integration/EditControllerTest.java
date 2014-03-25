/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package integration;

//<editor-fold desc="Imports" defaultstate="collapsed">
import controller.EditController;
import controller.UserController;
import entity.Document;
import entity.User;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;
import service.DocumentService;
import service.ResourceBundleService;
//</editor-fold>

public class EditControllerTest {
    
    private static EditController controller;
    private static DocumentService documentService;
    private static Document doc;
    private static User loggedUser;
    
    @BeforeClass
    public static void setUp(){
        ContextMocker.mockFacesContext(); 
        
        // The only document in the database
        User docOwner = new User();
        setField(docOwner,"id",1L);
        
        doc = new Document();
        setField(doc,"id",1L);
        setField(doc,"owner",docOwner);
        
        // The logged user
        loggedUser = new User();
        setField(loggedUser,"id",2L);        
        
        mockDocumentService();
        mockController();
    }
    
    @AfterClass
    public static void cleanUp(){
        FacesContext.getCurrentInstance().release();        
    }
    
    @Before
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getMessageList().clear();
        context.getExternalContext().getRequestParameterMap().clear();
        
        ContextMocker.responseStatusCode = -1;
    }
    
    @Test
    public void testInvalidIdParam(){                
        // No id is passed as parameter --> fail
        controller.validateId();
        
        // check that an HTTP_NOT_FOUND code is sent as response
        assert (ContextMocker.responseStatusCode == HttpURLConnection.HTTP_NOT_FOUND);
    }
    
    @Test
    public void testUnknownDocumentId(){ 
        // incorrect id is passed as parameter    
        putInRequestParameterMap("id","2");
        
        // the database contains only a document with 1L as id --> fail
        controller.validateId();
        
        // check that an HTTP_NOT_FOUND code is sent as response
        assert (ContextMocker.responseStatusCode == HttpURLConnection.HTTP_NOT_FOUND);
    }
    
    @Test  
    public void testNotAllowedToEdit(){
        // correct id is passed as parameter 
        putInRequestParameterMap("id","1");
        
        // logged User
        putInSessionMap("loggedUser",loggedUser); 
              
        // doc is not shared with loggedUser
        doReturn(false).when(controller).isShared(any(Document.class));
        
        // loggedUser != doc.owner and doc not shared --> fail
        controller.validateId();
        
        // check that an HTTP_FORBIDDEN code is sent as response
        assert (ContextMocker.responseStatusCode == HttpURLConnection.HTTP_FORBIDDEN);
    }
    
    @Test  
    public void testCanEditIfDocShared(){  
        // correct id is passed as parameter 
        putInRequestParameterMap("id","1");
        
        // logged User
        putInSessionMap("loggedUser",loggedUser); 
        
        // doc is now shared with loggedUser
        doReturn(true).when(controller).isShared(any(Document.class));
        
        // loggedUser != doc.owner but doc shared --> can edit
        controller.validateId();
        
        // check that the responseStatusCode is still -1
        assert (ContextMocker.responseStatusCode == -1);
    }
    
    @Test  
    public void testCanEditIfOwner(){    
        // correct id is passed as parameter 
        putInRequestParameterMap("id","1");
        
        // loggedUser is doc.owner
        putInSessionMap("loggedUser",doc.getOwner());  
        
        // doc is not shared with loggedUser
        doReturn(false).when(controller).isShared(any(Document.class));
        
        // doc not shared but loggedUser == doc.owner --> can edit
        controller.validateId();
        
        // check that the responseStatusCode is still -1
        assert (ContextMocker.responseStatusCode == -1);
    }
    
    @Test
    public void testFailedUpdate(){
        // stub documentService to throw an exception on edit()
        doThrow(RuntimeException.class).when(documentService).edit(any(Document.class));
        
        controller.setDocument(new Document());
        
        // perform update action
        String expectedReturnValue = UserController.HOME_PAGE;
        assert controller.update().equals(expectedReturnValue);
            
        // check action status (should be failure)
        checkMsg(FacesMessage.SEVERITY_ERROR, "an.error.occured");     
    }
    
    @Test
    public void testSuccessfullUpdate(){
        // do nothing on documentService.edit()
        doNothing().when(documentService).edit(any(Document.class));
        
        controller.setDocument(new Document());
        
        // perform update action
        String expectedReturnValue = UserController.HOME_PAGE;
        assert controller.update().equals(expectedReturnValue);
            
        // check action status (should be success)
        checkMsg(FacesMessage.SEVERITY_INFO, "document.updated");
    }    
    
    //<editor-fold desc="Mocking methods" defaultstate="collapsed">
    private static void mockDocumentService(){
        documentService = mock(DocumentService.class);
        doReturn(doc).when(documentService).find(1L);
    }
    
    private static void mockController(){
        EditController cc = new EditController();
        setField(cc, "documentService",documentService);
        
        controller = spy(cc);
        doReturn(Locale.FRENCH).when(controller).getLocale();
    }
    //</editor-fold>
    
    //<editor-fold desc="Other Methods" defaultstate="collapsed">
    private static void setField(Object obj, String fieldName, Object fieldValue){
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            
            f.setAccessible(true);
            f.set(obj, fieldValue);
            f.setAccessible(false);
        }
        catch (Exception ex){
            Logger.getLogger(CreateControllerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("An Exception occured: " + ex.getMessage());
        }        
    }  
    
    private void checkMsg(FacesMessage.Severity severity, String msgCode){
        List<FacesMessage> messagesList = FacesContext.getCurrentInstance().getMessageList();
        assert (messagesList.size() == 1);
            
        String failureMsg = ResourceBundleService.getString(msgCode,Locale.FRENCH,null);
        FacesMessage status = messagesList.get(0);
        assert (severity.equals(status.getSeverity()));
        assert (failureMsg.equals(status.getSummary()));        
    }
    
    private void putInRequestParameterMap(String key, String value){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.getRequestParameterMap().put(key, value);        
    }
    
    private void putInSessionMap(String key, Object value){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.getSessionMap().put(key,value);
    }
    //</editor-fold>
}