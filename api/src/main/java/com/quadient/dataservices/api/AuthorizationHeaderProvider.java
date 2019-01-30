package com.quadient.dataservices.api;

public interface AuthorizationHeaderProvider {

    /**
     * Gets the authorization header to use.
     * 
     * @param forceRenewedToken whether or not to force the renewal of the access token in the header (if false, an
     *            existing token may be reused).
     * @return
     */
    public String getAuthorizationHeader(boolean forceRenewedToken);

    /**
     * Gets the credentials used to provide the authorization header(s).
     * 
     * @return
     */
    public Credentials getCredentials();
}
