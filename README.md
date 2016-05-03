# Jackrabbit on Ant

Ant tasks for Jackrabbit against webdav.

Could not find any so I created an ant task lib looking at [Sardine](https://code.google.com/p/sardine/) and [AntDav](http://code.google.com/p/antdav/) for copy-paste.

## Usage
```xml
    <!-- Webdav taskdefinitions -->
    <typedef resource="com/github/judoole/webdav/jackrabbit-webdav-tasks.xml">
        <classpath>
            <pathelement location="${lib.loc}/jackrabbit-webdav-anttasks-0.4.jar"/>
            <pathelement location="${lib.loc}/commons-httpclient-3.1.jar"/>
            <pathelement location="${lib.loc}/commons-codec-1.2.jar"/>
            <pathelement location="${lib.loc}/jackrabbit-webdav-2.5.1.jar"/>
            <pathelement location="${lib.loc}/slf4j-api-1.6.4.jar"/>
            <pathelement location="${lib.loc}/commons-logging-api-1.1.jar"/>
        </classpath>
    </typedef>
    <target name="davthis">
        <jackrabbit username="superuser" password="hiddenPassword">
            <delete url="http://url-to-file-to-delete"/>        
            <get url="http://url-to-file-to-get-without-filename" file="remote-filename" fileOut="local-filename" />        
            <put url="http://url-to-file-to-folder">
                <fileset dir="." includes="**/file-to-transfer-up.txt"/>
            </put>
        </jackrabbit>
        <jackrabbit username="superuser" passwordFile="pathAndNameOfFileWithPassword">
            <delete url="http://url-to-file-to-delete"/>        
            <get url="http://url-to-file-to-get-without-filename" file="remote-filename" fileOut="local-filename" />        
            <put url="http://url-to-file-to-folder">
                <fileset dir="." includes="**/file-to-transfer-up.txt"/>
            </put>
        </jackrabbit>
    </target>
```
