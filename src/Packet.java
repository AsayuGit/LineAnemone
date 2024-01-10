import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class Packet {
    private int id;

    private LocalDateTime timeStamp;
    private long timeStampSeconds;
    private long timeStampFine;

    private long packetLength;
    private long originalPacketLength;

    private Protocol protocol;

    public Packet(int id, byte[] data, int offset, Pcap pcap) throws DataFormatException {
        this.id = id;

        timeStampSeconds = BinUtils.uint32LEFromFile(data, offset); offset += 4;
        timeStampFine = BinUtils.uint32LEFromFile(data, offset); offset += 4;
        timeStamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStampSeconds), ZoneId.systemDefault());

        packetLength += BinUtils.uint32LEFromFile(data, offset); offset += 4;
        originalPacketLength += BinUtils.uint32LEFromFile(data, offset); offset += 4;

        byte[] payload = Arrays.copyOfRange(data, offset, (int)(offset + packetLength));
        try {
            protocol = new Ethernet(payload, pcap);
        } catch (DataFormatException e) {
            protocol = new Protocol(payload);
        }
    }

    public long getFileLength() { return 16 + packetLength; }
    public long getPacketLength() { return originalPacketLength; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public long getTimeStampSeconds() { return timeStampSeconds; }
    public long getTimeStampFine() { return timeStampFine; }

    public String toString() {
        return "[ Packet " + id + ": Time: " + getTimeStamp() + " ]\n" + protocol;
    }
}
