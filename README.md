# Jackrabbit on Ant

Ant tasks for Jackrabbit against webdab.

Could not find any so I created an ant task lib looking at [Sardine](https://code.google.com/p/sardine/) and [AntDav](http://code.google.com/p/antdav/) for copy-paste.

## Usage
```xml
    <!-- Webdav taskdefinitions -->
    <typedef resource="com/github/judoole/webdav/jackrabbit-webdav-tasks.xml">
        <classpath>
            <pathelement location="${lib.loc}/jackrabbit-webdav-anttasks-0.1.jar"/>
        </classpath>
        <classpath>
            <pathelement location="${lib.loc}/commons-httpclient-3.1.jar"/>
        </classpath>
        <classpath>
            <pathelement location="${lib.loc}/commons-codec-1.4.jar"/>
        </classpath>
        <classpath>
            <pathelement location="${lib.loc}/jackrabbit-webdav-2.5.1.jar"/>
        </classpath>
        <classpath>
            <pathelement location="${lib.loc}/slf4j-api-1.6.4.jar"/>
        </classpath>
        <classpath>
            <pathelement location="${lib.loc}/commons-logging-1.1.1.jar"/>
        </classpath>
    </typedef>
    <target name="davthis">
        <jackrabbit username="superuser" password="hiddenPassword">
            <delete url="http://url-to-file-to-delete"/>
        </jackrabbit>
        <jackrabbit username="superuser" password="hiddenPassword">
            <get url="http://url-to-file-to-get"/>
        </jackrabbit>
        <jackrabbit username="superuser" password="hiddenPassword">
            <put url="http://url-to-file-to-folder">
                <fileset dir="." includes="**/file-to-transfer-up.txt"/>
            </put>
        </jackrabbit>
    </target>
```