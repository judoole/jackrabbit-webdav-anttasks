package com.github.judoole.webdav;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.util.ArrayList;
import java.util.List;

public class JackrabbitWebdavTasks extends Task {
    private List<Command> commands = new ArrayList<Command>();
    private String username = null;
    private String password = null;

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

}

