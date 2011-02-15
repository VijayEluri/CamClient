package com.mattbertolini.camclient;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mattbertolini.camclient.net.CamConnection;
import com.mattbertolini.camclient.net.CamConnectionException;
import com.mattbertolini.camclient.net.CamConnectionFactory;
import com.mattbertolini.camclient.request.CamRequest;
import com.mattbertolini.camclient.request.Operation;
import com.mattbertolini.camclient.request.RequestParameter;
import com.mattbertolini.camclient.response.CamResponse;

public class CamClient {
    private static final String FOUND_KEY = "found";
    private static final String VERSION_KEY = "version";

    private CamConnection conn;

    public CamClient(CamConnection conn) {
        this.conn = conn;
    }

    public static CamClient connect(String urlString, String username, String password) throws MalformedURLException {
        return new CamClient(CamConnectionFactory.getConnection(urlString, username, password));
    }

    public static CamClient connect(URL url, String username, String password) {
        return new CamClient(CamConnectionFactory.getConnection(url, username, password));
    }

    public static CamClient connect(String urlString, Proxy proxy, String username, String password) throws MalformedURLException {
        return new CamClient(CamConnectionFactory.getConnection(urlString, proxy, username, password));
    }

    public static CamClient connect(URL url, Proxy proxy, String username, String password) {
        return new CamClient(CamConnectionFactory.getConnection(url, proxy, username, password));
    }

    /**
     * Adds a MAC address to the Certified Devices list as an exempted device.
     * 
     * @param macAddress The MAC address to add. Must be in the following 
     * format: <code>01:23:45:67:89:AB</code>
     * @throws CamException If an error occurs making a request to the server.
     * @throws NullPointerException If the MAC address is null.
     */
    public void addCleanMacAddress(String macAddress) throws CamException {
        this.addCleanMacAddress(macAddress, null);
    }
    
