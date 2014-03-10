/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleService {
    
    public static String getString(String key, Locale locale, Object params[]) {

        String value;
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", locale);

        try {
            value = bundle.getString(key);
        } 
        catch (MissingResourceException e) {
            value = "???" + key + "???";
        }

        if (params != null) {
            MessageFormat mf = new MessageFormat(value, locale);
            value = mf.format(params, new StringBuffer(), null).toString();
        }

        return value;
    }
}