/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author meetd
 */
public class ClientTest {
    
    public ClientTest() {
    }
	@Test
	public void testThatAddNewLineWorksAsExpected() throws IOException {               
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();              
            PrintStream oldOut = System.out;		
            System.setOut(new PrintStream(out));
	               
            InputStream oldInput = System.in;
		
		String[] args = {"Meet", "localhost", "8888"};
		
		String input = "manage newtine1\nline experiment no. one line\n";
		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes("UTF8"));
		System.setIn(in);
		
		try {
		
		Client.main(args);

		} catch (Exception ex) {
                    
                    System.setIn(oldInput);
		
		String output = out.toString();			
		String expectedOutput = "experiment no. one line";		
                System.setOut(oldOut);                          
                
                
		assertTrue("output did not match: " + expectedOutput, output.contains(expectedOutput));          		
		}		
                
                
	}

	       
        
    }

    
    

