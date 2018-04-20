/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.barrenland;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author annamaxam
 */
public class BarrenLand419250Test {
    
    BarrenLandAnalysis bla = new BarrenLandAnalysis();
    
    public BarrenLand419250Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class BarrenLandAnalysis.
     */
    @Test
    public void testMain() {
    }

    /**
     * Test of findFertileLand method, of class BarrenLandAnalysis.
     */
    @Test
    public void testFindFertileLand() {
        String[] strSTDIN = {"0 292 399 307"};
        String STDOUT = "116800 116800 ";
        
        
        assertEquals(STDOUT, bla.findFertileLand(strSTDIN));
  
    }
    
    /**
     * Test of findFertileLand method, of class BarrenLandAnalysis.
     */
    @Test
    public void testFindFertileLand2() {
        String[] strSTDIN = {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};
        String STDOUT = "22816 192608 ";
        
        
        assertEquals(STDOUT, bla.findFertileLand(strSTDIN));
  
    }
    
}
