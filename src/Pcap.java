import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.DataFormatException;

class Pcap implements ITCPStreamDecorator {
    // Pcap Header Data
    private long version;
    private long snapLen;

    private ArrayList<Packet> packets = new ArrayList<Packet>();
    private ArrayList<TCPStream> tcpStreams = new ArrayList<TCPStream>(); 

    public Pcap(String path) throws IOException, DataFormatException {    
        Path pcapFilePath = Paths.get(path);
        byte[] pcapFile = Files.readAllBytes(pcapFilePath);
        
        parseHeader(pcapFile);
        parsePackets(pcapFile);
    }

    public Packet getPacket(int packetID) {
        Packet packet;

        try {
            packet = packets.get(packetID);
        } catch (IndexOutOfBoundsException e) {
            packet = null;
        }

        return packet;
    }

    private void parseHeader(byte[] data) throws DataFormatException {
        long magic = BinUtils.uint32LEFromFile(data, 0);
        if (magic != 0xa1b2c3d4L) throw new DataFormatException("Invalid Pcap Header");

        version = BinUtils.uint32LEFromFile(data, 4);
        snapLen = BinUtils.uint32LEFromFile(data, 16);
    }

    private void parsePackets(byte[] data) {
        Packet packet;
        for (int dataIndex = 24; dataIndex < data.length;) {
            try {
                packet = new Packet(packets.size(), data, dataIndex, this);
                packets.add(packet);
                dataIndex += packet.getFileLength();
            } catch (DataFormatException e) {
                System.out.println("Malformed packet ! Aborting ...");
                break;
            }
        }
    }

    public String toString() {
        String stringRep = "";

        int index = 0;
        for (Packet packet : packets) {
            stringRep += packet + "\n\n";
            ++index;
        }

        return stringRep;
    }

        
    // Register a tcp segment amongs all and return its tcp stream ID
    @Override
    public int registerTCPSegment(TCP segment, String source, String destination) {
        // We first try to insert the segment in an existing tcp stream
        TCPStream newStream = new TCPStream(source, destination);

        for (TCPStream stream : tcpStreams) {
            if (stream.equals(newStream)) {
                addTCPSegmentToTCPStream(segment, stream);
                return tcpStreams.indexOf(stream);
            }
        }

        // If None can be found we create a new one
        tcpStreams.add(newStream);
        return tcpStreams.size() - 1;
    }

    private void addTCPSegmentToTCPStream(TCP segment, TCPStream stream) {
        // TODO: Need to properly insert it based on sequence and ack numbers
        stream.add(segment);
    }

    // Get a tcp stream from its stream ID
    public ArrayList<Protocol> followTCPStream(int streamID) {
        ArrayList<Protocol> streamProtocols = new ArrayList<Protocol>();
        
        try {
            TCPStream stream = tcpStreams.get(streamID);
            ByteArrayOutputStream sequenceStream = new ByteArrayOutputStream();

            for (TCP segment : stream) {
                Protocol childProtocol = segment.getChildProtocol();
                if (childProtocol == null) continue;
                
                byte[] segmentData = childProtocol.getData();
                sequenceStream.write(segmentData, 0, segmentData.length);

                if (segment.getPSH()) {
                    byte[] sequenceData = sequenceStream.toByteArray();
                    Protocol protocol = TransportProtocol.tryParseAppProtocol(sequenceData);

                    if (protocol != null) {
                        streamProtocols.add(protocol);
                    }

                    sequenceStream = new ByteArrayOutputStream();
                }
            }
        } catch (IndexOutOfBoundsException e) {}

        return streamProtocols;
    }
}