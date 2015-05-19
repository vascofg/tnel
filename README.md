## JAX-RS with Jetty

Using tutorial from [Jersey documentation](https://jersey.java.net/documentation/latest/getting-started.html#heroku-webapp).

## Build and Run

Build the code with:

    $ mvn clean package

## Heroku

There is already an app configured in Heroku [here](http://meau.herokuapp.com/).

To deploy to it, you have to add the remote to your git repository:

    heroku git:remote -a meau
    
And then it's as simple as

    git push heroku master