

import java.util.*;

/**
 * A helper class for the current prototype {@link Client client}.  <i>E.g.</i>,
 * for formatting Command Line messages.
 */
public class CLFormatter {

  CLFormatter() {
  }

  /* Following are the auxiliary methods for formatting the UI text */

  
    static String formatSplash(ResourceBundle messages, String user) {
    return "\n" + messages.getString("hello") + " " + user + "!\n";
  }

   static String formatMainMenuPrompt(ResourceBundle messages) {
    return "\n" + messages.getString("MainMenuPrompt") + "\n"
            + messages.getString("MainMenuCommands")+ "\n";
  }

  static String formatDraftingMenuPrompt(ResourceBundle messages, String tag, List<String> lines) {
    return "\n" +messages.getString("DraftingMenuPrompt")+ " \n" +formatDrafting(tag, lines)
            + messages.getString("DraftingMenuCommands")+ "\n";
  }

  static String formatDrafting(String tag, List<String> lines) {
    StringBuilder b = new StringBuilder("#");
    b.append(tag);
    int i = 1;
    for (String x : lines) {
      b.append("\n");
      b.append(String.format("%12d", i++));
      b.append("  ");
      b.append(x);
    };
    return b.toString();
  }

  static String formatRead(String tag, List<String> users,
      List<String> read) {
    StringBuilder b = new StringBuilder("Read: #");
    b.append(tag);
    Iterator<String> it = read.iterator();
    for (String user : users) {
      b.append("\n");
      b.append(String.format("%12s", user));
      b.append("  ");
      b.append(it.next());
    };
    b.append("\n");
    return b.toString();
  }
  
  static String formatShow(Map<String, String> tags) {
    StringBuilder b = new StringBuilder("Show tags");
    for (String tag : tags.keySet()) {
      b.append("\n");
      b.append(String.format("%12s", tag));
      b.append("  ");
      b.append(tags.get(tag));
    };
    b.append("\n");
    return b.toString();
  }
  
}
