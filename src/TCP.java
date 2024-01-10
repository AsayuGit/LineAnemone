import java.util.Arrays;
import java.util.zip.DataFormatException;

public class TCP extends TransportProtocol {
    private long sequenceNumber;
    private long acknowledgementNumber;

    private byte dataOffset;

    // Flags
    private boolean CWR;
    private boolean ECE;
    private boolean URG;
    private boolean ACK;
    private boolean PSH;
    private boolean RST;
    private boolean SYN;
    private boolean FIN;

    private int windowSize;
    private int checksum;
    private int urgentPointer;

    private int streamID;

    public TCP(byte[] data, ITCPStreamDecorator parent) throws DataFormatException {
        super(data);
        
        name = "TCP";
        try {
            srcPort = BinUtils.uint16BEFromFile(data, 0);
            dstPort = BinUtils.uint16BEFromFile(data, 2);

            sequenceNumber = BinUtils.uint32BEFromFile(data, 4);
            acknowledgementNumber = BinUtils.uint32BEFromFile(data, 8);

            dataOffset = (byte)((data[12] & 0xf0) >> 4);
            
            CWR = (data[13] & 0x80) != 0;
            ECE = (data[13] & 0x40) != 0;
            URG = (data[13] & 0x20) != 0;
            ACK = (data[13] & 0x10) != 0;
            PSH = (data[13] & 0x08) != 0;
            RST = (data[13] & 0x04) != 0;
            SYN = (data[13] & 0x02) != 0;
            FIN = (data[13] & 0x01) != 0;

            windowSize = BinUtils.uint16BEFromFile(data, 14);
            checksum = BinUtils.uint16BEFromFile(data, 16); // TOTO: Verify Checksum
            urgentPointer = BinUtils.uint16BEFromFile(data, 18);

            this.data = Arrays.copyOfRange(data, 0, dataOffset * 4);
            protocol = tryParseAppProtocol(Arrays.copyOfRange(data, dataOffset * 4, data.length));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not a TCP Segment !");
        }

        streamID = parent.registerTCPSegment(this, "" + getSrcPort(), "" +getDstPort());
    }

    @Override
    public String toString() {
        String tcpRep ="[ " + getName() + " Stream " + streamID + " : srcPort: " + getSrcPort() + " dstPort: " + getDstPort() + " [ ";
        
        if (CWR) tcpRep += "CWR ";
        if (ECE) tcpRep += "ECE ";
        if (URG) tcpRep += "URG ";
        if (ACK) tcpRep += "ACK ";
        if (PSH) tcpRep += "PSH ";
        if (RST) tcpRep += "RST ";
        if (SYN) tcpRep += "SYN ";
        if (FIN) tcpRep += "FIN ";

        tcpRep += "] ]\n";

        if (protocol != null) tcpRep += protocol;

        return tcpRep;
    }

    // Flags getters
    public boolean getCWR() { return CWR; }
    public boolean getECE() { return ECE; }
    public boolean getURG() { return URG; }
    public boolean getACK() { return ACK; }
    public boolean getPSH() { return PSH; }
    public boolean getRST() { return RST; }
    public boolean getSYN() { return SYN; }
    public boolean getFIN() { return FIN; }
}