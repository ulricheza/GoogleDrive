/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filter;

import entity.Remember;
import entity.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.CookieService;
import service.RememberService;

@WebFilter("/faces/auth/*")
public class AuthenticationFilter implements Filter{
    RememberService rememberService = lookupRememberServiceBean();
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        User user = (User)req.getSession().getAttribute("loggedUser");        
        
        if (user == null) {
            String uuid = CookieService.getCookieValue(req, CookieService.COOKIE_NAME);
            
            if (uuid != null) {
                Remember remember = rememberService.find(uuid);
                if (remember != null) user = remember.getUser();

                if (user != null) {
                    
                    req.getSession().setAttribute("loggedUser", user); // Login.
                    CookieService.addCookie(res, CookieService.COOKIE_NAME, 
                                            uuid, CookieService.COOKIE_MAX_AGE); // Extends age.
                } else {
                    if (remember != null) rememberService.remove(remember);
                    CookieService.removeCookie(res, CookieService.COOKIE_NAME);
                }
            }
        }
        
        if(user != null){
            // User is logged in, so redirect to homepage
            res.sendRedirect(req.getContextPath() + "/faces/user/homepage.xhtml");
        }
        else{
            // User is not logged in, so just continue request.
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}

    private RememberService lookupRememberServiceBean() {
        try {
            Context c = new InitialContext();
            return (RememberService) c.lookup("java:global/WebApplication1/RememberService!service.RememberService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}