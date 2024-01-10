import java.util.ArrayList;

public class QUICv0 extends QUICVariableHeader {
    private ArrayList<Long> supportedVersions;

    public QUICv0(byte[] data, int offset) {
        // Loads each supported version
        for (; offset + 4 < data.length; offset += 4) {
            supportedVersions.add(BinUtils.uint32BEFromFile(data, offset));
        }
    }

    @Override
    public String toString() {
        String rep = "[ QUIC Version Negotiation ]\n";

        for (long version : supportedVersions) {
            rep += version + "\n";
        }

        return rep;
    }
}
