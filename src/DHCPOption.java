// http://www.tcpipguide.com/free/t_DHCPOptionsOptionFormatandOptionOverloading-2.htm
// http://www.tcpipguide.com/free/t_SummaryOfDHCPOptionsBOOTPVendorInformationFields-2.htm

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DHCPOption {
    public final static ArrayList<DHCPOption> tryParseDhcpOptions(byte[] data) {
        ArrayList<DHCPOption> options = new ArrayList<DHCPOption>();
        
        for (int offset = 0; offset < data.length;) {
            DHCPOption option = null;

            short code = (short)(data[offset++] & 0xff);
            if (code == 255) {
                options.add(new DHCPEnd());
                break;
            }

            short length = (short)(data[offset++] & 0xff);
            byte[] optionData = Arrays.copyOfRange(data, offset, offset += length);

            try {
                switch (code) {
                    case 1:
                    case 3:
                    case 6:
                    case 50:
                    case 54:
                        option = new DHCPAddress(optionData, code);
                        break;

                    case 12:
                    case 15:
                    case 60:
                    case 81:
                        option = new DHCPString(optionData, code, length);
                        break;

                    case 51:
                    case 58:
                    case 59:
                        option = new DHCPTime(optionData, code);
                        break;

                    case 53:
                        option = new DHCPMessageType(optionData, code);
                        break;

                    case 55:
                        option = new DHCPParameterList(optionData, code, length);
                        break;

                    case 61:
                        option = new DHCPHardwareAddress(optionData, code);
                        break;

                    case 125:
                        option = new DHCPVendorInfo(optionData, code);
                        break;

                    case 0:
                        continue;

                    default:
                        option = new DHCPOption(optionData, code);
                        break;
                }

                options.add(option);
            } catch (DataFormatException e) {
                options.add(new DHCPOption(optionData, code));
            }
        }

        return options;
    }

    public final static String parseOptionName(int code) {
        switch (code) {
            case 1:
                return "Subnet Mask";
            case 3:
                return "Router";
            case 6:
                return "Domain Name Server";
            case 12:
                return "Host Name";
            case 15:
                return "Domain Name";
            case 31:
                return "Perform Router Discover";
            case 33:
                return "Static Route";
            case 42:
                return "Network Time Protocol Servers";
            case 43:
                return "Vendor-Specific Information";
            case 44:
                return "NetBIOS over TCP/IP Name Server";
            case 46:
                return "NetBIOS over TCP/IP Node Type";
            case 47:
                return "NetBIOS over TCP/IP Scope";
            case 50:
                return "Requested IP Address";
            case 51:
                return "IP Address Lease Time";
            case 53:
                return "Message Type";
            case 54:
                return "Server Identifier";
            case 55:
                return "Parameter Request List";
            case 58:
                return "Renewal Time Value";
            case 59:
                return "Rebinding Time Value";
            case 60:
                return "Vendor class identifier";
            case 61:
                return "Client identifier";
            case 81:
                return "Client Fully Qualified Domain Name";
            case 119:
                return "Domain Search";
            case 121:
                return "Classless Static Route";
            case 125:
                return "V-I Vendor-specific Information";
            case 249:
                return "Private/Classless Static Route (Microsoft)";
            case 252:
                return "Private/Proxy autodiscovery";

            case 255:
                return "END";
        
            default:
                return "Unknown";
        }
    }

    protected byte[] data;
    protected int code;

    public DHCPOption(byte[] data, int code) {
        this.data = data;
        this.code = code;
    }

    @Override
    public String toString() {
        return "[ DHCP Unknown Option: " + code + " ]\n" + BinUtils.bytesToString(data, 16) + "\n";
    }
}
