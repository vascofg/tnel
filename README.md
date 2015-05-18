## JAX-RS with Grizzly

JAX-RS is a great way to build RESTful services in Java. This is a very quick and simple example of a REST service.

## Build and Run

Build the code with:

    $ mvn package

The POM file uses the [appassembler plugin](http://mojo.codehaus.org/appassembler/appassembler-maven-plugin/) to generate a wrapper script, so it's very simple to run the app. Simply execute:

    $ sh target/bin/app
    Starting grizzly...
    Oct 25, 2011 10:06:44 AM com.sun.grizzly.Controller logVersion
    INFO: Starting Grizzly Framework 1.9.18-i - Tue Oct 25 10:06:44 PDT 2011
    Jersey started with WADL available at http://localhost:9998/application.wadl.

(On Windows use target/bin/app.bat instead)

## WADL

A .WADL file describes the resources provided by a service and the relationships between them.

After running the app the WADL file will be available at */application.wadl.

## Heroku

There is already an app configured in Heroku [here](http://blooming-savannah-4545.herokuapp.com/).

To deploy to it, you have to add the remote to your git repository:

    heroku git:remote -a blooming-savannah-4545
    
And then it's as simple as

    git push heroku master