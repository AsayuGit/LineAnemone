import java.util.Arrays;
import java.util.zip.DataFormatException;

public class UDP extends TransportProtocol {
    private int size;

    public UDP(byte[] data) throws DataFormatException {
        super(data);

        if (data.length < 8) throw new DataFormatException("Malformed UDP Segment");
        name = "UDP";

        try {
            srcPort = BinUtils.uint16BEFromFile(data, 0);
            dstPort = BinUtils.uint16BEFromFile(data, 2);
            size = BinUtils.uint16BEFromFile(data, 4);

            this.data = Arrays.copyOfRange(data, 0, 8);
            protocol = tryParseAppProtocol(Arrays.copyOfRange(data, 8, data.length));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Malformed UDP Segment");
        }
    }

    @Override
    public String toString() {
        String udpRep = "[ " + getName() + ": srcPort: " + getSrcPort() + " dstPort: " + getDstPort() + " ]\n";
        
        if (protocol != null) udpRep += protocol;

        return udpRep;
    }
}
