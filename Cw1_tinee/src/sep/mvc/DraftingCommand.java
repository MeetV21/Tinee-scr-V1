/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sep.mvc;

/**
 *
 * @author meetd
 */
public class DraftingCommand {
    private final AbstractModel model;
  private final String line;

  public DraftingCommand(AbstractModel model, String line) {
    this.model = model;
    this.line = line;
  }

  public void execute(){
    this.model.addDraftLine(this.line);
  }

  public void undo(){
    this.model.removeDraftLine(this.line);
  }
}

