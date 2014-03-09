/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LanguageController implements Serializable {
    
    private Locale currentLocale;
    
    public LanguageController(){
        Application application = FacesContext.getCurrentInstance().getApplication();
        currentLocale = application.getDefaultLocale();
    }
    
    public String getLanguage(){
        return currentLocale.getLanguage();
    }
    
    public void setLanguage(String language){
        currentLocale = new Locale(language);
    }
    
    public Locale getCurrentLocale(){
        return currentLocale;
    }
    
    public SelectItem[] getLocales() {
        ArrayList items = new ArrayList();
        Application application = FacesContext.getCurrentInstance().getApplication();
        
        Iterator<Locale> supportedLocales = application.getSupportedLocales();        
        while (supportedLocales.hasNext()) {
            Locale locale = supportedLocales.next();
            String displayName = locale.getDisplayName(locale);            
            items.add(new SelectItem(locale.getLanguage(),camelize(displayName)));
        }
        
        SelectItem[] locales = new SelectItem[items.size()];
        items.toArray(locales);
        return locales;
    }    
    
    public void localeChanged(AjaxBehaviorEvent e){
        FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
    }
    
    private String camelize (String value){   
        StringBuilder builder = new StringBuilder(value);      
        builder.setCharAt(0,Character.toUpperCase(value.charAt(0)));
        
        return builder.toString();
    }
}