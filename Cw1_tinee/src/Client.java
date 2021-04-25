


import java.io.IOException;
import java.util.Locale;

/**
 * This class is an initial work-in-progress prototype for a command line Tinee
 * client. It has been hastily hacked together, as often the case in early
 * exploratory coding, and is incomplete and buggy. However, it does compile and
 * run, and <i>some</i> basic functionality, such as pushing and reading tines
 * to and from an instance of {@link sep.tinee.server.Server}, is working. Try
 * it out!
 * <p>
 * The arguments required to run a client correspond to the
 * {@link #set(String, String, int)} method: a user name, and the host name and
 * port number of a Tinee server.
 * <p>
 * You can compile and run this client using <b>NetBeans</b>; e.g., right-click
 * this file in the NetBeans editor and select "Run File". Note, to provide the
 * above arguments, you should set up a <b>run configuration</b> for this class:
 * {@literal ->} "Set Project Configuration" {@literal ->} "Customize..."
 * {@literal ->} "New...".
 * <p>
 * Assuming compilation using NetBeans (etc.), you can also run {@code Client}
 * from the command line; e.g., on Windows, run:
 * <ul>
 * <li style="list-style-type: none;">
 * {@code C:\...\tinee>  java -cp build\classes Client userid localhost 8888}
 * </ul>
 * <p>
 * You will be significantly reworking and extending this client: your aim is to
 * meet the specification, and you have mostly free rein to do so. (That is as
 * opposed to the base framework, which you are <b>not</b> permitted to modify,
 * i.e., the packages {@link sep.tinee.server},
 * {@link sep.tinee.server}, {@link sep.tinee.server} and]
 * {@link sep.tinee.server}.) The constraints on your client are:
 * <ul>
 * <li>
 * You must retain a class named {@code Client}) as the frontend class for
 * running your client, i.e., via its static {@linkplain #main(String[]) main}
 * method.
 * <li>
 * The {@linkplain Client#main(String[]) main} method must accept the <i>same
 * arguments</i> as currently, i.e., user name, host name and port number.
 * <li>
 * Your client must continue to accept user commands on the <i>standard input
 * stream</i> ({@code System.in}), and output on the <i>standard output
 * stream</i> ({@code System.out}).
 * <li>
 * Any other conditions specified by the assignment tasks.
 * </ul>
 * <p>
 * <i>Tip:</i> generate and read the <b>JavaDoc</b> API documentation for the
 * provided base framework classes (if you're not already reading this there!).
 * After importing the code project into NetBeans, right-click the project in
 * the "Projects" window and select "Generate Javadoc". By default, the output
 * is written to the {@code dist/javadoc} directory. You can directly read the
 * comments in the code for the same information, but the generated JavaDoc is
 * formatted more nicely as HTML with click-able links.
 *
 * @see CLFormatter
 */
public class Client {

  ClientController controller;

  Client(String user, String host, int port, Locale locale) {
    ClientModel model = new ClientModel(user,host, port);
    ClientView view = new ClientView();
    view.setLocale(locale);
    controller = new ClientController(model, view);
  }

  public static void main(String[] args) throws IOException {
    String user = null;
    String host = null;
    int port = 0;

    // Retrieving command line variables
    try {
      user = args[0];
      host = args[1];
      port = Integer.parseInt(args[2]);
    } catch (ArrayIndexOutOfBoundsException exception) {
      // Handle errors when command line parameters don't exist
      System.out.println("Missing required parameters");
      System.out.println("User, Host and Port are required");
      return;
    } catch (NumberFormatException exception) {
      // Handle errors when port parameter is not a number
      System.out.println("PORT parameter is not valid");
      System.out.println("Parameter needs to be an integer");
      return;
    }
    
    String country = null;
    String language = null;

    try{
      country = args[3];
      language = args[4];
    }
    catch (ArrayIndexOutOfBoundsException exception) {
     country = "GB";
     language = "en";
    }
    Locale locale = new Locale(language, country);

    System.out.println("Local set to "+language+"_"+country);
    // Create client object
    Client client = new Client(user, host, port, locale);
    client.run();
  }
  
  void run() throws IOException{
    controller.run();
  }
}
