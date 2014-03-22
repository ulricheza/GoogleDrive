/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unit;

import converter.TitleConverter;
import org.junit.Test;

public class TitleConverterTest {
    
    @Test
    public void testGetAsObject(){
        
        TitleConverter converter = new TitleConverter();
        
        // Empty title
        String expected = "";
        String actual = (String) converter.getAsObject(null, null, "");
        assert expected.equals(actual);
        
        // One whitespace
        expected = "";
        actual = (String) converter.getAsObject(null, null, " ");
        assert expected.equals(actual);
        
        // Multiple whitespaces
        expected = "";
        actual = (String) converter.getAsObject(null, null, "       ");
        assert expected.equals(actual);
        
        // title with whitespaces around
        expected = "aaaa";
        actual = (String) converter.getAsObject(null, null, "  aaaa  ");
        assert expected.equals(actual);
        
        // title with one whitespace inside
        expected = "aa aa";
        actual = (String) converter.getAsObject(null, null, "aa aa");
        assert expected.equals(actual);
        
        // title with whitespaces inside
        expected = "aa aa";
        actual = (String) converter.getAsObject(null, null, "aa      aa");
        assert expected.equals(actual);
        
        // title without whitespaces
        expected = "aaaa";
        actual = (String) converter.getAsObject(null, null, "aaaa");
        assert expected.equals(actual);
    }    
}