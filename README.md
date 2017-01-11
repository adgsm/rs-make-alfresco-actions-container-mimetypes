# Alfresco Container contained mime-types Action AMP, setting/updating container tags to reflect all contained content mime-types

Setting/updating container tags to reflect all contained content mime-types

### Usage

#### Create AMP
```
mvn clean install
```
#### Install AMP
```
/opt/alfresco/bin/apply_amps.sh
```
or
```
java -jar /opt/alfresco/bin/alfresco-mmt.jar install rs-make-alfresco-actions-container-mimetypes /opt/alfresco/tomcat/webapps/alfresco.war
```

### License
Licensed under the MIT license.
http://www.opensource.org/licenses/mit-license.php
