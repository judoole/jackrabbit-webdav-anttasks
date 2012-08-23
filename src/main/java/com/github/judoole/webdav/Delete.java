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
import java.util.Vector;

public class Delete extends Command {
    private String url;
    private boolean verbose = false;
    Vector<FileSet> fileSets = new Vector<FileSet>();
    private HttpClient client;


    /**
     * Set the path of the webdav to which the files are to be uploaded to <br>
     * Example: http://localhost:8080/repository/default
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * For providing a set of input files using Ant's fileset
     *
     * @param fileSet
     */
    public void addFileSet(FileSet fileSet) {
        if (!fileSets.contains(fileSet)) {
            fileSets.add(fileSet);
        }
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * The execute function is called by Ant.
     */
    public void execute() {
        try {
            DirectoryScanner ds;
            client = new HttpClient();
            Credentials creds = new UsernamePasswordCredentials(user, password);
            client.getState().setCredentials(AuthScope.ANY, creds);


            deleteFile(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a directory on the webdav server
     *
     * @param path
     * @param fileName
     */
    private void createDirectory(String path, String fileName) {
        try {
            //Remove the filename at the end
            String directoryPath = path.split(fileName)[0].trim();
            //Build the upload URL
            String uploadUrl = url;
            String[] directories = directoryPath.split(File.separator);

            //If a directory needs to be created
            if (directoryPath.length() > 0) {
                //Recursively create the directory structure
                for (String directoryName : directories) {
                    uploadUrl = uploadUrl + "/" + directoryName;

                    MkColMethod mkdir = new MkColMethod(uploadUrl);
                    int status = client.executeMethod(mkdir);

                    if (status == 405) {/*Directory exists. Do Nothing*/} else if (status != 201)
                        log("ERR " + " " + status + " " + uploadUrl);
                    else if (verbose) log("Directory " + uploadUrl + " created");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //Ignore as there is no directory to be created
        } catch (Exception e) {
            log("ERR creating " + path);
            e.printStackTrace();
        }
    }

    /**
     * Uploads the file. The File object (f) is used for creating a FileInputStream for uploading
     * files to webdav
     *
     * @param filename The relatvie path of the file
     */
    private void deleteFile(String filename) {
        try {
            DeleteMethod method = new DeleteMethod(filename);

            client.executeMethod(method);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}