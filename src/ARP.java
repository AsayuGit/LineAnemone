import java.util.Arrays;
import java.util.zip.DataFormatException;

public class ARP extends Protocol {
    private int protocolType;
    private int operation;

    private short hardwareAddrLen;
    private short protocolAddrLen;

    private byte[] srcMAC = new byte[6];
    private byte[] destMAC = new byte[6];

    private byte[] srcIP = new byte[4];
    private byte[] destIP = new byte[4];

    private String srcIPStr;
    private String destIPStr;

    
    public ARP(byte[] data) throws DataFormatException {
        super(data);
        
        if (data.length < 24) throw new DataFormatException("Malformed ARP Packet");
        name = "ARP";

        try {
            protocolType = BinUtils.uint16BEFromFile(data, 2);
            
            hardwareAddrLen = (short) (data[4] & 0xff);
            protocolAddrLen = (short) (data[5] & 0xff);

            operation = BinUtils.uint16BEFromFile(data, 6);

            srcMAC = Arrays.copyOfRange(data, 8, 14);
            destMAC = Arrays.copyOfRange(data, 18, 24);

            srcIP = Arrays.copyOfRange(data, 14, 18);
            destIP = Arrays.copyOfRange(data, 24, 28);

            srcIPStr = Ip4.ipTostring(srcIP);
            destIPStr = Ip4.ipTostring(destIP);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataFormatException("Malformed ARP Packet");
        }
    }

    public String getDstMac() {
        return Ethernet.macToString(destMAC);
    }

    public String getSrcMac() {
        return Ethernet.macToString(srcMAC);
    }

    public String getDstIp() {
        return destIPStr;
    }

    public String getSrcIp() {
        return srcIPStr;
    }

    @Override
    public String toString() {
        String arpStr = "";

        switch (operation) {
            case 1: // Request
                arpStr = "[ ARP: Who has " + destIPStr + " ? Please tell " + srcIPStr + " ]";
                break;
            case 2: // Reply
                arpStr = "[ ARP: Host " + srcIPStr + " is at " + getSrcMac() + " ]";
                break;
            default:
                break;
        }
        
        return arpStr + "\n";
    }

    // https://www.iana.org/assignments/arp-parameters/arp-parameters.xhtml
    // https://www.frameip.com/entete-arp/
    public static String parseARPHardwareType(int type) {
        String hardwareType = "";

        switch (type) {
            case 0:
            case 65535:
                hardwareType = "Reserved";
                break;
            case 1:
                hardwareType = "Ethernet (10Mb)";
                break;
            case 2:
                hardwareType = "Experimental Ethernet (3Mb)";
                break;
            case 3:
                hardwareType = "Amateur Radio AX.25";
                break;
            case 4:
                hardwareType = "Proteon ProNET Token Ring";
                break;
            case 5:
                hardwareType = "Chaos";
                break;
            case 6:
                hardwareType = "IEEE 802 Networks";
                break;
            case 7:
                hardwareType = "ARCNET";
                break;
            case 8:
                hardwareType = "Hyperchannel";
                break;
            case 9:
                hardwareType = "Lanstar";
                break;
            case 10:
                hardwareType = "Autonet Short Address";
                break;
            case 11:
                hardwareType = "LocalTalk";
                break;
            case 12:
                hardwareType = "LocalNet (IBM PCNet or SYTEK LocalNET)";
                break;
            case 13:
                hardwareType = "Ultra link";
                break;
            case 14:
                hardwareType = "SMDS";
                break;
            case 15:
                hardwareType = "Frame Relay";
                break;
            case 16:
                hardwareType = "Asynchronous Transmission Mode (ATM)";
                break;
            case 17:
                hardwareType = "HDLC";
                break;
            case 18:
                hardwareType = "Fibre Channel";
                break;
            case 19:
                hardwareType = "Asynchronous Transmission Mode (ATM)";
                break;
            case 20:
                hardwareType = "Serial Line";
                break;
            case 21:
                hardwareType = "Asynchronous Transmission Mode (ATM)";
                break;
            case 22:
                hardwareType = "MIL-STD-188-220";
                break;
            case 23:
                hardwareType = "Metricom";
                break;
            case 24:
                hardwareType = "IEEE 1394.1995";
                break;
            case 25:
                hardwareType = "MAPOS";
                break;
            case 26:
                hardwareType = "Twinaxial";
                break;
            case 27:
                hardwareType = "EUI-64";
                break;
            case 28:
                hardwareType = "HIPARP";
                break;
            case 29:
                hardwareType = "IP and ARP over ISO 7816-3";
                break;
            case 30:
                hardwareType = "ARPSec";
                break;
            case 31:
                hardwareType = "IPsec tunnel";
                break;
            case 32:
                hardwareType = "InfiniBand (TM)";
                break;
            case 33:
                hardwareType = "TIA-102 Project 25 Common Air Interface (CAI)";
                break;
            case 34:
                hardwareType = "Wiegand Interface";
                break;
            case 35:
                hardwareType = "Pure IP";
                break;
            case 36:
                hardwareType = "HW_EXP1";
                break;
            case 37:
                hardwareType = "HFI";
                break;
            case 38:
                hardwareType = "Unified Bus (UB)";
                break;
            case 256:
                hardwareType = "HW_EXP2";
                break;
            case 257:
                hardwareType = "AEthernet";
                break;
            default:
                hardwareType = "Unassigned";
                break;
        }

        return hardwareType;
    }
}
