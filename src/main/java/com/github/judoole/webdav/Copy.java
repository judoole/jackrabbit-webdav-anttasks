package com.github.judoole.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.jackrabbit.webdav.client.methods.CopyMethod;

import java.io.*;

public class Copy extends Command {
    private String source;
    private String destination;
    private boolean verbose = false;
    private boolean overwrite = true;

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void execute() throws IOException {
        HttpClient client = new HttpClient();
        Credentials creds = new UsernamePasswordCredentials(user, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
        long startTime = System.currentTimeMillis();

        CopyMethod method = new CopyMethod(source, destination, overwrite);

        if (verbose) {
            log(String.format("Copying %s to %s", source, destination));
        }

        int httpResponse = client.executeMethod(method);
        if (httpResponse != 201 && httpResponse != 200 && httpResponse != 204) {
            throw new RuntimeException(String.format("HttpResponse code for copy %s to %s returned %s instead of 200, 201 or 204", source, destination, httpResponse));
        }
        log(String.format("Copied %s to %s", source, destination));


        if (verbose) {
            long endTime = System.currentTimeMillis();
            long elapsed = ((endTime - startTime) / 1000);
            log(source + " took " + elapsed + " seconds to complete");
        }

    }

}