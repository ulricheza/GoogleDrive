/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unit;

import java.util.Locale;
import org.junit.Test;
import service.ResourceBundleService;
public class ResourceBundleServiceTest {

    @Test
    public void tesGetString(){
               
        // English
        String expected = "Register";
        String actual = ResourceBundleService.getString("register", Locale.ENGLISH, null);
        assert actual.equals(expected);        
        
        // French
        expected = "S'inscrire";
        actual = ResourceBundleService.getString("register", Locale.FRENCH, null);
        assert actual.equals(expected);
        
        // Unknown key in French
        expected = "???unknown.key???";
        actual = ResourceBundleService.getString("unknown.key", Locale.FRENCH, null);
        assert actual.equals(expected);
        
        // Unknown key in English
        expected = "???unknown.key???";
        actual = ResourceBundleService.getString("unknown.key", Locale.ENGLISH, null);
        assert actual.equals(expected);    
    }   
}