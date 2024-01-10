public class Protocol {
    protected String name = "Unknown Protocol";
    protected byte[] data;

    public Protocol(byte[] data) {
        this.data = data;
    }

    public final String getName() { return name; }
    public final byte[] getData() { return data; }

    private String dataToString() {
        String rep = "";

        for (int i = 0; i < data.length ; ++i) {
            if ((i % 16) == 0) {
                rep += "\n";
            }
            rep += String.format("%02x ", data[i]);
        }

        return rep;
    }
    
    @Override
    public String toString() {
        return "[ " + name + " ]\n" + BinUtils.bytesToString(data, 16) + "\n";
    }

}