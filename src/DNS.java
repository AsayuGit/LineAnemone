import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class DNS extends Protocol {
    private final int headerLength = 12;

    private int transactionID;
    
    // Flags
    private boolean queryResponse;
    private byte queryType;
    private boolean authoritativeAnswer;
    private boolean truncation;
    private boolean recursionDesired;
    private boolean recursionAvailable;
    private byte responseCode;

    private int numberOfQuestions;
    private int numberOfAnswers;
    private int numberOfAuthorityRR;
    private int numberOfAdditionalRR;

    private ArrayList<DNSRecord> records = new ArrayList<DNSRecord>();
    private ArrayList<DNSRecord> additionalRecords = new ArrayList<DNSRecord>();

    public DNS(byte[] data) throws DataFormatException {
        super(data);
        
        if (data.length < headerLength) throw new DataFormatException("Malformed DNS Packet");

        name = "DNS";
        try {
            transactionID = BinUtils.uint16BEFromFile(data, 0);

            int flags = BinUtils.uint16BEFromFile(data, 2);
            
            queryResponse = ((flags & 0x8000) != 0);

            queryType = (byte) ((flags & 0x7800) >>> 11);

            authoritativeAnswer = ((flags & 0x0400) != 0);
            truncation = ((flags & 0x0200) != 0);
            recursionDesired = ((flags & 0x0100) != 0);
            recursionAvailable = ((flags & 0x0080) != 0);

            responseCode = (byte) (flags & 0x000f);

            numberOfQuestions = BinUtils.uint16BEFromFile(data, 4);
            numberOfAnswers = BinUtils.uint16BEFromFile(data, 6);
            numberOfAuthorityRR = BinUtils.uint16BEFromFile(data, 8);
            numberOfAdditionalRR = BinUtils.uint16BEFromFile(data, 10);

            // Try to parse all dns queries (in practice there is rarely more than one)
            int offset = headerLength;
            for (int queryIndex = 0; (queryIndex < numberOfQuestions) && (offset < data.length); ++queryIndex) {
                DNSRecord newQuerry = new DNSQuerry(data, offset);
                offset += newQuerry.size();
                records.add(newQuerry);
            }

            
            for (int recordIndex = 0; (recordIndex < numberOfAnswers) && (offset < data.length); ++recordIndex) {
                DNSRecord newRecord = new DNSAnswer(data, offset);
                records.add(newRecord);
                offset += newRecord.size();
            }

            // TODO: Implement EDNS
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not a DNS Packet");
        }
    }
    
    @Override
    public String toString() {
        String stringRep = "[ DNS: ID: " + transactionID + " ]\n";
        
        for (DNSRecord record : records) {
            stringRep += record + "\n";
        }

        return stringRep;
    }
}
