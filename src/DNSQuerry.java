import java.util.zip.DataFormatException;

public class DNSQuerry extends DNSRecord {

    public DNSQuerry(byte[] data, int offset) throws DataFormatException {
        size = offset;
        this.offset = offset;

        try {
            name = loadDomainFromFile(data, data.length);
            parseRecordType(BinUtils.uint16BEFromFile(data, this.offset)); this.offset += 2;
            parseRecordClass(BinUtils.uint16BEFromFile(data, this.offset)); this.offset += 2;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not a DNSQuerry !");
        }

        size = this.offset - size;
    }

    @Override
    public String toString() {
        return "[ DNSQuerry: " + name + " " + getRecordType() + " " + getRecordClass() + " ]";
    }

}
