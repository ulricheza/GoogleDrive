/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unit;

import controller.CreateController;
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

public class CreateControllerTest {
    
    private static CreateController mockController;
    private static DocumentService mockService;
    
    @BeforeClass
    public static void setUp(){
        ContextMocker.mockFacesContext();   
        
        // documentService.create() does nothing the first time
        // and throw an exception the second time
        mockService = mock(DocumentService.class);
        doNothing().doThrow(RuntimeException.class)
                .when(mockService).create(any(Document.class));
        
        // create mock Controller
        mockCreateController();
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
    public void testSuccessfullSave(){
        // perform save action
        String expectedReturnValue = "/user/homepage?faces-redirect=true";
        assert mockController.save().equals(expectedReturnValue);
            
        // check action status (should be success)
        List<FacesMessage> messagesList = FacesContext.getCurrentInstance().getMessageList();
        assert (messagesList.size() == 1);
            
        String successMsg = ResourceBundleService.getString("document.created",Locale.FRENCH,null);
        FacesMessage status = messagesList.get(0);
        assert (FacesMessage.SEVERITY_INFO.equals(status.getSeverity()));
        assert (successMsg.equals(status.getSummary()));
    }
    
    @Test
    public void testFailedSave(){
        // perform save action
        String expectedReturnValue = "/user/homepage?faces-redirect=true";
        assert mockController.save().equals(expectedReturnValue);
            
        // check action status (should be failure)
        List<FacesMessage> messagesList = FacesContext.getCurrentInstance().getMessageList();
        assert (messagesList.size() == 1);
            
        String failureMsg = ResourceBundleService.getString("an.error.occured",Locale.FRENCH,null);
        FacesMessage status = messagesList.get(0);
        assert (FacesMessage.SEVERITY_ERROR.equals(status.getSeverity()));
        assert (failureMsg.equals(status.getSummary()));
    }

    private static void mockCreateController(){
        try {
            CreateController controlleur = new CreateController();
            Field f = controlleur.getClass().getDeclaredField("documentService");
            f.setAccessible(true);
            f.set(controlleur, mockService);
            f.setAccessible(false);
            
            mockController = spy(controlleur);
            doReturn(Locale.FRENCH).when(mockController).getLocale();
        } 
        catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException ex){
            Logger.getLogger(CreateControllerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("An Exception occured: " + ex.getMessage());
        }
    }    
}