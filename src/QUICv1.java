import java.util.Arrays;

public class QUICv1 extends QUICVariableHeader {
    enum QUICPacketType {
        INITIAL,
        ZERORTT,
        HANDSHAKE,
        RETRY
    }

    private QUICPacketType type;
    private byte[] payload;

    public QUICv1(byte[] data, int offset) {
        type = QUICPacketType.values()[(data[0] & 0x30) >>> 4];

        payload = Arrays.copyOfRange(data, offset, data.length);
    }

    @Override
    public String toString() {
        String rep = "[ QUICv1 " + type + " ]\n";
        
        rep += BinUtils.bytesToString(payload, 16, 5) + "\n";

        return rep;
    }
}
