import java.util.Hashtable;
import java.util.Map;

public class DHCPParameterList extends DHCPOption {
    private Hashtable<Integer, String> parameterList = new Hashtable<Integer, String>();

    public DHCPParameterList(byte[] data, int code, short length) {
        super(data, code);

        for (int offset = 0; offset < length; ++offset) {
            int option = data[offset] & 0xff;
            parameterList.put(option, DHCPOption.parseOptionName(option));
        }
    }

    @Override
    public String toString() {
        String rep = "[ DHCP " + parseOptionName(code) + " ]\n";

        for (Map.Entry<Integer, String> entry : parameterList.entrySet()) {
            rep += String.format("  Request: %s (%d) \n", entry.getValue(), entry.getKey());
        }

        return rep;
    }
    
}
