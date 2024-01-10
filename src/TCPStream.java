import java.util.ArrayList;

public class TCPStream extends ArrayList<TCP> {
    private String source;
    private String destination;

    public TCPStream(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() { return source; }
    public String getDestination() { return destination; }

    @Override
    public String toString() {
        return "[TCPStream: " + getSource() + " -> " + getDestination() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TCPStream)) return false;
        TCPStream stream = (TCPStream)o;
        return (((stream.getSource().equals(getSource())) && (stream.getDestination().equals(getDestination()))) ||
                ((stream.getSource().equals(getDestination())) && (stream.getDestination().equals(getSource()))));
    }
}
