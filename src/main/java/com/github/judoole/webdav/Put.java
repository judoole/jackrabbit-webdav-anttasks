package com.github.judoole.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

public class Put extends Command {
    private String url;
    private boolean verbose = false;
    Vector<FileSet> fileSets = new Vector<FileSet>();
    private HttpClient client;
    private File file;
    private String filename;

    public void setUrl(String url) {
        this.url = url;
    }

    public void addFileSet(FileSet fileSet) {
        if (!fileSets.contains(fileSet)) {
            fileSets.add(fileSet);
        }
    }

    public void setFile(File f) {
        this.file = f;
    }

    public void setFilename(String name) {
        this.filename = name;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void execute() {
        DirectoryScanner ds;
        client = new HttpClient();
        Credentials creds = new UsernamePasswordCredentials(user, password);
        client.getState().setCredentials(AuthScope.ANY, creds);

        if (file != null) uploadFile(file, filename == null ? file.getName() : filename);

        for (FileSet fileset : fileSets) {
            ds = fileset.getDirectoryScanner(getProject());
            File dir = ds.getBasedir();
            String[] filesInSet = ds.getIncludedFiles();

            for (String filename : filesInSet) {
                if (verbose)
                    log("Processing " + filename);

                File f = new File(dir, filename);
                createDirectory(filename, f.getName());
                uploadFile(f, filename);
            }
        }

    }

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
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            //Ignore as there is no directory to be created
        } catch (Exception e) {
            log("ERR creating " + path);
            throw new RuntimeException("Error creating directory " + path, e);
        }
    }

    /**
     * Uploads the file. The File object (f) is used for creating a FileInputStream for uploading
     * files to webdav
     *
     * @param f        The File object of the file to be uploaded
     * @param filename The relatvie path of the file
     */
    private void uploadFile(File f, String filename) {
        try {
            String uploadUrl = url + "/" + filename;

            PutMethod method = new PutMethod(uploadUrl);
            RequestEntity requestEntity = new InputStreamRequestEntity(new FileInputStream(f));
            method.setRequestEntity(requestEntity);
            client.executeMethod(method);

            //201 Created => No issues
            if (method.getStatusCode() == 204) {
                log(String.format("%s overwritten with %s", uploadUrl, filename));
            } else if (method.getStatusCode() != 201) {
                log("ERR " + " " + method.getStatusCode() + " " + method.getStatusText() + " " + f.getAbsolutePath());
                throw new RuntimeException(String.format("Could not upload %s to %s", filename, uploadUrl));
            } else {
                log(String.format("Transferred %s to %s", filename, uploadUrl));
            }
        } catch (Exception e) {
            log("ERR " + f.getAbsolutePath());
            throw new RuntimeException("Error transferring " + filename, e);
        }
    }

}