// https://en.wikipedia.org/wiki/Dynamic_Host_Configuration_Protocol#DHCP_message_types
public class DHCPMessageType extends DHCPOption {
    private String message;

    public DHCPMessageType(byte[] data, int code) {
        super(data, code);
        
        switch (data[0] & 0xff) {
            case 1:
                message = "DISCOVER";
                break;
            case 2:
                message = "OFFER";
                break;
            case 3:
                message = "REQUEST";
                break;
            case 4:
                message = "DECLINE";
                break;
            case 5:
                message = "ACK";
                break;
            case 6:
                message = "NAK";
                break;
            case 7:
                message = "RELEASE";
                break;
            case 8:
                message = "INFORM";
                break;
            case 9:
                message = "FORCERENEW";
                break;
            case 10:
                message = "LEASEQUERY";
                break;
            case 11:
                message = "LEASEUNASSIGNED";
                break;
            case 12:
                message = "LEASEUNKNOWN";
                break;
            case 13:
                message = "LEASEACTIVE";
                break;
            case 14:
                message = "BULKLEASEQUERY";
                break;
            case 15:
                message = "LEASEQUERYDONE";
                break;
            case 16:
                message = "ACTIVELEASEQUERY";
                break;
            case 17:
                message = "LEASEQUERYSTATUS";
                break;
            case 18:
                message = "TLS";
                break;

            default:
                message = "Unknown";
                break;
        }
    }

    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + ": " + message + " ]\n";
    }
}
