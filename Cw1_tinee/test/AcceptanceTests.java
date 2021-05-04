/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import sep.tinee.client.java.ClientView;
import sep.tinee.client.java.ClientModel;
import sep.tinee.client.java.ClientController;
import java.io.*;
import java.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sep.mvc.DraftingCommand;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;
import sep.tinee.net.message.ShowReply;
import sep.tinee.net.message.ShowRequest;
/**
 *
 * @author meetd
 */
public class AcceptanceTests {
    ClientModel model;
  ClientView view;
  ClientController controller;

  public AcceptanceTests() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws IOException {
    Locale locale = new Locale("GB", "en");
    model = new ClientModel("testuser", "localhost", 4000);
    view = new ClientView(locale);
    controller = new ClientController(model, view);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void ControllerInitialisedWithNoErrors() throws IOException {
    assertNotNull(controller);
    assertNotNull(controller.getModel());
    assertNotNull(controller.getView());
  }

  @Test
  public void ControllerCanReadFromServer() throws IOException {
    ReadRequest message = new ReadRequest("readtest");
    ReadReply result = controller.read(message);

    assertNotNull(result);
    assertEquals(result.lines.get(0), "This is the first line");
    assertEquals(result.lines.get(1), "This is the Second line");

  }

  @Test
  public void ControllerCanReadMultipleUsersFromServer() throws IOException {
    ReadRequest message = new ReadRequest("rdtest2");
    ReadReply result = controller.read(message);

    assertNotNull(result);
    assertEquals(result.lines.get(0), "This is the first line");
    assertEquals(result.lines.get(3), "This is the fourth line");
    assertEquals(result.users.get(0), "testuser");
    assertEquals(result.users.get(3), "test2");

  }

  @Test
  public void ControllerCanPushNewTagData() throws IOException {
    try {
      model.newDraftTag("pushtest");
      model.addDraftLine("Test Line 1");
      model.addDraftLine("Test Line 2");

      controller.push();
    } catch (Exception e) {
      fail("An error occurred trying to push new tag data");
    }
  }

  @Test
  public void ControllerCanPushNewTagDataAndClearDraft() throws IOException {
    model.newDraftTag("pushtest");
    model.addDraftLine("Test Line 1");
    model.addDraftLine("Test Line 2");

    controller.push();
    assertTrue(model.getDraftLines().isEmpty());
    assertNull(model.getDraftTag());
  }

  @Test
  public void ControllerAddNewDraftLine() {
    DraftingCommand command = new DraftingCommand(model, "First line here");

    controller.line(command);

    assertEquals(controller.history.get(0), command);
    assertEquals(controller.currentCommand, 0);
    assertEquals(model.getDraftLines().get(0), "First line here");
  }

  @Test
  public void ControllerUndoDraftLine() {
    controller.discard();

    DraftingCommand command1 = new DraftingCommand(model, "First line here");
    controller.line(command1);
    DraftingCommand command2 = new DraftingCommand(model, "Second line here");
    controller.line(command2);
    DraftingCommand command3 = new DraftingCommand(model, "Third line here");
    controller.line(command3);

    assertEquals(controller.history.size(), 3);
    assertEquals(controller.history.get(0), command1);
    assertEquals(controller.history.get(2), command3);
    assertEquals(controller.currentCommand, 2);
    assertEquals(model.getDraftLines().size(), 3);

    controller.undo();

    assertEquals(controller.history.size(), 3);
    assertEquals(controller.history.get(0), command1);
    assertEquals(controller.history.get(2), command3);
    assertEquals(controller.currentCommand, 1);
    assertEquals(model.getDraftLines().size(), 2);

  }

  @Test
  public void ControllerRedoDraftLine() {
    controller.discard();

    DraftingCommand command1 = new DraftingCommand(model, "First line here");
    controller.line(command1);
    DraftingCommand command2 = new DraftingCommand(model, "Second line here");
    controller.line(command2);
    DraftingCommand command3 = new DraftingCommand(model, "Third line here");
    controller.line(command3);

    assertEquals(controller.history.size(), 3);
    assertEquals(controller.history.get(0), command1);
    assertEquals(controller.history.get(2), command3);
    assertEquals(controller.currentCommand, 2);
    assertEquals(model.getDraftLines().size(), 3);

    controller.undo();

    assertEquals(controller.history.size(), 3);
    assertEquals(controller.history.get(0), command1);
    assertEquals(controller.history.get(2), command3);
    assertEquals(controller.currentCommand, 1);
    assertEquals(model.getDraftLines().size(), 2);

    controller.redo();

    assertEquals(controller.history.size(), 3);
    assertEquals(controller.history.get(0), command1);
    assertEquals(controller.history.get(2), command3);
    assertEquals(controller.currentCommand, 2);
    assertEquals(model.getDraftLines().size(), 3);
  }

  @Test
  public void ControllerDiscardDraft() {
    model.newDraftTag("discard");

    DraftingCommand command1 = new DraftingCommand(model, "First line here");
    controller.line(command1);
    DraftingCommand command2 = new DraftingCommand(model, "Second line here");
    controller.line(command2);
    DraftingCommand command3 = new DraftingCommand(model, "Third line here");
    controller.line(command3);

    // Check that draft  still exists
    assertEquals(controller.history.size(), 3);
    assertEquals(model.getDraftTag(), "discard");
    assertEquals(controller.currentCommand, 2);
    assertEquals(model.getDraftLines().size(), 3);

    controller.discard();

    assertEquals(controller.history.size(), 0);
    assertTrue(controller.history.isEmpty());
    assertEquals(controller.currentCommand, -1);
    assertEquals(model.getDraftLines().size(), 0);
    assertNull(model.getDraftTag());
  }

  @Test
  public void ControllerCanShowExistingTags(){
    ShowRequest request = new ShowRequest();

    ShowReply reply = controller.show(request);

    assertNotNull(reply.tags);
    // Checking if tags size is more than 4 since the exact number may vary
    assertTrue(reply.tags.size() > 4);
  }
}
