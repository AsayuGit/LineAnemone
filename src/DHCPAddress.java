import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DHCPAddress extends DHCPOption {
    private String address;

    public DHCPAddress(byte[] data, int code) throws DataFormatException {
        super(data, code);
        
        address = Ip4.ipTostring(Arrays.copyOfRange(data, 0, 4));
    }
    
    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + ": " + address + " ]\n";
    }
}
