public class BinUtils {

    static long uint32LEFromFile(byte[] data, int offset) {
        long value = ((long)(data[offset + 3] & 0xff) << 24);
        value += ((data[offset + 2] & 0xff) << 16);
        value += ((data[offset + 1] & 0xff) << 8);
        value += ((data[offset] & 0xff));

        return value;
    }

    static long uint32BEFromFile(byte[] data, int offset) {
        long value = ((long)(data[offset] & 0xff) << 24);
        value += ((data[offset + 1] & 0xff) << 16);
        value += ((data[offset + 2] & 0xff) << 8);
        value += ((data[offset + 3] & 0xff));

        return value;
    }

    static int uint16LEFromFile(byte[] data, int offset) {
        int value = ((int)(data[offset + 1] & 0xff) << 8);
        value += ((data[offset] & 0xff));

        return value;
    }

    static int uint16BEFromFile(byte[] data, int offset) {
        int value = ((int)(data[offset] & 0xff) << 8);
        value += ((data[offset + 1] & 0xff));

        return value;
    }

    static int strLenFromFile(byte[] data, int offset) {
        int strLen = 0;
        while (data[offset + strLen] != '\0') ++strLen;
        return strLen;
    }

    static String bytesToString(byte[] data) {
        return bytesToString(data, 0);
    }

    static String bytesToString(byte[] data, int linesize) {
        return bytesToString(data, linesize, 0);
    }

    static String bytesToString(byte[] data, int linesize, int maxLines) {
        String rep = "";
        int lines = 0;

        for (int i = 0; i < data.length ; ++i) {
            if ((linesize > 0) && (i > 0) && ((i % linesize) == 0)) {
                rep += "\n";
                ++lines;
            }
            if ((maxLines > 0) && (lines > maxLines)) {
                rep += "...";
                break;
            }
            rep += String.format("%02x ", data[i]);
        }

        return rep;
    }
}
