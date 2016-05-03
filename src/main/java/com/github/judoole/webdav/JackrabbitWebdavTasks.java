package com.github.judoole.webdav;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JackrabbitWebdavTasks extends Task {
    private List<Command> commands = new ArrayList<Command>();
    private String username = null;
    private String password = null;
	private static final int MAX_PASSWORD_LENGTH = 256;

    public void addPut(Put put) {
        put.setPassword(password);
        put.setUser(username);
        this.commands.add(put);
    }

    public void addDelete(Delete delete) {
        delete.setPassword(password);
        delete.setUser(username);
        this.commands.add(delete);
    }

    public void addGet(Get get) {
        get.setPassword(password);
        get.setUser(username);
        this.commands.add(get);
    }

    public void addCopy(Copy copy) {
        copy.setPassword(password);
        copy.setUser(username);
        this.commands.add(copy);
    }

    @Override
    public void execute() {
        try {
            for (Command command : this.commands) {
                command.execute();
            }
        } catch (Exception e) {
            log("Command did not execute properly!", e, 1);
            throw new BuildException(e);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Set WebDAV password from file. For the password to be recognized,
	 * the file must not contain a line-break
	 * 
	 * @param passwordFile
	 *            Name of file from which to read password
	 * @throws FileNotFoundException,
	 *             IOException
	 */
	public void setPasswordFile(String passwordFile) throws FileNotFoundException, IOException {
		File pwdFile = new File(passwordFile);
		if (!pwdFile.exists()) {
			throw new FileNotFoundException("File '" + passwordFile + "' does not exist");
		}
		if (!pwdFile.canRead()) {
			throw new IOException("File '" + passwordFile + "' cannot be read");
		}
		int fileSize = (int) pwdFile.length();
		if (fileSize == 0) {
			throw new IOException("File '" + passwordFile + "' is empty");
		}
		if (fileSize > MAX_PASSWORD_LENGTH) {
			throw new IOException("File '" + passwordFile + "' has a size of "
					+ fileSize + " bytes (max. " + MAX_PASSWORD_LENGTH
					+ " bytes allowed)");
		}
		char[] cbuf = new char[fileSize];
		FileReader fr = new FileReader(pwdFile);
		fr.read(cbuf);
		fr.close();
		this.password = new String(cbuf);
	}
}

