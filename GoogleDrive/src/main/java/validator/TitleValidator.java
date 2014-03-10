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
public class TitleValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        int lth = ((String)value).trim().length();
        
        if (lth == 0){
            FacesMessage msg = new FacesMessage();
            msg.setSummary(ResourceBundleService.getString("title.required", 
                           FacesContext.getCurrentInstance().getViewRoot().getLocale(),
                           null));
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            
            throw new ValidatorException(msg);
        }
    }
}