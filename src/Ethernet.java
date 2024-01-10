import java.util.Arrays;
import java.util.zip.DataFormatException;

public class Ethernet extends ContainerProtocol implements ITCPStreamDecorator {
    public static String macToString(byte[] macAddr) {
        String macStr = "";

        for (int index = 0; index < 6; ++index) {
            macStr += String.format("%02x%s", macAddr[index], (index < 5) ? ":" : "");
        }

        return macStr;
    }

    private byte[] destMAC = new byte[6];
    private byte[] srcMAC = new byte[6];

    private ITCPStreamDecorator parent;

    public Ethernet(byte[] data, ITCPStreamDecorator parent) throws DataFormatException {
        super(data);
        
        name = "Ethernet";
        this.parent = parent;

        destMAC = Arrays.copyOfRange(data, 0, 6);
        srcMAC = Arrays.copyOfRange(data, 6, 12);

        this.data = Arrays.copyOfRange(data, 0, 14);
        byte[] payload = Arrays.copyOfRange(data, 14, data.length);
        try {
            switch (BinUtils.uint16BEFromFile(data, 12)) {
                case 0x0800: // IPv4
                    protocol = new Ip4(payload, this);
                    break;
                case 0x86dd: // IPv6
                    protocol = new Ip6(payload, this);
                    break;
                case 0x0806: // ARP
                    protocol = new ARP(payload);
                    break;
                default:
                    protocol = new Protocol(payload);
                    break;
            }
        } catch (DataFormatException e) {
            protocol = new Protocol(payload);
        }
    }

    public String getDstMac() {
        return macToString(destMAC);
    }

    public String getSrcMac() {
        return macToString(srcMAC);
    }

    @Override
    public String toString() {
        return "[ Ethernet Frame: destMac: " + getDstMac() + " srcMac: " + getSrcMac() + " ]\n" + protocol;
    }

    @Override
    public int registerTCPSegment(TCP segment, String source, String destination) {
        return parent.registerTCPSegment(segment, source, destination);
    }
}