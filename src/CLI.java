import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CLI {
    private Pcap pcap;
    private boolean running = true;

    private BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

    public CLI(Pcap pcap, List<String> args) {
        this.pcap = pcap;

        // Execute command line options
        if ((args == null) || (args.size() == 0)) {
            displayPcap();
        } else {
            for (String arg : args) {
                parseUserInput(arg);
            }
        }

        // Interractive mode
        while (running) {
            displayMenu();
            String input = getUserInput();
            parseUserInput(input);
        }
    }

    public CLI(Pcap pcap) {
        this(pcap, null);
    }

    private void displayMenu() {
        System.out.print("cmd: ");
    }

    private void displayPcap() {
        System.out.println(pcap);
    }

    private void displayHelp() {
        System.out.println("""
            Available commands:
                h/help        : Display the help menu

                d/display     : Display the pcap
                f/follow [Nb] : Follow a TCP Stream (If available)
                s/show [Nb]   : Show a single packet in more detail (If available)
                q/quit/exit   : Exit LineAnemone

        """);
    }

    private String getUserInput() {
        String userInput = "";

        try {
            userInput = consoleIn.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: Couldn't read user input !");
        }

        System.out.println("");
        return userInput;
    }

    private void parseUserInput(String input) {
        StringTokenizer tokens = new StringTokenizer(input);

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();

            try {
                switch (token) {
                    case "q":
                    case "quit":
                    case "exit":
                        running = false;
                        break;

                    case "h":
                    case "help":
                        displayHelp();
                        break;

                    case "d":
                    case "display":
                        displayPcap();
                        break;

                    case "s":
                    case "show":
                        if (tokens.hasMoreTokens()) {
                            displayPacket(Integer.parseInt(tokens.nextToken()));
                        } else {
                            throw new InvalidParameterException("Missing Parameter");
                        }
                        break;

                    case "f":
                    case "follow":
                        if (tokens.hasMoreTokens()) {
                            followTCPStream(Integer.parseInt(tokens.nextToken()));
                        } else {
                            throw new InvalidParameterException("Missing Parameter");
                        }
                        break;

                    default:
                        break;
                }
            } catch (InvalidParameterException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Needs a number");
            }
        }
    }

    private void followTCPStream(int streamID) {
        ArrayList<Protocol> tcpStream = pcap.followTCPStream(streamID);

        if (tcpStream.size() == 0) {
            System.out.println("ERROR: There is no TCP Stream " + streamID + "\n");
        } else {
            System.out.println("Following TCP Stream " + streamID + ":\n");

            for (Protocol protocol : tcpStream) {
                System.out.println(protocol);
            }
        }
    }

    private void displayPacket(int packetID) {
        Packet packet = pcap.getPacket(packetID);

        if (packet == null) {
            System.out.println("ERROR: Not such packet ID: " + packetID + "\n");
        } else {
            System.out.println("Showing Packet " + packetID + ":\n\n" + packet);
        }
    }
}