    public void addCleanMacAddress(String macAddress, String ssip) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.ADD_CLEAN_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error adding clean MAC address: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    /**
     * Adds a new local user account to the Clean Access Manager.
     * 
     * @param username The user name of the new account.
     * @param password The password of the new account.
     * @param role The user role of the new account.
     * @throws CamException If an error occurred making the request to the 
     * server.
     * @throws NullPointerException If the user name, password, or role 
     * arguments are null.
     */
    public void addLocaluser(String username, String password, String role) throws CamException {
        if(username == null) {
            throw new NullPointerException("Username cannot be null.");
        }
        if(password == null) {
            throw new NullPointerException("Password cannot be null.");
        }
        if(role == null) {
            throw new NullPointerException("Role cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.ADD_LOCAL_USER);
        req.addParameter(RequestParameter.USERNAME, username);
        req.addParameter(RequestParameter.USER_PASSWORD, password);
        req.addParameter(RequestParameter.ROLE_NAME, role);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error adding local user: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Adds the given MAC address to the Device Filters list.
     * 
     * @param macAddress The MAC address to add. Must be in the following 
     * format: <code>01:23:45:67:89:AB</code>
     * @throws CamException If an error occurred making the request to the 
     * server.
     */
    public void addMacAddress(String macAddress) throws CamException {
        this.addMacAddress(macAddress, null, null, null, null, null);
    }

    public void addMacAddress(String macAddress, String ipAddress, Type type, String role, String description, String ssip) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.ADD_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        if(ipAddress != null && !ipAddress.isEmpty()) {
            req.addParameter(RequestParameter.IP_ADDRESS, ipAddress);
        }
        if(type != null) {
            req.addParameter(RequestParameter.TYPE, type.getName());
        }
        if(role != null && type != null && (type == Type.USE_ROLE || type == Type.CHECK)) {
            req.addParameter(RequestParameter.ROLE_NAME, role);
        }
        if(description != null && !description.isEmpty()) {
            req.addParameter(RequestParameter.DESCRIPTION, description);
        }
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error adding MAC address: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    public void addSubnet(String subnet, String mask, Type type, String role, String description, String ssip) throws CamException {
        if(subnet == null) {
            throw new NullPointerException("Subnet cannot be null.");
        }
        if(mask == null) {
            throw new NullPointerException("Mask cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.ADD_SUBNET);
        req.addParameter(RequestParameter.SUBNET, subnet);
        req.addParameter(RequestParameter.SUBNET_MASK, mask);
        if(type != null) {
            req.addParameter(RequestParameter.TYPE, type.getName());
        }
        if(role != null && type != null && type == Type.USE_ROLE) {
            req.addParameter(RequestParameter.ROLE_NAME, role);
        }
        if(description != null && !description.isEmpty()) {
            req.addParameter(RequestParameter.DESCRIPTION, description);
        }
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error adding subnet: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Bounces and out-of-band port in a switch given a switch ID and a port 
     * number.
     * 
     * @param switchId The ID of the switch as inserted in the switches table.
     * @param port The port in the switch to bounce.
     * @throws CamException If an error occurs making the request to the server.
     * @throws NullPointerException If the switch ID is null.
     * @throws IllegalArgumentException If the port number is less than zero.
     */
    public void bouncePort(String switchId, int port) throws CamException {
        if(switchId == null) {
            throw new NullPointerException("Switch ID cannot be null.");
        }
        if(port < 0) {
            throw new IllegalArgumentException("Switch port cannot be less than zero.");
        }
        CamRequest req = new CamRequest(Operation.BOUNCE_PORT);
        req.addParameter(RequestParameter.SWITCH_ID, switchId);
        req.addParameter(RequestParameter.SWITCH_PORT, Integer.toString(port));
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error bouncing port: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    /**
     * Bounces an Out-of-Band port of the switch where the given MAC address is 
     * currently associated.
     * 
     * @param macAddress The MAC address of the connected device to bounce.
     * @throws CamException If an error occurred making the request to the 
     * server.
     */
    public void bouncePortByMacAddress(String macAddress) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.BOUNCE_PORT_BY_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error bouncing port by mac address: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    public void changeLoggedInUserRole(String ipAddress, String role) throws CamException {
        if(ipAddress == null) {
            throw new NullPointerException("IP address cannot be null.");
        }
        if(role == null) {
            throw new NullPointerException("Role cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.CHANGE_LOGGED_IN_USER_ROLE);
        req.addParameter(RequestParameter.IP_ADDRESS, ipAddress);
        req.addParameter(RequestParameter.ROLE_NAME, role);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error changing user role: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    public void changeUserRole(String ipAddress, String role) throws CamException {
        if(ipAddress == null) {
            throw new NullPointerException("IP address cannot be null.");
        }
        if(role == null) {
            throw new NullPointerException("Role cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.CHANGE_USER_ROLE);
        req.addParameter(RequestParameter.IP_ADDRESS, ipAddress);
        req.addParameter(RequestParameter.ROLE_NAME, role);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error changing user role: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Checks the Device Filters list to see if the given MAC address exists. 
     * If the MAC address is found, collects device information from the 
     * filters list and places it in a CamDevice object.
     * 
     * @param macAddress The MAC address to search for. Must match the display 
     * format <code>01:23:45:67:89:AB</code>.
     * @return If the MAC is found, the method returns a CamDevice object. If 
     * no device is found, null is returned. 
     * @throws CamException If an error occurred making the request to the 
     * server.
     * @throws NullPointerException If the given MAC address is null.
     */
    public CamDevice checkMacAddress(String macAddress) throws CamException {
        return this.checkMacAddress(macAddress, null);
    }
    
    /**
     * Checks the Device Filters list to see if the given MAC address exists. 
     * If the MAC address is found, collects device information from the 
     * filters list and places it in a CamDevice object.
     * 
     * @param macAddress The MAC address to search for. Must match the display 
     * format <code>01:23:45:67:89:AB</code>.
     * @param ssip The Clean Access Server IP address.
     * @return If the MAC is found, the method returns a CamDevice object. If 
     * no device is found, null is returned. 
     * @throws CamException If an error occurred making the request to the 
     * server.
     * @throws NullPointerException If the given MAC address is null.
     */
    public CamDevice checkMacAddress(String macAddress, String ssip) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.CHECK_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        CamDevice retVal = null;
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error checking MAC address: " + resp.getErrorText());
            }
            List<Map<String, String>> respData = resp.getResponseData();
            boolean isFound = Boolean.parseBoolean(respData.get(0).get(FOUND_KEY));
            if(isFound) {
                retVal = CamDeviceParser.parseMap(respData.get(1));
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
        return retVal;
    }

    /**
     * Deletes all entries in the Certified Devices list.
     * 
     * @throws CamException If an error occurs when making the request to the 
     * server.
     */
    public void clearCertifiedList() throws CamException {
        CamRequest req = new CamRequest(Operation.CLEAR_CERTIFIED_LIST);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error clearing certified list: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    public void deleteAllLocalUsers() throws CamException {
        CamRequest req = new CamRequest(Operation.DELETE_LOCAL_USER);
        req.addParameter(RequestParameter.QUERY_TYPE, "all");
        req.addParameter(RequestParameter.QUERY_VALUE, "");
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error deleting all local users: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    public void deleteLocalUser(String username) throws CamException {
        if(username == null) {
            throw new NullPointerException("Username cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.DELETE_LOCAL_USER);
        req.addParameter(RequestParameter.QUERY_TYPE, "name");
        req.addParameter(RequestParameter.QUERY_VALUE, username);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error deleting local user: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Gets the version number of the CAM.
     * 
     * @return A String containing the version number.
     * @throws CamException If an error occurred making the request to the 
     * server.
     */
    public String getCamVersion() throws CamException {
        String retVal = null;
        CamRequest req = new CamRequest(Operation.GET_VERSION);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error getting version: " + resp.getErrorText());
            }
            retVal = resp.getResponseData().get(0).get(VERSION_KEY);
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
        return retVal;
    }

    /**
     * Gets the list of local user accounts currently in the CAM.
     * 
     * @return A list of CamLocalUser objects containing user name and role 
     * name.
     * @throws CamException If an error occurs making a request to the server.
     */
    public List<CamLocalUser> getLocalUserList() throws CamException {
        CamRequest req = new CamRequest(Operation.GET_LOCAL_USER_LIST);
        List<CamLocalUser> retList = new ArrayList<CamLocalUser>();
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error retieving local user list: " + resp.getErrorText());
            }
            // TODO: Break this code out to separate method/class.
            List<Map<String, String>> respData = resp.getResponseData();
            for(Map<String, String> row : respData) {
                String name = row.get("name");
                String role = row.get("role");
                CamLocalUser user = new CamLocalUser(name, role);
                retList.add(user);
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
        return retList;
    }

    /**
     * Retrieves the entire Device Filters list.
     * 
     * @return An ArrayList containing CamDevice objects representing each 
     * record in the Device Filters list. If no devices are found, an empty 
     * list is returned.
     * @throws CamException If an error occurs when making the request to the 
     * server.
     */
    public List<CamDevice> getMacAddressList() throws CamException {
        CamRequest req = new CamRequest(Operation.GET_MAC_ADDRESS_LIST);
        List<CamDevice> retList = new ArrayList<CamDevice>();
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error getting MAC address list: " + resp.getErrorText());
            }
            retList = CamDeviceParser.parseList(resp.getResponseData());
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
        return retList;
    }

    public String getUserAgent() {
        return this.conn.getUserAgent();
    }

    /**
     * Ends the active session of the out-of-band user with the given MAC 
     * address and removes the user from the Out-of-Band Online Users list.
     * 
     * @param macAddress The MAC address of the out-of-band user to kick. Must 
     * be in the following format: <code>01:23:45:67:89:AB</code>
     * @throws CamException If an error occurred making the request to the 
     * server.
     */
    public void kickOutOfBandUser(String macAddress) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.KICK_OOB_USER);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error kicking OOB user: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    public void kickUser(String ipAddress) throws CamException {
        if(ipAddress == null) {
            throw new NullPointerException("IP address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.KICK_USER);
        req.addParameter(RequestParameter.IP_ADDRESS, ipAddress);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error kicking user: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    public void kickUserByMacAddress(String macAddress) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.KICK_USER_BY_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error kicking user: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    /**
     * Removes the given MAC address from the Certified Devices list.
     * 
     * @param macAddress The MAC address to remove. Must be in the following 
     * format: <code>01:23:45:67:89:AB</code>.
     * @throws CamException If an error occurs making the request to the server.
     * @throws NullPointerException If the MAC address is null.
     */
    public void removeCleanMacAddress(String macAddress) throws CamException {
        this.removeCleanMacAddress(macAddress, null);
    }
    
    public void removeCleanMacAddress(String macAddress, String ssip) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.REMOVE_CLEAN_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error removing clean MAC address: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Removes the given MAC address from the Device Filters list.
     * 
     * @param macAddress The MAC address to remove. Must be in the following 
     * format: <code>01:23:45:67:89:AB</code>
     * @throws CamException If an error occurred making the request to the 
     * server.
     */
    public void removeMacAddress(String macAddress) throws CamException {
        this.removeMacAddress(macAddress, null);
    }

    public void removeMacAddress(String macAddress, String ssip) throws CamException {
        if(macAddress == null) {
            throw new NullPointerException("MAC address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.REMOVE_MAC_ADDRESS);
        req.addParameter(RequestParameter.MAC_ADDRESS, macAddress);
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error removing MAC address: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    /**
     * Deletes all entries from the Device Filters list.
     * 
     * @throws CamException If an error occurs when making the request to the 
     * server.
     */
    public void removeMacAddressList() throws CamException {
        CamRequest req = new CamRequest(Operation.REMOVE_MAC_ADDRESS_LIST);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error removing MAC address list: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }

    public void removeSubnet(String subnet, String mask, String ssip) throws CamException {
        if(subnet == null) {
            throw new NullPointerException("Subnet cannot be null.");
        }
        if(mask == null) {
            throw new NullPointerException("Mask cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.REMOVE_SUBNET);
        req.addParameter(RequestParameter.SUBNET, subnet);
        req.addParameter(RequestParameter.SUBNET_MASK, mask);
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error removing subnet: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Renews the session timeout of the user with the given IP address by one 
     * session.
     * 
     * @param ipAddress The IP address of the user to renew the session.
     * @throws CamException If an error occurred making the request to the 
     * server.
     */
    public void renewUserSessionTime(String ipAddress) throws CamException {
        if(ipAddress == null) {
            throw new NullPointerException("IP address cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.RENEW_USER_SESSION_TIME);
        req.addParameter(RequestParameter.LIST, ipAddress);
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error renewing user session time: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
    
    /**
     * Set the user agent string that will be sent to the CAM with every 
     * request.
     * 
     * @param userAgent The string to set for the user agent.
     * @throws NullPointerException If the string is null.
     */
    public void setUserAgent(final String userAgent) {
        this.conn.setUserAgent(userAgent);
    }

    public void updateSubnet(String subnet, String mask, Type type, String role, String description, String ssip) throws CamException {
        if(subnet == null) {
            throw new NullPointerException("Subnet cannot be null.");
        }
        if(mask == null) {
            throw new NullPointerException("Mask cannot be null.");
        }
        CamRequest req = new CamRequest(Operation.UPDATE_SUBNET);
        req.addParameter(RequestParameter.SUBNET, subnet);
        req.addParameter(RequestParameter.SUBNET_MASK, mask);
        if(type != null) {
            req.addParameter(RequestParameter.TYPE, type.getName());
        }
        if(role != null && type != null && type == Type.USE_ROLE) {
            req.addParameter(RequestParameter.ROLE_NAME, role);
        }
        if(description != null && !description.isEmpty()) {
            req.addParameter(RequestParameter.DESCRIPTION, description);
        }
        if(ssip != null && !ssip.isEmpty()) {
            req.addParameter(RequestParameter.SERVER_IP_ADDRESS, ssip);
        }
        try {
            CamResponse resp = this.conn.submitRequest(req);
            if(resp.isError()) {
                throw new CamException("Error updating subnet: " + resp.getErrorText());
            }
        } catch(CamConnectionException e) {
            throw new CamException(e);
        }
    }
}
