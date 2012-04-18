package com.mattbertolini.camclient.net.urlconnection;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Matt Bertolini
 */
public interface HttpResponse {
    HttpPayload getPayload();
    Map<String, List<String>> getHeaders();
    int getStatusCode();
    String getStatusMessage();
}
