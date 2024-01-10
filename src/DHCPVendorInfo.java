public class DHCPVendorInfo extends DHCPOption {

    public DHCPVendorInfo(byte[] data, int code) {
        super(data, code);

        // TODO: Process Info
    }

    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + " ]\n";
    }
}
