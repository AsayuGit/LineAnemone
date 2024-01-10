import java.util.zip.DataFormatException;

// https://www.rfc-editor.org/rfc/rfc8999.html
public class QUIC extends ContainerProtocol {
    private QUICHeader header;

    public QUIC(byte[] data) throws DataFormatException {
        super(data);
        name = "QUIC";

        if ((data[0] & 0x80) == 0) {
            header = new QUICShort(data);
        } else {
            header = new QUICLong(data);
        }
    }

    @Override
    public String toString() {
        return header.toString();
    }
}
