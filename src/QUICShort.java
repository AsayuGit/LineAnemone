import java.util.zip.DataFormatException;

public class QUICShort extends QUICHeader {
    public QUICShort(byte[] data)  throws DataFormatException {
        throw new DataFormatException("Not a QUIC short header !"); // Impossible to tell without context
                                                                    // Need something similar to FollowTCPStream
    }

    @Override
    public String toString() {
        return "[ QUIC Short ]\n";
    }
}
