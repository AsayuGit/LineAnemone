public class ContainerProtocol extends Protocol {
    protected Protocol protocol = null;

    public ContainerProtocol(byte[] data) {
        super(data);
        name = "Unknown Container Protocol";
    }

    public final Protocol getChildProtocol() { return protocol; }
}
