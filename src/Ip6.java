import java.util.Arrays;
import java.util.zip.DataFormatException;

public class Ip6 extends NetworkProtocol {    
    public static String ipTostring(byte[] ipAddr) throws DataFormatException {
        if (ipAddr.length != 16) throw new DataFormatException("Not an IPv6 Address !");
        // TODO: Very incomplete
        String ipStr = "";

        for (int index = 0; index < 16; ++index) {
            ipStr += String.format("%02x", ipAddr[index] & 0xff);
            if ((index < 15) && (index % 2 != 0)) {
                ipStr += ":";
            }
        }

        return ipStr;
    }

    private byte version;
    private short traficClass;
    private int flowLabel;
    private int payloadLength;
    private short nextHeader;
    private short hopLimit;

    public Ip6(byte[] data, ITCPStreamDecorator parent) throws DataFormatException {
        super(data, parent);
    
        try {
            version = (byte)((short)(data[0] & 0xf0) >> 4);
            if (version != 6) throw new DataFormatException("Not an IPv6 Packet");

            name = "IPv6";
            traficClass = (short)(((data[0] & 0x0f) << 4) | ((data[1] & 0xf0) >> 4));
            flowLabel = (int)(BinUtils.uint32BEFromFile(data, 0) & 0x000fffff);
            
            payloadLength = BinUtils.uint16BEFromFile(data, 4);
            nextHeader = (short)(data[6] & 0xff);
            hopLimit = (short)(data[7] & 0xff);

            sourceAddress = Arrays.copyOfRange(data, 8, 24);
            destinationAddress = Arrays.copyOfRange(data, 24, 40);

            sourceAddressStr = ipTostring(sourceAddress);
            destinationAddressStr = ipTostring(destinationAddress);

            this.data = Arrays.copyOfRange(data, 0, 40);
            byte[] payload = Arrays.copyOfRange(data, 40, payloadLength);
            try {
                // Could be mutualized with ipv4 ?
                switch (nextHeader) {
                    case 1:
                        // should be ICMP v6
                        //protocol = new Icmp(payload);
                        break;
                    case 6:
                        protocol = new TCP(payload, this);
                        break;
                    case 17:
                        protocol = new UDP(payload);
                        break;
                    default:
                        protocol = new Protocol(payload);
                        break;
                }
            } catch (DataFormatException e) {
                protocol = new Protocol(payload);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not an IPv6 Packet");
        }
    }

    @Override
    public String toString() {
        return "[ IPv6 Packet: srcIp: " + sourceAddressStr + " dstIp: " + destinationAddressStr + " ]\n" + protocol;
    }
}