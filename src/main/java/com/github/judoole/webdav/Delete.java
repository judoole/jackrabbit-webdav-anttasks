package com.github.judoole.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Delete extends Command {
    private String url;
    private boolean verbose = false;
    Vector<FileSet> fileSets = new Vector<FileSet>();
    private HttpClient client;


    public void setUrl(String url) {
        this.url = url;
    }

    public void addFileSet(FileSet fileSet) {
        if (!fileSets.contains(fileSet)) {
            fileSets.add(fileSet);
        }
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void execute() throws IOException {
        DirectoryScanner ds;
        client = new HttpClient();
        Credentials creds = new UsernamePasswordCredentials(user, password);
        client.getState().setCredentials(AuthScope.ANY, creds);


        deleteFile(url);
    }

    private void deleteFile(String filename) throws IOException {
        DeleteMethod method = new DeleteMethod(filename);

        client.executeMethod(method);
        log("Deleted file "+filename);
    }
}