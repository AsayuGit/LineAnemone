import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DNSAnswer extends DNSRecord {
    private long timeToLive;

    private int dataLength;

    private String additionalData = "";

    public DNSAnswer(byte[] data, int offset) throws DataFormatException {
        if ((data[offset++] & 0xff) != 0xc0) {
            throw new DataFormatException("Not a DNSAnswer !");        
        }
        size = offset;

        try {
            this.offset = (short)(data[offset++] & 0xff);
            name = loadDomainFromFile(data, data.length);
            this.offset = offset;

            parseRecordType(BinUtils.uint16BEFromFile(data, this.offset)); this.offset += 2;
            parseRecordClass(BinUtils.uint16BEFromFile(data, this.offset)); this.offset += 2;

            timeToLive = BinUtils.uint32BEFromFile(data, this.offset); this.offset += 4;
            
            dataLength = BinUtils.uint16BEFromFile(data, this.offset); this.offset += 2;

            byte[] address;
            switch (recordType) {
                case 1: // IPv4 Address
                    address = Arrays.copyOfRange(data, this.offset, this.offset += 4); this.offset++;
                    additionalData = Ip4.ipTostring(address);
                    break;
                case 28: // IPv6 Address
                    address = Arrays.copyOfRange(data, this.offset, this.offset += 16); this.offset++; 
                    additionalData = Ip6.ipTostring(address);
                    break;
                case 2: // NS
                case 5: // CNAME
                    additionalData = loadDomainFromFile(data, this.offset + dataLength);
                    break;
                case 15: // Extended Domain
                    {
                        int preference = BinUtils.uint16BEFromFile(data, this.offset); this.offset += 2;
                        additionalData = preference + " " + loadDomainFromFile(data, this.offset + dataLength - 2);
                    }
                    break;

                default:
                    break;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not a DNSAnswer !");
        }

        size = this.offset - size;
    }

    @Override
    public String toString() {
        return "[ DNSAnswer: " + name  + " " + timeToLive + " " + getRecordType() + " " + getRecordClass() + " " + additionalData + " ]";
    }
}
