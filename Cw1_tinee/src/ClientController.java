import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sep.mvc.AbstractController;
import sep.mvc.AbstractModel;
import sep.mvc.AbstractView;
import sep.mvc.DraftingCommand;
import sep.tinee.net.message.Push;
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
public class ClientController extends AbstractController {

  protected ArrayList<DraftingCommand> history = null;
  protected int currentCommand = -1;

  public ClientController(AbstractModel model, AbstractView view) {
    super(model, view);
  }

  public void init() throws IOException {
    this.history = new ArrayList();
    getView().run();
  }

  @Override
  public void shutdown() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public ReadReply readTag(ReadRequest message) {
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
  public void pushDraft(String tag) {
    try {
      AbstractModel model= this.getModel();
      Push push = model.createPushCommand(tag);
      this.getModel().send(push);
      this.currentCommand = -1;
      this.history = new ArrayList();
      this.getModel().clearDraftLines();
    } catch (IOException exception) {
      Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, exception);
    }
  }

  @Override
  public void addDraftLine(DraftingCommand command) {
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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public ShowReply showTags(ShowRequest message) {
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
  public void discardDraft() {
    this.history = new ArrayList();
    this.getModel().clearDraftLines();
    this.currentCommand = -1;
  }
}