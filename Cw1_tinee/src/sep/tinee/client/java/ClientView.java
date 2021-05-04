package sep.tinee.client.java;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import sep.mvc.AbstractView;
import sep.mvc.DraftingCommand;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;
import sep.tinee.net.message.ShowReply;
import sep.tinee.net.message.ShowRequest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * 
 */
public class ClientView extends AbstractView {

  String state = "Main";
public ClientView(Locale locale) {
    super(locale);
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void run() throws IOException {
    try (
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      loop(reader);
    } catch (IOException | ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    } finally {
      getController().shutdown();
    }
  }

  void loop(BufferedReader reader) throws IOException, ClassNotFoundException {
    
    boolean done = false;
    // The loop
    while (!done) {

      // Print user options
      if (state.equals("Main")) {
        printToTerminal(CLFormatter.formatMainMenuPrompt(getResourceBundle()));
      } else {  // state = "Drafting"
        printToTerminal(CLFormatter.formatDraftingMenuPrompt(getResourceBundle(),this.getModel().getDraftTag(), getModel().getDraftLines()));
      }

      // Read a line of user input
      String raw = reader.readLine();
      if (raw == null) {
        throw new IOException("Input stream closed while reading.");
      }

      // Trim leading/trailing white space, and split words according to spaces
      List<String> split = Arrays.stream(raw.trim().split("\\ "))
              .map(x -> x.trim()).collect(Collectors.toList());
      String cmd = split.remove(0);  // First word is the command keyword
      String[] rawArgs = split.toArray(new String[split.size()]); // Remainder, if any, are arguments

      switch (cmd) {
        case "exit":
          done = true;
          break;
        case "manage":
          if ("Main".equalsIgnoreCase(this.state)) {
            this.state = "Drafting";
            this.getController().startNewDraft(rawArgs[0]);
            break;
          }
        case "read":
          if ("Main".equalsIgnoreCase(this.state)) {
            ReadReply rep = getController().read(new ReadRequest(rawArgs[0]));
            printToTerminal(CLFormatter.formatRead(rawArgs[0], rep.users, rep.lines));
            break;
          }
        case "show":
          if ("Main".equalsIgnoreCase(this.state)) {
            ShowReply rep = getController().show(new ShowRequest());
            printToTerminal(CLFormatter.formatShow(rep.tags));
            break;
          }
        case "line":
          if ("Drafting".equalsIgnoreCase(this.state)) {
            String line = Arrays.stream(rawArgs).collect(Collectors.joining(" "));
            DraftingCommand command = new DraftingCommand(getController().getModel(), line );
            getController().line(command);
            break;
          }
        case "undo":
          if ("Drafting".equalsIgnoreCase(this.state)) {
            getController().undo();
            break;
          }
          case "redo":
          if ("Drafting".equalsIgnoreCase(this.state)) {
            getController().redo();
            break;
          }
        case "discard":
          if ("Drafting".equalsIgnoreCase(this.state)) {
            getController().discard();
            this.state = "Main";
            
            break;
          }
        case "push":
          if ("Drafting".equalsIgnoreCase(this.state)) {
            getController().push();
            this.state = "Main";
            
            break;
          }
          case "close":
          if ("Drafting".equalsIgnoreCase(this.state)) {
            getController().close();
            this.state = "Main";
            
            break;
          }
        default:
          printToTerminal("Invalid command. Please try again");
          break;
      }
    }
  }

  public void printToTerminal(String text) {
    System.out.print(text);
  }

  
}