package sep.tinee.client.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sep.mvc.AbstractController;
import sep.mvc.AbstractModel;
import sep.mvc.AbstractView;
import sep.mvc.DraftingCommand;
import sep.tinee.net.message.Bye;
import sep.tinee.net.message.Push;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;
import sep.tinee.net.message.ShowReply;
import sep.tinee.net.message.ShowRequest;
import java.util.ResourceBundle;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * 
 */
public class ClientController extends AbstractController {

  public ArrayList<DraftingCommand> history = null;
  public int currentCommand = -1;

  public ClientController(AbstractModel model, AbstractView view) throws IOException {
    super(model, view);
  }

  public void init() throws IOException {
    this.history = new ArrayList();
    getView().run();
  }

  @Override
   public void shutdown() throws IOException {
    this.getModel().send(new Bye()); // Close connection to server
    this.getView().close(); // Initiate shutdown for View
  }

  @Override
  public ReadReply read(ReadRequest message) {
    AbstractModel model = this.getModel();
    ReadReply reply = null;
    try {
      model.send(message);
      reply = (ReadReply) model.receive();
    } catch (IOException | ClassNotFoundException exception) {
      Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, exception);
    }
    return reply;
  }

  @Override
  public void push() {
    try {
       AbstractModel model = this.getModel();

      Push push = new Push(model.getUser(),model.getDraftTag(), model.getDraftLines()); // Create PUSH message for server

      this.getModel().send(push); // Send PUSH message to server
      this.getModel().clearDraft(); // Clear model draft data

      resetDraft(); // Reset controller draft
    } catch (IOException exception) {
      Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, exception);
    }
  }

  @Override
  public void line(DraftingCommand command) {
    this.history.subList(this.currentCommand + 1, this.history.size()).clear();
    this.history.add(command);
    command.execute();
    this.currentCommand++;
  }

  @Override
  public void undo() {
    if (currentCommand >= 0) {
      DraftingCommand command = this.history.get(currentCommand);
      command.undo();
      currentCommand--;
    }
  }

  @Override
  public void redo() {
   if(history.size() > (this.currentCommand + 1)){
      DraftingCommand command = this.history.get(this.currentCommand+1);

      command.execute();
      this.currentCommand++;
    }
  }

  @Override
  public ShowReply show(ShowRequest message) {
    AbstractModel model = this.getModel();
    ShowReply reply = null;
    try {
      model.send(message);
      reply = (ShowReply) model.receive();
    } catch (IOException | ClassNotFoundException exception) {
      Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, exception);
    }
    return reply;
  }

  @Override
  public void discard() {
    this.history = new ArrayList();
    this.getModel().clearDraft();
    this.currentCommand = -1;
  }

   
     @Override
  public void close() {
    // Add closing line to close tag
    DraftingCommand command = new DraftingCommand(this.getModel(), "##CLOSE##");
    this.line(command);
 
    // Push draft changes to server
    this.push();
  }


  protected void resetDraft(){
    this.history = new ArrayList();
    this.currentCommand = -1;
  }

  @Override
  public void startNewDraft(String tag) {
    this.getModel().newDraftTag(tag);
  }
    public ArrayList<DraftingCommand> getHistory(){
    return this.history;
  }

  public int getCurrentCommand(){
    return this.currentCommand;
  }
    
}