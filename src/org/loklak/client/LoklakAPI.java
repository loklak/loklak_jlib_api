package org.loklak.client;

import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface LoklakAPI {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface GET {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface POST {}

    /**
     * A subset of Twitter's search API, which provides matched tweets.
     * @param q Tweet search query
     * @return
     */
    @GET
    JSONObject search(String q);

    /**
     * A subset of Twitter's search API, which provides matched tweets and maximum number of
     * results can be restricted.
     * @param q Tweet search query
     * @param count Maximum number of results
     * @return
     */
    @GET
    JSONObject search(String q, int count);

    /**
     * A subset of Twitter's search API, which provides matched tweets. A filtered search for
     * tweets in a time period can be made.
     * @param q Tweet search query
     * @param timezoneOffset Offset of a timezone in minutes
     * @param since Left bound of a query
     * @param until Right bound of a query
     * @return
     */
    @GET
    JSONObject search(String q, int timezoneOffset, String since, String until);

    /**
     * A subset of Twitter's search API, which provides matched tweets. A filtered search for
     * tweets in a time period can be made, and maximum number of matched results can be specified.
     * @param q Tweet search query
     * @param timezoneOffset Offset of a timezone in minutes
     * @param since Left bound of a query time
     * @param until Right bound of a query time
     * @param count Maximum number of results
     * @return
     */
    @GET
    JSONObject search(String q, int timezoneOffset, String since, String until, int count);

    /**
     *Provides search suggestions for tweets for matching query
     * @param q Suggestion query
     * @return
     */
    @GET
    JSONObject suggest(String q);

    /**
     * Provides search suggestions for tweets for matching query. Maximum number of suggestions
     * can also be specified.
     * @param q Suggestion query
     * @param count Maximum number of suggestions.
     * @return
     */
    @GET
    JSONObject suggest(String q, int count);

    /**
     * Provides search suggestions for tweets for matching query. Source and sorting on
     * timestamp basis can be specified.
     * @param q Suggestion query
     * @param source default: all
     * @param order Descending or ascending order on timestamp basis; default: Descending order.
     * @return
     */
    @GET
    JSONObject suggest(String q, String source, String order);

    /**
     * Provides search suggestions for tweets for matching query. Source and sorting on timestamp
     * basis and maximum number of suggestions can be specified.
     * @param q Suggestion query
     * @param source default: all
     * @param order Descending or ascending order on timestamp basis; default: Descending order.
     * @param count Maximum number of suggestions.
     * @return
     */
    @GET
    JSONObject suggest(String q, String source, String order, int count);

    /**
     * Provides search suggestions for tweets for matching query. Source, sorting on timestamp
     * basis, filtering of suggestions for a time period and maximum number of suggestions can be
     * specified.
     * @param q Suggestion query
     * @param source default: all
     * @param order Descending or ascending order on timestamp basis; default: Descending order
     * @param timezoneOffset Offset of a timezone in minutes
     * @param since Left bound of a query time
     * @param until Right bound of a query time
     * @param count Maximum number of suggestions.
     * @return
     */
    @GET
    JSONObject suggest(String q, String source, String order, int timezoneOffset, String since,
                       String until, int count);

    /**
     * Provides the combined result of the <code>hello</code> calls from all peers and provides a
     * list of addresses where the remote peers can be accessed.
     * @return
     */
    @GET
    JSONObject peers();

    /**
     * Used to announce that a new client (a new peer) has been started up.
     * @return
     */
    @GET
    JSONObject hello();

    /**
     * Provides mass data from the search back-end, including Twitter scraper. Provides search
     * results with a given set of query terms, extracts all the hashtags and usernames from the
     * result list and searches with those words again.
     * @param start Terms to search for, separated by comma(s)
     * @param depth Default 0, non-localhost clients may only set a maximum of 1
     * @param hashtags If true then hashtags from first search result are used for next searches
     * @param users If true then usernames from the results are used for next search requests
     * @return
     */
    @GET
    JSONObject crawler(String start, int depth, boolean hashtags, boolean users);

    /**
     * Provides the size of the internal ElasticSearch search index for messages and users.
     * Furthermore it provides the current browser client settings in the client_info.
     * @return
     */
    @GET
    JSONObject status();

    /**
     * Provides the retrieval of user followers and the accounts which the user is following.
     * @param screen_name Twitter username
     * @return
     */
    @GET
    JSONObject user(String screen_name);

    /**
     *Provides a list of loklak apps.
     * @return
     */
    @GET
    JSONObject apps();

    /**
     * Provides a list of loklak apps filtered by category.
     * @param category Category for filtering apps.
     * @return
     */
    @GET
    JSONObject apps(String category);

    /**
     * Transmits scraped data to the server hosting loklak_server
     * @param data JSONObject to be sent to hosting server.
     * @return
     */
    @POST
    JSONObject push(JSONObject data);
}
