/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import entity.Remember;
import entity.User;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import service.CookieService;
import service.RememberService;
import service.UserService;

@ManagedBean
@SessionScoped
public class UserController implements Serializable{
    
    private static final String HOME_PAGE = "/user/homepage?faces-redirect=true";
    private static final String LOGIN_PAGE = "/auth/login?faces-redirect=true";
        
    private User user;
    private String pwdConfirmation;
    private boolean remember;
    
    private final Map<String,Object> cookieProperties = new HashMap<>();
    
    @EJB
    private UserService userService;
    
    @EJB
    private RememberService rememberService;
    
    @PostConstruct
    public void init(){
        user = new User();
        
        remember = true;
        
        cookieProperties.put("path", CookieService.COOKIE_PATH);
        cookieProperties.put("maxAge", CookieService.COOKIE_MAX_AGE);
    }
    
    public String signIn(){
        
        if (userService.signIn(user)){
            user = userService.findByLogin(user.getLogin());
            FacesContext context = FacesContext.getCurrentInstance();
            
            if(remember){
                String uuid = UUID.randomUUID().toString();  
                rememberService.create(new Remember(uuid,user));
                context.getExternalContext().addResponseCookie(CookieService.COOKIE_NAME, 
                                                               uuid,cookieProperties);                  
            }
            
            context.getExternalContext().getSessionMap().put("loggedUser", user);            
            return HOME_PAGE;
        }
        else{
            sendError("Login failed");
            return null;
        }
    }
    
    public String logout(){        
        FacesContext context = FacesContext.getCurrentInstance();
        User loggedUser = (User)context.getExternalContext().getSessionMap().get("loggedUser"); 
        
        if(loggedUser != null){
            rememberService.deleteByUser(loggedUser);
            
            cookieProperties.put("maxAge", 0);        
            context.getExternalContext().addResponseCookie(CookieService.COOKIE_NAME,
                                                           null,cookieProperties);
        }
        
        context.getExternalContext().invalidateSession();        
        return LOGIN_PAGE;
    }
    
    public String register() {  
        
        if (!pwdConfirmation.equals(user.getPassword())){            
            sendError("Passwords don't match");            
            return null;
        }
        else if (userService.findByLogin(user.getLogin()) != null){
            sendError("Login already in use");            
            return null;            
        }        
        else {
            userService.create(user);
            remember = true;
            
            return signIn();
        }
    }
    
    private void sendError (String summary){        
        FacesMessage msg = new FacesMessage(summary);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);        
    }

    public User getUser(){
        return user;
    }
    
    public String getPwdConfirmation() {
        return pwdConfirmation;
    }
    public void setPwdConfirmation(String pwdConfirmation) {
        this.pwdConfirmation = pwdConfirmation;
    }
    
    public boolean isRemember() {
        return remember;
    }
    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}