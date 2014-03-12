package com.videoclient.api.conn;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * @author Drem Darios
 *
 */
public class Connection implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger("connection");
    private Socket sock;
    private int port;
    private String ipAddress;
    private ClientStream stream;
    
    /**
     * Creates a new connection to the server.
     * 
     * @param ipAddress - The IP of the server to connect to.
     * @param port - The port to connect to.
     */
    public Connection(String ipAddress, int port)
    {
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    /**
     * Opens a connection between the server at the port indicated.
     * 
     * @throws IOException - If there was an issue opening the connection or
     *         creating the stream.
     */
    public void open() throws IOException
    {
        LOG.info("Opening connection...");
        Socket socket = new Socket(ipAddress, port);
        this.sock = socket;
        //new stream created when connection is opened
        stream = new ClientStream(sock);
    }

    /**
     * Closes the socket and any streams that were opened.
     * 
     * @throws IOException - If there was an issue closing the connection
     */
    public void close() throws IOException
    {
        LOG.info("Closing conection...");
        sock.close();
        stream.closeStream();
    }
    
    /**
     * Gets the client stream for this connection.
     * 
     * @return The client stream for this connection or null if the connection
     *         hasn't been opened yet.
     */
    public ClientStream getStream()
    {
        return stream;
    }
    
    /**
     * Checks to see if a connection has been established.
     * 
     * @return true if successfully connected to the server or false otherwise.
     */
    public boolean isConnected()
    {
        return sock != null && sock.isConnected();
    }
}
