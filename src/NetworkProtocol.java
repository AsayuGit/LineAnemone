public class NetworkProtocol extends ContainerProtocol implements ITCPStreamDecorator {
    protected byte[] sourceAddress;
    protected byte[] destinationAddress;

    protected String sourceAddressStr = "";
    protected String destinationAddressStr = "";

    private ITCPStreamDecorator parent;;

    public NetworkProtocol(byte[] data, ITCPStreamDecorator parent) {
        super(data);
        name = "Unknown Network Protocol";
        this.parent = parent;
    }

    public final String getDstIp() {
        return destinationAddressStr;
    }

    public final String getSrcIp() {
        return sourceAddressStr;
    }

    @Override
    public final int registerTCPSegment(TCP segment, String source, String destination) {
        return parent.registerTCPSegment(segment, getSrcIp() + "-" + source, getDstIp() + "-" + destination);
    }
}
