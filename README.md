# Welcome

If you have arrived here expecting to be able to draw some glycan structures and perhaps export them as an image or text encoding please go direct to the [live demo](http://unicarbkb.org/builder). Alternatively if you are looking for information on the original Swing/Applet versions of GlycanBuilder or the core library that underpins all versions of GlycanBuilder please see the original [Google Code Site](http://code.google.com/p/glycanbuilder) GlycanBuilderV updates will now be committed to this separate repository but the core library and Swing/Applet versions will continue to be maintained on the Google Code Site. 

# How to cite

If you are using GlycanBuilder to aid your research or to prepare figures for inclusion in publications please cite the following papers (a new one covering GlycanBuilderV should hopefully be published shortly).
<pre>Ceroni A, Dell A, Haslam SM.<br/> The GlycanBuilder: a fast, intuitive and flexible software tool for building and displaying glycan structures.<br/> Source Code Biol Med. 2007 Aug 7;2:3. PubMed PMID: 17683623; PubMed Central PMCID: PMC1994674.</pre>
<pre>Damerell D, Ceroni A, Maass K, Ranzinger R, Dell A, Haslam SM.<br/> The GlycanBuilder and GlycoWorkbench glycoinformatics tools: updates and new developments.<br/> Biol Chem. 2012 Nov;393(11):1357-62. PubMed PMID: 23109548.</pre>

The latest version of GlycanBuilder is compatiable with Vaadin7. This work was supported by the Australian NeCTAR project. To recognise this work please cite UniCarbKB:
<pre>Campbell MP, Peterson R, Mariethoz J, Gasteiger E, Akune Y, Aoki-Kinoshita KF, Lisacek F, Packer NH.<br/> UniCarbKB: building a knowledge platform for glycoproteomics.<br/> Nucleic Acids Res. 2014 Jan 1;42(1):D215-21. PubMed PMID: 24234447.</pre>

To help us secure future funding we would be grateful if you could provide a link to this GitHub page and provide URL links to the above references.

# Further Information

Vaadin is a web development framework that functions on top of the Google Web Toolkit (GWT). The GWT allows you to write web applications completely in Java: this includes both the client and server side components. GWT includes a compiler that takes the Java programmed client side and turns this into a JavaScript library: that the browser is asked to load. Vaadin takes GWT one step further, almost allowing the developer to forget that their is a separation between the client and server side components: Vaadin development is very similar to Swing and SWT development. The Vaadin glycan builder application draws glycan structures to an HTML5 canvas element. This component can be embedded in existing web pages (see below). Alternatively you can create new Vaadin based web sites and use the Vaadin glycan builder component as a native component. In addition you can use the component that draws glycan structures outside of the builder application (i.e. you could use a series of canvas elements to show a list of glycan structures). We had to modify the Vaadin component that talks to the native browser HTML5 canvas component: see the following URL for this new component [CanvasWidgetICL](https://bitbucket.org/daviddamerell/canvaswidgeticl).

# Developers

This project represents a port of the popular GlycanBuilder software tool from the native Java Swing/Applet environment to the Vaadin RIA platform. The two main advantages of this port are that it can be run in a web browser without any plug-ins, and providers a UI experience that doesn't break the visual flow of a web page in the same way that many Applets do.

GlycanBuilderV shares a common code base with both the standalone and Applet versions of the GlycanBuilder software tool, and was until recently present in the GlycanBuilder Google Code site](http://code.google.com/p/glycanbuilder) under the sub directory [VaadinGlycanBuilder](http://code.google.com/p/glycanbuilder/source/browse/#hg%2FVaadinGlycanBuilder).

## Code checkout and set up

If you would like to make changes to GlycanBuilderV simply checkout the code and follow the instructions below.
<pre>
git clone https://github.com/IntersectAustralia/GlycanBuilderVaadin7Version.git
cd GlycanBuilderVaadin7Version/glycanbuilderv
</pre>

##Setting up Eclipse

1. If you haven't already done so please download [eclipse](http://eclipse.org/downloads) and make sure you have a suitable [JDK (must support Java 7)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) in your path and that JAVA_HOME is set up correctly.

2. Start Eclipse and click on Help->Install new software->Add, and enter "Vaadin" in name box and "http://vaadin.com/eclipse" in the location box.
3. Select all the Vaadin components listed and follow the on-screen instructions.
4. Now import the GlycanBuilderV project by clicking on File->Import->Existing Projects into Workspace.
5. Select the folder containing the code you cloned earlier. 
6. Expand Java Resources -> Libraries right click on ivy.xml [default], ivy.xml [nodeploy], ivy.xml[widgetset-compile] and on each select Resolve and then Refresh

To run GlycanBuilderV from within Eclipse make sure you have a Servlet container set up (recommend Tomcat 7.0.47 or newer in 7 branch) in Eclipse (File->New->Server). Once this is done simply Right-Click on the GlycanBuilderV project and click "Run as"->"Run on server" and select the server you just set up. To see the running web application, open up your browser of choice and navigate to "http://localhost:8080/GlycanBuilder".

## Integrating GlycabBuilderV into an existing web page (http://unicarbkb.org/builder#)

The following is a brief set of instructions to install this component and embed it in another web page. Note that we ask Apache to proxy access to Tomcat: this is important as modern web browsers will refuse to load an iframe unless both the hostname and port match that of the host web page (that you are embedding glycan builder into).

Start by putting the following web page somewhere in your web server served directory.
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="width:100%;height:100%;border:0;margin:0;">
<head>
<title>Vaadin embedded GlycanBuilder applet</title>
</head>
<body>
        <script type="text/javascript" src="/GlycanBuilder/VAADIN/vaadinBootstrap.js"></script>

        <iframe id="__gwt_historyFrame" style="width:0;height:0;border:0;overflow:hidden" src="javascript:false"></iframe>

        <p>Vaadin embedded GlycanBuilder applet</p>

<div style="height:800px; border: 2px solid red;" id="fb" class="v-app">
   <!-- Optional placeholder for the loading indicator -->
    <div class=" v-app-loading"></div>

    <!-- Alternative fallback text -->
    <noscript>You have to enable javascript in your browser to
              use an application built with Vaadin.</noscript>
  </div>
<script type="text/javascript">//<![CDATA[
    if (!window.vaadin)
        alert("Failed to load the bootstrap JavaScript: VAADIN/vaadinBootstrap.js");

    /* The UI Configuration */
    vaadin.initApplication("fb", {
        "browserDetailsUrl": "GlycanBuilder/",
        "serviceUrl": "GlycanBuilder/",
        "widgetset": "ac.uk.icl.dell.vaadin.glycanbuilder.widgetset.GlycanbuilderWidgetset",
        "theme": "ucdb_2011theme",
        "versionInfo": {"vaadinVersion": "7.0.0"},
        "vaadinDir": "/GlycanBuilder/VAADIN/",
        "heartbeatInterval": 300,
        "debug": false,
        "standalone": false,
        "authErrMsg": {
            "message": "Take note of any unsaved data, "+
                      "and <u>click here<\/u> to continue.",
            "caption": "Authentication problem"
        },
        "comErrMsg": {
            "message": "Take note of any unsaved data, "+
                       "and <u>click here<\/u> to continue.",
            "caption": "Communication problem"
        },
        "sessExpMsg": {
            "message": "Take note of any unsaved data, "+
                       "and <u>click here<\/u> to continue.",
            "caption": "Session Expired"
        }
    });
  </script>
        <script type="text/javascript">
        var callBack=[];
        callBack.run=function(response){alert(response);}
        </script>
        <form>
        <input type="button" name="Search" value="Search" onclick='exportCanvas("glycoct_condensed","callBack");'/>
        </form>

        </body>
</html>
```

Now you can either build or download war file. 
To build war file simply right click on project in eclipse (GlycanBuilder) and select Export -> WAR File.
If you prefer to download it you can get it from [here](https://github.com/IntersectAustralia/GlycanBuilderVaadin7Version/blob/master/glycanbuilderv/downloads/GlycanBuilder12_11_2013.war)

Once you have a war file place it into the webapps directory of tomcat. Rename the download GlycanBuilder.war

Finally setup Apache to proxy access to tomcat (hopefully it's obvious what settings you need to adjust).
```
<VirtualHost *:80>
        ServerAdmin webmaster@localhost
        ServerName glycodb.nixbioinf.org

        DocumentRoot /opt/server/glycodb.nixbioinf.org/

        <Directory /opt/server/glycodb.nixbioinf.org>
                Options Indexes FollowSymLinks MultiViews
                AllowOverride All
                Order deny,allow
                allow from all 
        </Directory>

        RewriteEngine On

        RewriteRule ^/GlycanBuilder/(.*)$ /tomcat/GlycanBuilder/$1 [P]

        ProxyRequests Off

        ProxyPass /tomcat/ http://localhost:8080/
        ProxyPassReverse /tomcat/ http://localhost:8080/

        ProxyPreserveHost On

        HostnameLookups on

        ErrorLog /var/log/apache2/ucdb_2011.error.log

        LogLevel warn

        CustomLog /var/log/apache2/ucdb_2011.access.log combined
</VirtualHost>
```


After you have started both Apache and TomCat you should be able to see glycan builder in action by navigating to the web page you created above. You can move glycan builder around in a web page by moving the div element where the id equals fb. Hopefully you can also see how the submit button in the above example used a call back to export the canvas in GlycoCT condensed format.

##Funding
GlycanBuilder v1.0 was supported by the EUROCarbDB project (Anne Dell and Stuart Haslam).
GlycanBuilder v2.0 was developed at Imperial College with funding from the BBSRC. 
Updates to the latest version of Vaadin were made by Macquarie University via The National eResearch Collaboration Tools and Resources (NeCTAR project RT016) project and in collaboration with Imperial College.

##Developers
Alessio Ceroni
David Damerell
Kai Maass
Matthew Campbell
