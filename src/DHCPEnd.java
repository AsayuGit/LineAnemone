public class DHCPEnd extends DHCPOption{
    public DHCPEnd() {
        super(null, 255);
    }

    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + " ]\n";
    }
}
