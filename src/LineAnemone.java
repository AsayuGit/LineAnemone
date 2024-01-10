import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

class LineAnemone {
    private static boolean help = false;
    private static String path = null;

    private static ArrayList<String> cliArgs = new ArrayList<String>();

    public static void main(String args[]) {
        parseArgs(args);

        if (help) {
            displayHelp();
        } else {
            if (path == null) {
                System.out.println("ERROR: No file to load");
            } else {
                try {
                    Pcap pcap = new Pcap(path);
                    new CLI(pcap, cliArgs);
                } catch (IOException e) {
                    System.out.println("ERROR: Couldn't open file " + path);
                } catch (DataFormatException e) {
                    System.out.println("ERROR: Invalid Pcap file !");
                }
            }
        }
    }

    // Parse the long and short console args and set the apropriate flags
    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].substring(0, 1).equals("-")) {
                boolean next = false;

                for (char option : args[i].substring(1, args[i].length()).toCharArray()) {
                    switch (option) {
                        case 'h':
                            help = true;
                            break;
                        
                        case 'f':
                            if ((i + 1) < args.length) {
                                cliArgs.add("follow " + args[++i]);
                                next = true;
                            }
                            break;

                        case 's':
                            if ((i + 1) < args.length) {
                                cliArgs.add("show " + args[++i]);
                                next = true;
                            }
                            break;

                        case 'd':
                            cliArgs.add("display");
                            break;

                        case 'q':
                            cliArgs.add("exit");
                            break;

                        default:
                            break;
                    }
                    if (next) break;
                }

            } else if (path == null) {
                path = args[i];
            }
        }
    }

    private static void displayHelp() {
        System.out.println("""
        LineAnemone:

            Usage: java -jar LineAnemone.jar [OPTION] [PATH]

            -h      : Display this guide
            -d      : Display the pcap
            -f [Nb] : Follow a TCP Stream (If available)
            -s [Nb] : Show a single packet in more detail (If available)
            -q      : Exit LineAnemone

        """);
    }
}