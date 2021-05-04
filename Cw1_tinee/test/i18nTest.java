/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import sep.tinee.client.java.Client;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author meetd
 */
public class i18nTest {

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

@Test(expected=RuntimeException.class)
  public void ClientErrorsWithNoSystemInput() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";


    Client.main(args);
  }

  @Test
  public void defaultLocalIsUsed() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInput.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);

    assertTrue( outContent.toString().contains("Locale set to en_GB"));
  }

  @Test
  public void ClientStartsDrafting() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInputDrafting.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);
  }

  @Test
  public void ClientStartsDraftEdit() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInputDraftEdit.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);
  }

  @Test
  public void ClientStartsMain() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInputMain.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);
  }

  @Test
  public void ClientStartsAddingLines() throws IOException {
    String[] args = new String[3];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInputDrafting.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);
  }


  @Test
  public void customLocaleIsUsed() throws IOException {
    String[] args = new String[5];
    args[0] = "user";
    args[1] = "localhost";
    args[2] = "4000";
    args[3] = "FR";
    args[4] = "fr";

    final InputStream original = System.in;
    final FileInputStream fips = new FileInputStream(new File("./tests/ClientTestInput.txt"));
    System.setIn(fips);
    Client.main(args);
    System.setIn(original);

    assertTrue( outContent.toString().contains("Locale set to fr_FR"));
  }
}
