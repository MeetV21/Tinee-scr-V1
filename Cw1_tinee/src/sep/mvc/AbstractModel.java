/*
 * This file is part of the base framework for the 6COM1038 Software Engineering
 * Practice coursework assessments -- a command line client application for a
 * text-based Ticket System.
 *
 * <i>Warning:</i> you are advised <b>not</b> to modify this file, nor any of
 * the base framework, i.e., the packages {@link sep.tinee.server},
 * {@link sep.tinee.server}, {@link sep.tinee.server} and
 * {@link sep.tinee.server}. Finally, you submit only your client code, which
 * will be tested against the base framework as provided.
 *
 * If you do wish to (temporarily) modify the base framework for debugging or
 * your own interest, please bear in mind the above point.
 */
package sep.mvc;

import java.io.IOException;
import java.util.ArrayList;
import sep.tinee.net.channel.ClientChannel;
import sep.tinee.net.message.Message;
import sep.tinee.net.message.Push;

/**
 * The Model is the most fundamental component in an MVC application - it
 * encapsulates the application's core data structures. It offers operations on
 * the encapsulated data, and regulates when and how these operations should be
 * performed. For example, the validity of certain operations may depend on,
 * and transform, the current state of the Model. The Model should be
 * independent of any particular
 * {@linkplain sep.mvc.AbstractController controller} or
 * {@linkplain sep.mvc.AbstractView view}.
 * <p>
 * This base Model definition is empty. This definition is used to specify that
 * a Model is bound to a controller when the latter is created. In some
 * scenarios, a Model may maintain a reference to the View via which updates
 * can be actively pushed when the model is operated on - we opt not to specify
 * such functionality for our present application.
 *
 * @author rhu1 {@literal <r.z.h.hu@herts.ac.uk>}
 */
public abstract class AbstractModel {
    
    protected ClientChannel channel;
  protected String user;
  protected ArrayList<String> draftLines;

  public AbstractModel(String user, String host, int port) {
    this.channel = new ClientChannel(host, port);
    this.user = user;
    this.draftLines = new ArrayList<>();
  }

  public ArrayList<String> getDraftLines() {
    return this.draftLines;
  }

  public void clearDraftLines() {
    this.draftLines = new ArrayList();
  }

  public abstract void send(Message msg) throws IOException;

  public abstract Message receive() throws IOException, ClassNotFoundException;

  public abstract void addDraftLine(String text);

  public abstract void removeDraftLine(String text);

  public abstract Push createPushCommand(String tag);

}
