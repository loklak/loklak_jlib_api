package org.loklak;

/**
 * This class provides constant values that are most often used while testing.
 */
public class Constants {

    public final static String HOST_SERVER_URL = "https://api.loklak.org";
    public final static String HELLO_API_ENDPOINT = "/api/hello.json";
    public final static String PUSH_API_ENDPOINT = "/api/push.json";
    public final static String HELLO_API_URL = HOST_SERVER_URL + HELLO_API_ENDPOINT;
    public final static String PUSH_API_URL = HOST_SERVER_URL + PUSH_API_ENDPOINT;
    // keys of JSONObject in reply of push API
    public final static String JSON_KEY_PUSH_STATUS = "status";
    public final static String JSON_KEY_PUSH_MESSAGE = "message";
    public final static String JSON_KEY_PUSH_RECORDS = "records";
    public final static String JSON_KEY_PUSH_MPS = "mps";
    // keys of JSONObject in reply to hello API
    public final static String JSON_KEY_HELLO_PUBLIC_KEY = "public_key";
    public final static String JSON_KEY_HELLO_ALGORITHM_KEY = "key_algorithm";
    public final static String JSON_KEY_HELLO_PEER_HASH = "peer_hash";
    public final static String JSON_KEY_HELLO_PEER_HASH_ALGORITHM = "peer_hash_algorithm";
    public final static String JSON_KEY_HELLO_SESSION = "session";
    public final static String JSON_KEY_HELLO_STATUS = "status";
}
