/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package integration;

//<editor-fold desc="Imports" defaultstate="collapsed">
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
//</editor-fold>

public abstract class ContextMocker extends FacesContext {

    private static final Release RELEASE = new Release();
    private static final AddMessage ADD_MESSAGE = new AddMessage();
    private static final ResponseSendError RESPONSE_SEND_ERROR = new ResponseSendError();    
    
    private static final List<FacesMessage> messagesList = new ArrayList<>();
    public static int responseStatusCode;
    
    public static void mockFacesContext() {
        try {
            // the session Map
            Map<String,Object> sessionMap = new HashMap<>();
            Map<String,String> requestParameterMap = new HashMap<>();
            
            // mock the external context
            ExternalContext ec = mock(ExternalContext.class);
            when(ec.getSessionMap()).thenReturn(sessionMap);
            when(ec.getRequestParameterMap()).thenReturn(requestParameterMap);
            when(ec.getFlash()).thenReturn(mock(Flash.class));
            doAnswer(RESPONSE_SEND_ERROR).when(ec).responseSendError(anyInt(), anyString());
            
            // mock FacesContext
            FacesContext context = mock(FacesContext.class);
            setCurrentInstance(context);
            
            when(context.getExternalContext()).thenReturn(ec);
            doAnswer(ADD_MESSAGE).when(context).addMessage(anyString(), any(FacesMessage.class));
            doAnswer(RELEASE).when(context).release();
            doReturn(messagesList).when(context).getMessageList();
        } 
        catch (IOException ex) {
            Logger.getLogger(ContextMocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //<editor-fold desc="Answers" defaultstate="collapsed">
    private static class Release implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            setCurrentInstance(null);
            return null;
        }
    }
    
    private static class AddMessage implements Answer<Void> {
       @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            messagesList.add((FacesMessage)args[1]);
            return null;
        }
    } 
    
    private static class ResponseSendError implements Answer<Void> {
       @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            responseStatusCode = (int) args[0];
            return null;
        }
    } 
    //</editor-fold>
}