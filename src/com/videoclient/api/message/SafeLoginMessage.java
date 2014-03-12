package com.videoclient.api.message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Drem Darios
 *
 */
public class SafeLoginMessage extends LoginMessage
{
    /**
     * The nonce sent from the server
     */
    private String nonce;
    /**
     * Hex values used to convert byte to hex
     */
    static final String HEXES = "0123456789ABCDEF";
    /**
     * The algorithm to use;
     */
    private static final String ALGO = "MD5";
    /**
     * The username
     */
    private String username;
    /**
     * The password
     */
    private String password;
    
    /**
     * Takes in a nonce to use in encrypting the password to send it to the server.
     * 
     */
    public SafeLoginMessage(String username, String password, String nonce)
    {
        super(username, password);
        this.nonce = nonce;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String getMessage()
    {
        
        StringBuffer message = new StringBuffer("safelogin;");
        message.append("id:");
        message.append(username + ";");
        message.append("hash:");
        //Format: safelogin;id:UserId;hash:HexOfMD5Hash(password+nonce);;
        String hash = calculateHash();
        message.append( hash + ";;");
        
        return message.toString();

    }


    private static String getHex(byte [] raw) 
    {
        if (raw == null) 
        {
            return null;
        }
        
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for(final byte b : raw) 
        {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
            .append(HEXES.charAt((b & 0x0F)));
        }
        
        return hex.toString();
    }
    
    
    private String calculateHash()
    {
        String combinedPassword = password + nonce;
        String hexString = null;
        MessageDigest md5;
        try
        {
            md5 = MessageDigest.getInstance(ALGO);
            byte[] md5Hash = md5.digest(combinedPassword.getBytes());
            
            hexString = getHex(md5Hash);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return hexString;
    }

}
