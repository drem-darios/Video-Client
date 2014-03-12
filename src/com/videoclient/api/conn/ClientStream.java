package com.videoclient.api.conn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Drem Darios
 *
 */
public class ClientStream
{

    private static final Logger LOG = Logger.getLogger("stream");
    private OutputStreamWriter output;
    private InputStreamReader input;
    private Socket sock;
    private String reply = "";
    
    /**
     * Opens a new stream between the client and the server
     * 
     * @param socket - The socket to retrieve the output stream from
     * @throws IOException - if an I/O error occurs when creating the output 
     *         stream or if the socket is not connected.
     */
    public ClientStream(Socket socket) throws IOException
    {
        this.sock = socket;
        openStream();
    }
    
    /**
     * Closes Input/Output buffer streams
     * @throws IOException
     */
    public void closeStream() throws IOException
    {
        LOG.info("Closing stream...");
        output.close();
        input.close();
    }
    /**
     * Writes a message to the server in UTF-8 encoded bytes
     * @param message - The message to send
     * @throws IOException
     */
    public void write(String message) throws IOException
    {
        LOG.info("Writing message...");
        openStream();
        output.write(message);
        output.flush();
        read();
    }
    
    /**
     * Returns the last reply from the server or blank if no request has been made
     * 
     */
    public String getReply()
    {
        return reply;
    }
    
    /**
     * Opens Input/Output buffer streams
     * @throws IOException
     */
    private void openStream() throws IOException
    {
        LOG.info("Opening stream...");
        
        OutputStream rawOut = new BufferedOutputStream(sock.getOutputStream());
        InputStream rawIn = new BufferedInputStream(sock.getInputStream());
        
        this.output = new OutputStreamWriter(rawOut, "UTF-8");
        this.input = new InputStreamReader(rawIn, "UTF-8");
        
        output.flush();
    }
    
    /**
     * Reads the reply back from the server and sets it for future reference
     */
    private void read() throws IOException
    {
        LOG.info("Reading message...");
        int data = -1;
        StringBuffer reply = new StringBuffer();
        
        while(true)
        {
            data = input.read();
            //connection was closed
            if(data == -1)
            {
               break; 
            }
            reply.append((char)data);
            
            //reply is at least 2 chars long
            if (reply.length() > 1)
            {
                //check if this and previous char are 59 (";;")
                if(reply.codePointAt(reply.length() - 2) == ';' 
                        && data == ';')
                {
                    break;
                }
            }
        }
        
        this.reply = reply.toString();
    }
}
