import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DHCPHardwareAddress extends DHCPOption {
    private String hardwareType;
    private String hardwareAddress;

    public DHCPHardwareAddress(byte[] data, int code) throws DataFormatException {
        super(data, code);

        hardwareType = ARP.parseARPHardwareType(data[0] & 0xff);
        hardwareAddress = Ethernet.macToString(Arrays.copyOfRange(data, 1, 7));
    }

    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + ": " + hardwareAddress + " over " + hardwareType + " ]\n";
    }
}
