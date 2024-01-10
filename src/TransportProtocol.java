import java.util.zip.DataFormatException;

public abstract class TransportProtocol extends ContainerProtocol {
    protected int srcPort;
    protected int dstPort;

    public TransportProtocol(byte[] data) {
        super(data);
        name = "Unknown Transport Protcol";
    }

    public final int getSrcPort() {
        return srcPort;
    }

    public final int getDstPort() {
        return dstPort;
    }

    protected static Protocol tryParseAppProtocol(byte[] payload) {
        Protocol protocol = null;
        int classIndex = 0;
        while (payload.length > 0) {
            try {
                switch (classIndex) {
                    case 0:
                        protocol = new DNS(payload);
                        break;
                    case 1:
                        protocol = new HTTP(payload);
                        break;
                    case 2:
                        protocol = new QUIC(payload);
                        break;
                    case 3:
                        protocol = new DHCP(payload);
                        break;
                    default:
                        protocol = new Protocol(payload);
                        break;
                }
            } catch (DataFormatException e) {
                ++classIndex;
                continue;
            }
            break;
        }

        return protocol;
    }
}
