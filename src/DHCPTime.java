public class DHCPTime extends DHCPOption {
    private long seconds;

    public DHCPTime(byte[] data, int code) {
        super(data, code);

        seconds = BinUtils.uint32BEFromFile(data, 0);
    }
    
    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + " (" + seconds + "s) ]\n";
    }
}
