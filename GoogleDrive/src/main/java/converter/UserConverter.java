/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import service.UserService;

@FacesConverter(value = "userConverter")
public class UserConverter implements Converter {
    UserService userService = lookupUserServiceBean();    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String login) {
        return userService.findByLogin(login);      
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object user) {
        return user.toString();
    }

    private UserService lookupUserServiceBean() {
        try {
            Context c = new InitialContext();
            return (UserService) c.lookup("java:global/GoogleDrive/UserService!service.UserService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}