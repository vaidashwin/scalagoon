# scalagoon

A bot for our dumb channel

## First Time Build

Because lein-scalac is locked on scala 2.9, we have to rebuild it.

1. Install [Leiningen](https://leiningen.org/)
2. Clone [lein-scalac](https://github.com/technomancy/lein-scalac)
3. In [project.cli](https://github.com/technomancy/lein-scalac/blob/master/project.clj) in the lein-scalac dir, change "2.9.1" to "2.12.6" (or whatever scala version)
4. In the lein-scalac dir, run `lein install` - This will download a bunch of shit and compile lein-scalac, and save it into your m2 repo.
5. Now you can use scalagoon normally (`lein run` to run in place.  `lein uberjar` will create a standalone jar with all the shit in it.)

### Other Notes

Note Leiningen doesn't know wtf to do with scala libs unless you specify the scala version.  I.E. `"org.scalaj" %% "scalaj-http" % "2.4.1"` needs to be `org.scalaj/scalaj-http_2.12 "2.4.1"`  (Note the \_2.12 specifying scala version).

[Cursive](https://cursive-ide.com/) is a nice intellij plugin for Clojure, but note the editting mode is weird for .clj files.  Also you have to register but it's free for non-commercial.


## License

Copyright © 2018 #mtgoon programming squad

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
