package cps.server.testing.tests;

import static org.junit.Assert.*;

import org.junit.Assert;

import cps.server.controllers.*;
import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.Authenticator.Success;

public class TestLotController {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testInsertCar() {
	String testLine = "0&0&0&&0&0&0&&0&0&0&&&0&0&0&&0&0&0&&0&0&0&&&0&0&0&&0&0&0&&0&0&0&&&0&0&0&&0&0&0&&0&0&0";
	String[][][] result1 = new String[4][3][3], result2 = null;
	int i, j, k;
	for (i = 0; i < 4; i++) {
	    for (j = 0; j < 3; j++) {
		for (k = 0; k < 3; k++) {
		    result1[i][j][k] = "0";
		}
	    }
	}
	
	
    }

    @Test
    public void testRetrieveCar() {
    }

    @Test
    public void testHandle() {
    }

}
