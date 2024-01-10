// https://iponwire.com/dhcp-header-explained/
// http://www.tcpipguide.com/free/t_DHCPMessageFormat.htm

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DHCP extends Protocol {
    enum DHCPOpType {
        REQUEST,
        REPLY
    }

    private DHCPOpType type;
    private String hardwareType;

    private short hops;                 // Depends
    private long transactionID;
    private int secsElpsed; // To parse // Depends
    
    private byte[] clientIpAddress;
    private byte[] yourIpAddress;     // Offer, ACK
    private byte[] serverIpAddress;   // Offer, ACK
    private byte[] gatewayIpAddress;   // Discover
    private byte[] clientHardwareAddress;
    
    private String serverHostname;
    private String bootFileName;        // Offer

    private ArrayList<DHCPOption> options = new ArrayList<DHCPOption>();

    public DHCP(byte[] data) throws DataFormatException {
        super(data);
        name = "DHCP";

        try {
            // Check the magic byte to be sure it's a DHCP message
            if (BinUtils.uint32BEFromFile(data, 236) != 0x63825363) throw new DataFormatException("Not a DHCP Message");

            type = DHCPOpType.values()[data[0] - 1];
            hardwareType = ARP.parseARPHardwareType((short)(data[1] & 0xff));
            hops = (short)(data[3] & 0xff);
            transactionID = BinUtils.uint32BEFromFile(data, 4);
            secsElpsed = BinUtils.uint16BEFromFile(data, 8);

            clientIpAddress = Arrays.copyOfRange(data, 12, 16);
            yourIpAddress = Arrays.copyOfRange(data, 16, 20);
            serverIpAddress = Arrays.copyOfRange(data, 20, 24);
            gatewayIpAddress = Arrays.copyOfRange(data, 24, 28);

            int hardwareAddressLen = data[2] & 0xff;
            clientHardwareAddress = Arrays.copyOfRange(data, 28, 28 + hardwareAddressLen);

            serverHostname = new String(Arrays.copyOfRange(data, 44, 108));
            bootFileName = new String(Arrays.copyOfRange(data, 108, 236));

            options = DHCPOption.tryParseDhcpOptions(Arrays.copyOfRange(data, 240, data.length));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Not a DHCP Message !");
        }
    }

    @Override
    public String toString() {
        String rep = String.format("[ %s %s: [ ID: 0x%02x Sec: %d ] over %s ]\n", name, type, transactionID, secsElpsed, hardwareType);

        for (DHCPOption option : options) {
            rep += option;
        }

        return rep;
    }
    
}