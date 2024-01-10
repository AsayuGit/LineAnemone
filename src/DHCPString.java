import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DHCPString extends DHCPOption {
    private String name = "";

    public DHCPString(byte[] data, int code, short length) throws DataFormatException {
        super(data, code);

        int offset = 0;

        switch (code) {
            case 81:
                // TODO: process flags
                offset = 3;
                break;
        
            default:
                break;
        }
        
        name = new String(Arrays.copyOfRange(data, offset, length));
    }
    
    @Override
    public String toString() {
        return "[ DHCP " + parseOptionName(code) + ": " + name + " ]\n";
    }
}
