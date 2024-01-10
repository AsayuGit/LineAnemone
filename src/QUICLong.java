import java.util.Arrays;
import java.util.zip.DataFormatException;

public class QUICLong extends QUICHeader {
    private long version;
    private byte[] destinationConnectionID;
    private byte[] sourceConnectionID;
    private QUICVariableHeader variableHeader;

    public QUICLong(byte[] data) throws DataFormatException {
        version = BinUtils.uint32BEFromFile(data, 1);

        short destConIdLen = (short)(data[5] & 0xff);
        int offset = 6 + destConIdLen;
        destinationConnectionID = Arrays.copyOfRange(data, 6, offset);

        short srcConIdLen = (short)(data[offset++] & 0xff);
        sourceConnectionID = Arrays.copyOfRange(data, offset, offset += srcConIdLen);

        if (version == 0) {
            variableHeader = new QUICv0(data, offset);
        } else if (version == 1) {
            variableHeader = new QUICv1(data, offset);
        } else {
            // Something else
        }
    }

    @Override
    public String toString() {
        String rep = "[ QUIC Long v" + version + " ]\n" ;

        rep += "  dstConID: " + BinUtils.bytesToString(destinationConnectionID) + "\n";
        rep += "  srcConID: " + BinUtils.bytesToString(sourceConnectionID) + "\n";

        rep += variableHeader;

        return rep;
    }
}
