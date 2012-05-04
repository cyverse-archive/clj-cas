# clj-cas

A CAS client designed for use with Ring.

## Usage

### Ticket Validation Only

```clojure
; When used within the primary Ring handler.
(defn site-handler [routes]
  (-> routes
      (validate-cas-proxy-ticket cas-server-fn server-name-fn)
      wrap-keyword-params
      wrap-nested-params
      wrap-query-params))

; When used for a context within unsecured routes.
(defroutes my-routes
  ; ...
  (context "/secured" []
    (validate-cas-proxy-ticket secured-routes cas-server-fn server-name-fn)))
```

The first argument to `validate-cas-proxy-ticket` refers to the route
definitions that should be secured.  The second argument is a function that
returns the name of the CAS server, which is a base URL that is used to
connect to the CAS server.  The third argument is the name of the local
server, which is a base URL that the CAS server can use to connect to the
service.

When a request arrives, the CAS proxy ticket should be included in the
`proxyToken` query string parameter of the request URL.

Here's a more complete example of a Ring application with one unsecured
endpoint at `/public` and one secured endpoint at `/secured/private`.  This
example assumes that the CAS server is running on `by-tor.example.org` and
listening on the standard HTTPS port.  It also assumes that the Ring app that
contains the secured services is running on `snow-dog.example.org` and
listening to port 2112.

```clojure
(defn cas-server []
  "https://by-tor.example.org/cas/")

(defn server-name []
  "http://snow-dog.example.org:2112")

(defroutes secured-routes
  (GET "/private" []
    "This is private information")

  (route/not-found "Not found."))

(defroutes all-routes
  (GET "/public" []
    "This is public information.")

  (context "/secured" []
    (validate-cas-proxy-ticket secured-routes cas-server server-name))

  (route/not-found "Not found."))

(defn site-handler [routes]
  (-> routes
      wrap-keyword-params
      wrap-nested-params
      wrap-query-params))

(def app (site-handler all-routes))
```

### Ticket Validation with Group Membership Verification

```clojure
; When used within the primary Ring handler.
(defn site-handler [routes]
  (-> routes
      (validate-cas-group-membership cas-server-fn server-name-fn
        group-attr-name-fn allowed-groups-fn)
      wrap-keyword-params
      wrap-nested-params
      wrap-query-params))

; When used for a context within unsecured routes.
(defroutes my-routes
  ; ...
  (context "/secured" []
    (validate-cas-group-membership secured-routes cas-server-fn server-name-fn
      group-attr-name-fn allowed-groups-fn)))
```

The first three arguments `validate-cas-group-membership` are identical to
those of `validate-cas-proxy-ticket`.  The fourth argument is a function that
returns the name of the CAS user attribute that contains the group membership
information for the authenticated user.  The value of this attribute should be
a comma-and-whitespace delimited string surrounded by square brackets.  For
example, `[foo, bar, baz]`.  The last argument is a function that returns the
list of groups that are allowed to access the resource.

Here's a more complete example that is identical to the example for ticket
validation only except that the secured endpoint can only be accessed by
members of the `admin` group.

```clojure
(defn cas-server []
  "https://by-tor.example.org/cas/")

(defn server-name []
  "http://snow-dog.example.org:2112")

(defn group-attr-name []
  "entitlement")

(defn allowed-groups []
  ["admin"])

(defroutes secured-routes
  (GET "/private" []
    "This is private information")

  (route/not-found "Not found."))

(defroutes all-routes
  (GET "/public" []
    "This is public information.")

  (context "/secured" []
    (validate-cas-group-membership secured-routes cas-server server-name
      group-attr-name allowed-groups))

  (route/not-found "Not found."))

(defn site-handler [routes]
  (-> routes
      wrap-keyword-params
      wrap-nested-params
      wrap-query-params))

(def app (site-handler all-routes))
```

## License

Copyright (c) 2012, The Arizona Board of Regents on behalf of The University
of Arizona

All rights reserved.

Developed by: iPlant Collaborative at BIO5 at The University of Arizona
http://www.iplantcollaborative.org

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

 * Neither the name of the iPlant Collaborative, BIO5, The University of
   Arizona nor the names of its contributors may be used to endorse or promote
   products derived from this software without specific prior written
   permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
