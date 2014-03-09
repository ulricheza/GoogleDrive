/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import service.ResourceBundleService;

@FacesValidator
public class LoginValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String login = (String)value;        
        int lth = login.length();
        
        if (lth < 5 || lth > 10){
          
            FacesMessage msg = new FacesMessage();
            msg.setSummary(ResourceBundleService.getString("login.invalid.length", 
                           FacesContext.getCurrentInstance().getViewRoot().getLocale(),
                           new Object[]{5,10,lth}));
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            
            throw new ValidatorException(msg);
        }
    }
}