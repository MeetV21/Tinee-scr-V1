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
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;
import sep.tinee.net.message.ShowReply;
import sep.tinee.net.message.ShowRequest;


/**
 * A Controller is used to assemble the {@linkplain sep.mvc.AbstractModel Model}
 * and {@linkplain sep.mvc.AbstractView view}(s), and is responsible for
 * coordinating operations between them to form the application as a whole. The
 * Controller is the central component that processes commands (from the
 * user-driven view) and other events of interest (e.g., network I/O) by
 @@ -42,12 +47,12 @@
 * performing operations on the Model and view as appropriate.
 * <p>
 * Alternative application behaviours can be realised by different Controllers
 * over the same Model and views. Likewise, a Controller should be able to
 * handle any view that observes a compatible command protocol.
 * <p>
 * The binding of the Model and view to this Controller is fixed on creation.
 *
 * @author rhu1 {@literal <r.z.h.hu@herts.ac.uk>}
 */
public abstract class AbstractController {

  private final AbstractModel model;
  private final AbstractView view;

  /**
   * Binds the supplied Model and View to this Controller. Also implicitly
   * registers this Controller to the supplied View - this Controller will be
   * shutdown if a Controller has already been bound to the View.
   *
   * @param model The Model
   * @param view  The View
   *@throws java.io.IOException
   * @see sep.mvc.AbstractView#setController(sep.mvc.AbstractController)
   */
  protected AbstractController(final AbstractModel model, final AbstractView view)  throws IOException {
    this.model = model;
    this.view = view;
    view.setController(this);
  }
   /**
   * Starts the MVC architecture by starting the View
   *
   * @throws java.io.IOException
   */
  public void run() throws IOException {
    this.view.run();
  }

  /**
   * Shutdown this Controller, and the MVC application as whole. Implicitly
   * closes the bound View.
   * @throws java.io.IOException
   */
  public abstract void shutdown() throws IOException;

  /* Getters */

  /**
   * Get the Model bound to this Controller upon creation.
   *
   * @return The bound model
   */
  public AbstractModel getModel() {
    return this.model;
  }

  /**
   * Get the View bound to this Controller upon creation.
   *
   * @return The bound View
   */
  public AbstractView getView() {
    return this.view;
  }
   /**
   * Read messages for a given tag
   *
   * @param message (ReadRequest) to be sent to the server
   * @return The ReadReply from the server
   */
  public abstract ReadReply read(ReadRequest message);

 /**
   * Show all tags existing on the server
   *
   * @param message (ShowRequest) to be sent to the server
   * @return The ShowReply from the server
   */
  public abstract ShowReply show(ShowRequest message);

  /**
   * Clears all draftLines added for a draftTag and clears draft tag
   */
  public abstract void discard();

  /**
   * Publishes draft lines added for a tag to the server
   */
  public abstract void push();

  /**
   * Publishes draft lines added for a tag to the server Closes tag to prevent
   * further updates to the draftTag
   */
  public abstract void close();

  /**
   * Adds a new line to the draft
   *
   * @param command (DraftingCommand) action to add a new draft line
   */
  public abstract void line(DraftingCommand command);

  /**
   * Undoes the last draft line and keeps track of the position
   */

  public abstract void undo();
  /**
   * Executes the last undo command
   */

  public abstract void redo();
    /**
   * Starts a new draft for the tag
   *
   * @param tag
   */
  public abstract void startNewDraft(String tag);

}
