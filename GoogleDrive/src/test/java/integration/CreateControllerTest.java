/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package integration;

//<editor-fold desc="Imports" defaultstate="collapsed">
import controller.CreateController;
import controller.UserController;
import entity.Document;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;
import service.DocumentService;
import service.ResourceBundleService;
//</editor-fold>

public class CreateControllerTest {
    
    private static CreateController controller;
    private static DocumentService documentService;
    
    @BeforeClass
    public static void setUp(){
        ContextMocker.mockFacesContext();   
        
        mockDocumentService();
        mockController();
    }
    
    @AfterClass
    public static void cleanUp(){
        FacesContext.getCurrentInstance().release();        
    }
    
    @Before
    public void init(){
        FacesContext.getCurrentInstance().getMessageList().clear();
    }
    
    @Test
    public void testFailedSave(){
        // stub documentService to throw an exception on create()
        doThrow(RuntimeException.class).when(documentService).create(any(Document.class));
        
        // perform save action
        String expectedReturnValue = UserController.HOME_PAGE;
        assert controller.save().equals(expectedReturnValue);
            
        // check action status (should be failure)
        checkMsg(FacesMessage.SEVERITY_ERROR, "an.error.occured");              
    }
    
    @Test
    public void testSuccessfullSave(){
        // do nothing on documentService.create()
        doNothing().when(documentService).create(any(Document.class));
        
        // perform save action
        String expectedReturnValue = UserController.HOME_PAGE;
        assert controller.save().equals(expectedReturnValue);
            
        // check action status (should be success)
        checkMsg(FacesMessage.SEVERITY_INFO, "document.created");
    }
    
    //<editor-fold desc="Mocking methods" defaultstate="collapsed">
    private static void mockDocumentService(){
        documentService = mock(DocumentService.class);
    }
    
    private static void mockController(){
        CreateController cc = new CreateController();
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
    //</editor-fold>
}