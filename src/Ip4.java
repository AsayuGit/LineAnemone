import java.util.Arrays;
import java.util.zip.DataFormatException;

public class Ip4 extends NetworkProtocol {
    public static String ipTostring(byte[] ipAddr) throws DataFormatException {
        if (ipAddr.length != 4) throw new DataFormatException("Not an IPv4 Address !");
        String ipStr = "";

        for (int index = 0; index < 4; ++index) {
            ipStr += String.format("%d%s", ipAddr[index] & 0xff, (index < 3) ? "." : "");
        }

        return ipStr;
    }

    private byte version;

    private short serviceType;
    private short timeToLive;
    
    private long fragmentID;
    private long fragmentOffset;
    
    // Flags
    private boolean dontFragment;
    private boolean moreFragment;

    public Ip4(byte data[], ITCPStreamDecorator parent) throws DataFormatException {
        super(data, parent);
        
        try {
            byte headerLength = (byte) (data[0] & 0xf);
            version = (byte)(data[0] >>> 4);
            if (version != 4) throw new DataFormatException("Not an IPv4 Packet");

            name = "IPv4";
            serviceType = (short)(data[1] & 0xff);

            int totalLength = BinUtils.uint16BEFromFile(data, 2);
            fragmentID = BinUtils.uint16BEFromFile(data, 4);

            dontFragment = (data[6] & 0b10) != 0;
            moreFragment = (data[6] & 0b01) != 0;
            
            fragmentOffset = BinUtils.uint16BEFromFile(data, 6) & 0x1fff;
            
            timeToLive = (short)(data[8] & 0xff);

            sourceAddress = Arrays.copyOfRange(data, 12, 16);
            destinationAddress = Arrays.copyOfRange(data, 16, 20);

            sourceAddressStr = ipTostring(sourceAddress);
            destinationAddressStr = ipTostring(destinationAddress);

            this.data = Arrays.copyOfRange(data, 0, 20);
            byte[] payload = Arrays.copyOfRange(data, 20, totalLength);
            try {
                switch ((short)(data[9] & 0xff)) {
                    case 1:
                        protocol = new Icmp(payload);
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
            } catch (DataFormatException a) {
                protocol = new Protocol(payload);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not an IPv4 Packet");
        }
    }

    @Override
    public String toString() {
        return "[ IPv4 Packet: srcIp: " + sourceAddressStr + " dstIp: " + destinationAddressStr + " ]\n" + protocol;
    }
}
