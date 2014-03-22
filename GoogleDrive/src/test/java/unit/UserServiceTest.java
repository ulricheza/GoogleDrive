/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unit;

import entity.User;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import service.EncryptionService;
import service.UserService;

public class UserServiceTest {
       
    @Test
    public void testSignIn(){        
        UserService mockService = mock(UserService.class);
        doCallRealMethod().when(mockService).signIn(any(User.class));
        doReturn(new User("admin", EncryptionService.encrypt("admin")))
                .when(mockService).findByLogin("admin");
        
        // Unknown user
        User u = new User("unknown", "admin");        
        assertFalse(mockService.signIn(u));
        
        // Wrong password 
        u = new User("admin", "wrongPwd");
        assertFalse(mockService.signIn(u));
        
        // Valid user and password
        u = new User("admin", "admin");
        assertTrue(mockService.signIn(u));
    }
}