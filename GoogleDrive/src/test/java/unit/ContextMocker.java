/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unit;

import entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class ContextMocker extends FacesContext {

    private static final Release RELEASE = new Release();
    private static final AddMessage ADD_MESSAGE = new AddMessage();
    private static final List<FacesMessage> messagesList = new ArrayList<>();
    
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
    
    public static FacesContext mockFacesContext() {
        
        // mock the session Map
        Map<String,Object> sessionMap = mock(HashMap.class);
        when(sessionMap.get("loggedUser")).thenReturn(new User());
        
        // mock the external context
        Flash flash = mock(Flash.class);
        doNothing().when(flash).setKeepMessages(anyBoolean());
        
        ExternalContext ec = mock(ExternalContext.class);
        when(ec.getSessionMap()).thenReturn(sessionMap);
        when(ec.getFlash()).thenReturn(flash);
        
        // mock FacesContext
        FacesContext context = mock(FacesContext.class);
        setCurrentInstance(context);
        
        doAnswer(ADD_MESSAGE).when(context).addMessage(anyString(), any(FacesMessage.class));
        doAnswer(RELEASE).when(context).release();
        doReturn(messagesList).when(context).getMessageList();
        when(context.getExternalContext()).thenReturn(ec);
        
        return context;
    }
}