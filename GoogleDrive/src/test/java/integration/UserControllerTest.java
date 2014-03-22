/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package integration;

//<editor-fold desc="Imports" defaultstate="collapsed">
import integration.CreateControllerTest;
import controller.UserController;
import entity.User;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.mockito.Mockito.*;
import service.RememberService;
import service.ResourceBundleService;
import service.UserService;
//</editor-fold>

public class UserControllerTest {
    
    private static UserController controller;
    private static UserService userService;
    private static RememberService rememberService;
    
    @BeforeClass
    public static void setUp(){
        ContextMocker.mockFacesContext(); 
        
        mockUserService();
        mockRememberService();
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
    public void testSucessfullSignIn(){
        doReturn(true).when(userService).signIn(any(User.class));         
        
        String expected = UserController.HOME_PAGE;
        String actual = controller.signIn();
        assert actual.equals(expected);
        
        // Logged user instance should be put in the session map
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,Object> sessionMap = context.getExternalContext().getSessionMap();
        assert (sessionMap.size() == 1);
        assert (sessionMap.get("loggedUser") instanceof User);
    }
    
    @Test
    public void testFailedSignIn(){
        doReturn(false).when(userService).signIn(any(User.class));
        
        // should stay on the same page
        assert (null == controller.signIn());
        
        // check error msg
        checkErrorMsg("login.failed");        
    }
    
    @Test
    public void testRegisterPwdsDontMatch(){
        controller.getUser().setPassword("matchingPwd");
        controller.setPwdConfirmation("wrongPwd");
        
        // should stay on the same page
        assert (null == controller.register());
        
        // check error msg
        checkErrorMsg("passwords.do.not.match");
    }
    
    @Test
    public void testRegisterWithLoginInUse(){
        controller.getUser().setPassword("matchingPwd");
        controller.setPwdConfirmation("matchingPwd");
        
        // should stay on the same page
        assert (null == controller.register());
        
        // check error msg
        checkErrorMsg("login.already.in.use");        
    }
    
    @Test
    public void testRegisterWithValidLogin(){
        controller.getUser().setPassword("matchingPwd");
        controller.setPwdConfirmation("matchingPwd");
        
        controller.setRemember(false);
        doReturn(null).when(userService).findByLogin(anyString());
        
        String expected = UserController.HOME_PAGE;
        String actual = controller.register();
        assert actual.equals(expected);
        
        // remember should be automatically selected
        assert controller.isRemember();
    }
    
    @Test
    public void testLogout(){
        String expected = UserController.LOGIN_PAGE;
        String actual = controller.logout();
        assert actual.equals(expected);
    }
    
    //<editor-fold desc="Mocking methods" defaultstate="collapsed">
    private static void mockUserService(){
        userService = mock(UserService.class);        
        doReturn(new User()).when(userService).findByLogin(anyString());
    }
    
    private static void mockRememberService(){
        rememberService = mock(RememberService.class);
    }
    
    private static void mockController(){
        UserController uc = new UserController();
        uc.init();
        setField(uc, "userService",userService);
        setField(uc, "rememberService",rememberService);
        
        controller = spy(uc);
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
        catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException ex){
            Logger.getLogger(CreateControllerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("An Exception occured: " + ex.getMessage());
        }        
    }  
    
    private void checkErrorMsg(String msgCode){
        List<FacesMessage> messagesList = FacesContext.getCurrentInstance().getMessageList();
        assert (messagesList.size() == 1);
            
        String failureMsg = ResourceBundleService.getString(msgCode,Locale.FRENCH,null);
        FacesMessage status = messagesList.get(0);
        assert (FacesMessage.SEVERITY_ERROR.equals(status.getSeverity()));
        assert (failureMsg.equals(status.getSummary()));        
    }
    //</editor-fold>
}