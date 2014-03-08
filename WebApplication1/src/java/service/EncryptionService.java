/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

public class EncryptionService {

    public static String encrypt (String value){
        String hash = value;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(value.getBytes("UTF-8"));
            byte[] raw = md.digest();            
            hash = Base64.encodeBase64String(raw);
        } 
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(EncryptionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return hash;
    }
}