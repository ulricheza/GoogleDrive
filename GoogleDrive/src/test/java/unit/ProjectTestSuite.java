/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    unit.UserServiceTest.class, 
    unit.ResourceBundleServiceTest.class, 
    unit.CreateControllerTest.class
})
public class ProjectTestSuite {}