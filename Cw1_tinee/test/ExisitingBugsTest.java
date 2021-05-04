/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import sep.tinee.client.java.Client;
import java.io.*;
import java.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sep.mvc.DraftingCommand;
import sep.tinee.net.message.Push;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;
import sep.tinee.net.message.ShowReply;
import sep.tinee.net.message.ShowRequest;
/**
 *
 * @author meetd
 */
public class ExisitingBugsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void ClientStartedWithNoIssues() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInput.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);

    assertTrue(outContent.toString().contains("Locale set to en_GB"));
  }

  @Test
  public void ClientFailsWithInvalidPort() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "40a200";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInput.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);

    assertTrue(outContent.toString().contains("PORT parameter is not valid"));
  }

  @Test
  public void ClientFailsWithMissingFields() throws IOException {
    String[] args = new String[2];
    args[0] = "user";
    args[1] = "localhost";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInput.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);

    assertTrue(outContent.toString().contains("Missing required parameters"));
  }
    
}
