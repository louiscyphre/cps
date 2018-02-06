/**
 * 
 */
package cps.client.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class parses and saves all command line parameters for client
 */
public class CmdParser {

  /**
   * 
   */
  private CommandLine cmd;

  /**
   * @param args
   * @throws ParseException
   */
  public void extract(String[] args) throws ParseException {

    Options options = new Options();

    Option address = new Option("h", "host", true, "Server adress");
    address.setRequired(true);
    options.addOption(address);

    Option port = new Option("p", "port", true, "Server port");
    port.setRequired(true);
    options.addOption(port);

    Option mode = new Option("m", "mode", true, "Mode of work: kiosk, web, or service");
    mode.setRequired(false);
    options.addOption(mode);

    Option lotId = new Option("l", "lot-id", true, "Parking lot id (0 for web client)");
    lotId.setRequired(false);
    options.addOption(lotId);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("java -jar yourClientJarName.jar", options);
      throw e;
    }
  }

  /**
   * @return host
   */
  public String getHost() {
    String option = cmd.getOptionValue("host");
    return (option == null) ? option : cps.common.Constants.DEFAULT_HOST;
  }

  /**
   * @return port
   * @throws NumberFormatException
   */
  public int getPort() throws NumberFormatException {
    String option = cmd.getOptionValue("port");
    if (option == null) {
      return cps.common.Constants.DEFAULT_PORT;
    }
    return Integer.parseInt(option);
  }

  /**
   * @return lot id
   * @throws NumberFormatException
   */
  public int getLotId() throws NumberFormatException {
    String option = cmd.getOptionValue("lot-id");
    if (option == null) {
      return cps.common.Constants.DEFAULT_LOT_NUMBER;
    }
    return Integer.parseInt(option);
  }

  /**
   * @return mode of the application
   */
  public String getMode() {
    String option = cmd.getOptionValue("mode");
    return (option == null) ? "kiosk" : option;
  }
}
