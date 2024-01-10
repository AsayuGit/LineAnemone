import java.util.zip.DataFormatException;

public abstract class DNSRecord {
    protected int recordType;
    protected int recordClass;

    protected String recordTypeStr;
    protected String recordClassStr;

    protected String name;
    protected int size;

    protected int offset;

    public String getRecordName() {
        return name;
    }

    public String getRecordType() {
        return recordTypeStr;
    }

    public String getRecordClass() {
        return recordClassStr;
    }

    public int size() {
        return size;
    }

    protected String loadDomainFromFile(byte[] data, int maxOffset) throws DataFormatException {
        int offset = this.offset;

        String domainName = "";
        
        short len = (short)(data[offset++] & 0xff);
        if (len == 0) throw new DataFormatException("Not a DNS Record");

        try {
            while (true) {
                if (len == 0xC0) {
                    this.offset = (short)(data[offset++] & 0xff);
                    domainName += loadDomainFromFile(data, maxOffset);
                    offset++;
                } else  {
                    for (; len > 0; --len) {
                        domainName += (char)data[offset++];
                    }
                }


                if (offset >= maxOffset) break;

                if ((len = (short)(data[offset++] & 0xff)) == 0) break;
                domainName += ".";
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            
            throw new DataFormatException("Not a DNS Record");
        }
        
        this.offset = offset;

        return domainName;
    }

    protected void parseRecordClass(int recordClass) throws DataFormatException {
        this.recordClass = recordClass;

        switch (recordClass) {
            case 1:
                recordClassStr = "IN";
                break;
            case 2:
                recordClassStr = "CS";
                break;
            case 3:
                recordClassStr = "CH";
                break;
            case 4:
                recordClassStr = "HS";
                break;
            default:        
                throw new DataFormatException("Not a DNS Record");
        }
    }

    protected void parseRecordType(int recordType) throws DataFormatException {
        this.recordType = recordType;

        switch (recordType) {
            case 1:
                recordTypeStr = "A";
                break;
            case 2:
                recordTypeStr = "NS";
                break;
            case 5:
                recordTypeStr = "CNAME";
                break;
            case 6:
                recordTypeStr = "SOA";
                break;
            case 12:
                recordTypeStr = "PTR";
                break;
            case 13:
                recordTypeStr = "HINFO";
                break;
            case 15:
                recordTypeStr = "MX";
                break;
            case 16:
                recordTypeStr = "TXT";
                break;
            case 17:
                recordTypeStr = "RP";
                break;
            case 18:
                recordTypeStr = "AFSDB";
                break;
            case 24:
                recordTypeStr = "SIG";
                break;
            case 25:
                recordTypeStr = "KEY";
                break;
            case 28:
                recordTypeStr = "AAAA";
                break;
            case 29:
                recordTypeStr = "LOC";
                break;
            case 33:
                recordTypeStr = "SRV";
                break;
            case 35:
                recordTypeStr = "NAPTR";
                break;
            case 36:
                recordTypeStr = "KX";
                break;
            case 37:
                recordTypeStr = "CERT";
                break;
            case 39:
                recordTypeStr = "DNAME";
                break;
            case 42:
                recordTypeStr = "APL";
                break;
            case 43:
                recordTypeStr = "DS";
                break;
            case 44:
                recordTypeStr = "SSHFP";
                break;
            case 45:
                recordTypeStr = "IPSECKEY";
                break;
            case 46:
                recordTypeStr = "RRSIG";
                break;
            case 47:
                recordTypeStr = "NSEC";
                break;
            case 48:
                recordTypeStr = "DNSKEY";
                break;
            case 49:
                recordTypeStr = "DHCID";
                break;
            case 50:
                recordTypeStr = "NSEC3";
                break;
            case 51:
                recordTypeStr = "NSEC3PARAM";
                break;
            case 52:
                recordTypeStr = "TLSA";
                break;
            case 53:
                recordTypeStr = "SMIMEA";
                break;
            case 55:
                recordTypeStr = "HIP";
                break;
            case 59:
                recordTypeStr = "CDS";
                break;
            case 60:
                recordTypeStr = "CDNSKEY";
                break;
            case 61:
                recordTypeStr = "OPENPGPKEY";
                break;
            case 62:
                recordTypeStr = "CSYNC";
                break;
            case 63:
                recordTypeStr = "ZONEMD";
                break;
            case 64:
                recordTypeStr = "SVCB";
                break;
            case 65:
                recordTypeStr = "HTTPS";
                break;
            case 108:
                recordTypeStr = "EUI48";
                break;
            case 109:
                recordTypeStr = "EUI64";
                break;
            case 249:
                recordTypeStr = "TKEY";
                break;
            case 250:
                recordTypeStr = "TSIG";
                break;
            case 255:
                recordTypeStr = "ANY";
                break;
            case 256:
                recordTypeStr = "URI";
                break;
            case 257:
                recordTypeStr = "CAA";
                break;
            case 32768:
                recordTypeStr = "TA";
                break;
            case 32769:
                recordTypeStr = "DLV";
                break;
            default:
                throw new DataFormatException("Not a DNS Record");
        }
    }

}
