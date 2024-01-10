import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

enum HTTPMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTION,
    TRACE,
    PATCH
}

public class HTTP extends Protocol {
    private String httpMessageType;

    private HTTPMethod method;
    private String version = "";
    private int statusCode;
    private String statusMessage;
    private String uri;

    private Hashtable<String, String> fields = new Hashtable<String, String>();

    public HTTP(byte[] data) throws DataFormatException {
        super(data);

        name = "HTTP";
        try {
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
            
            parseHTTPHeader(input.readLine());

            while (((line = input.readLine()) != null) && !(line).equals("")) {
                parseHTTPField(line);
            }
            input.close();
        } catch (Exception e) {
            throw new DataFormatException("Not a HTTP Message");
        }
    }

    @Override
    public String toString() {
        String httpStr = "[ " + httpMessageType + ": " + version + " ]\n";
        
        if (method != null) {
            httpStr += "  Method: " + method + "\n";
            httpStr += "  URI -> " + uri + "\n";
        } else {
            httpStr += "  Status: " + statusCode + " " + statusMessage + "\n";
        }

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            httpStr += String.format("  %s: %s\n", entry.getKey(), entry.getValue());
        }

        return httpStr;
    }

    private void parseHTTPHeader(String header) throws DataFormatException {
        if (header == null) throw new DataFormatException("Invalid HTTP Header !");

        String[] headerTokens = header.split(" ", 3);
        int tokenIndex = 0;

        if (headerTokens.length != 3) throw new DataFormatException("Invalid HTTP Header !");

        // Method switch
        switch (headerTokens[tokenIndex]) {
            case "GET":
                method = HTTPMethod.GET;
                break;
            case "HEAD":
                method = HTTPMethod.HEAD;
                break;
            case "POST":
                method = HTTPMethod.POST;
                break;
            case "PUT":
                method = HTTPMethod.PUT;
                break;
            case "DELETE":
                method = HTTPMethod.DELETE;
                break;
            case "CONNECT":
                method = HTTPMethod.CONNECT;
                break;
            case "OPTION":
                method = HTTPMethod.OPTION;
                break;
            case "TRACE":
                method = HTTPMethod.TRACE;
                break;
            case "PATCH":
                method = HTTPMethod.PATCH;
                break;
            default:
                break;
        }

        // Request
        if (method != null) {
            httpMessageType = "HTTP Request";
            uri = headerTokens[1];
            version = headerTokens[2];
        } else { // Response
            httpMessageType = "HTTP Response";
            version = headerTokens[0];
            statusCode = Integer.parseInt(headerTokens[1]);
            statusMessage = headerTokens[2];
        }
    }

    private void parseHTTPField(String field) throws DataFormatException { // Parse the required fields of the http message header
        StringTokenizer tokens = new StringTokenizer(field, ":");

        if (tokens.countTokens() >= 2) {
            String key = tokens.nextToken();
            String data = tokens.nextToken().trim();

            fields.put(key, data);
        }
    }
}