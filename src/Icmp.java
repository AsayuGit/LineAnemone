public class Icmp extends Protocol {
    private String messageType = "...";
    private String code = "...";

    public Icmp(byte[] data) {
        super(data);
        
        name = "ICMP";

        short fileMessageType = (short) (data[0] & 0xff);
        short fileCode = (short) (data[1] & 0xff);

        parseControlMessages(fileMessageType, fileCode);
    }

    public String getType() {
        return messageType;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "[ " + name + ": Type: " + getType() + " -> " + getCode() + " ]\n";
    }

    private void parseControlMessages(short fileMessageType, short fileCode) {
        switch (fileMessageType) {
            case 0:
                messageType = "Echo Reply";
                code = "Ping";
                break;
            case 8:
                messageType = "Echo Request";
                code = "Ping";
                break;
            case 3:
                messageType = "Destination Unreachable";
                switch (fileCode) {
                    case 0:
                        code = "Destination network unreachable";
                        break;
                    case 1:
                        code = "Destination host unreachable";
                        break;
                    case 2:
                        code = "Destination protocol unreachable";
                        break;
                    case 3:
                        code = "Destination port unreachable";
                        break;
                    case 4:
                        code = "Fragmentation required, and DF flag set";
                        break;
                    case 5:
                        code = "Source route failed";
                        break;
                    case 6:
                        code = "Destination network unknown";
                        break;
                    case 7:
                        code = "Destination host unknown";
                        break;
                    case 8:
                        code = "Source host isolated";
                        break;
                    case 9:
                        code = "Network administratively prohibited";
                        break;
                    case 10:
                        code = "Host administratively prohibited";
                        break;
                    case 11:
                        code = "Network unreachable for ToS";
                        break;
                    case 12:
                        code = "Host unreachable for ToS";
                        break;
                    case 13:
                        code = "Communication administratively prohibited";
                        break;
                    case 14:
                        code = "Host Precedence Violation";
                        break;
                    case 15:
                        code = "Precedence cutoff in effect";
                        break;

                    default:
                        code = "Unknown";
                        break;
                }
                break;
            case 4:
                messageType = "Source Quench";
                code = "Congestion control";
                break;
            case 5:
                messageType ="Redirect Message";
                switch (fileCode) {
                    case 0:
                        code = "Redirect Datagram for the Network";
                        break;
                    case 1:
                        code = "Redirect Datagram for the Host";
                        break;
                    case 2:
                        code = "Redirect Datagram for the ToS & network ";
                        break;
                    case 3:
                        code = "Redirect Datagram for the ToS & host";
                        break;
                }
                break;
            case 9:
                messageType = "Router Advertisement";
                break;
            case 10:
                messageType = "Router Solicitation";
                break;
            case 11:
                messageType = "Time Exceeded";
                switch (fileCode) {
                    case 0:
                        code = "TTL expired in transit";
                        break;
                    case 1:
                        code = "Fragment reassembly time exceeded";
                        break;
                }
                break;
            case 12:
                messageType = "Parameter Problem: Bad IP header";
                switch (fileCode) {
                    case 0:
                        code = "Pointer indicates the error";
                        break;
                    case 1:
                        code =  "Missing a required option";
                        break;
                    case 2:
                        code = "Bad length";
                        break;
                }
                break;
            case 13:
                messageType = "Timestamp";
                break;
            case 14:
                messageType = "Timestamp Reply";
                break;
            case 15:
                messageType = "Information Request";
                break;
            case 16:
                messageType = "Information Reply";
                break;
            case 17:
                messageType = "Address Mask Request";
                break;
            case 18:
                messageType = "Address Mask Reply";
                break;
            case 30:
                messageType = "Traceroute";
                code = "Information Request";
                break;
            case 42:
                messageType = "Extended Echo Request";
                break;
            case 43:
                messageType = "Extended Echo Reply";
                switch (fileCode) {
                    case 0:
                        code = "No Error";
                        break;
                    case 1:
                        code = "Malformed Query";
                        break;
                    case 2:
                        code = "No Such Interface";
                        break;
                    case 3:
                        code = "No Such Table Entry";
                        break;
                    case 4:
                        code = "Multiple Interfaces Satisfy Query";
                        break;
                }
                break;

            case 1:
            case 2:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 255:
                messageType = "Reserved";
                code = "Reserved";
                break;
            default:
                messageType ="Unassigned";
                break;
        }
    }
}
