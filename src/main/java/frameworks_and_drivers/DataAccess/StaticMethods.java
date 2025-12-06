package frameworks_and_drivers.DataAccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Response;
import use_case.DataAccessException;

/**
 * Utility class for making HTTP requests to the API.
 * Supports GET and POST methods with automatic handling of API key,
 * JSON parsing, and error handling.
 */
public class StaticMethods {

  public native static String jsMakeRequest(
      String requestType,
      String url
  );

    // private static final OkHttpClient CLIENT = new OkHttpClient();

    /**
     * Make a request to the API using GET or POST.
     * Automatically adds the API key to parameters and handles JSON parsing.
     *
     * @param requestType "GET" or "POST"
     * @param method      The endpoint path (e.g., "/test-api")
     * @param parameters      Map of request parameters (key-value pairs), can be null
     * @param apiKey      The API key for authentication
     * @return JSONObject parsed from the API response
     * @throws DataAccessException if request fails or API returns an error
     * @throws IllegalArgumentException if request type is illegal
     */
    public static JSONObject makeApiRequest(
            String requestType,
            String method,
            Map<String, String> parameters,
            String apiKey
    ) throws DataAccessException {
        Map<String, String> params = parameters;
        // Ensure params map exists and includes API key
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("key", apiKey);

        // Request request;
        String requestStr;

        if ("GET".equalsIgnoreCase(requestType)) {
            // Build query string
            StringBuilder query = new StringBuilder();
            if (!params.isEmpty()) {
                query.append("?");
                params.forEach((k, v) -> query.append(k).append("=").append(v).append("&"));
                query.setLength(query.length() - 1);
            }

            // request = new Request.Builder()
            //         .url(Constants.API_URL + method + query)
            //         .get()
            //         .build();
            requestStr = Constants.API_URL + method + query.toString();

        } else if ("POST".equalsIgnoreCase(requestType)) {
            // Build form body for POST
            // FormBody.Builder formBuilder = new FormBody.Builder();
            // params.forEach(formBuilder::add);
            // RequestBody formBody = formBuilder.build();

            // Build form body for POST
            StringBuilder query = new StringBuilder();
            if (!params.isEmpty()) {
                query.append("?");
                params.forEach((k, v) -> query.append(k).append("=").append(v).append("&"));
                query.setLength(query.length() - 1);
            }

            // request = new Request.Builder()
            //         .url(Constants.API_URL + method + query)
            //         .post(formBody)
            //         .build();
            requestStr = Constants.API_URL + method + query.toString();

        } else {
            throw new IllegalArgumentException("Unsupported request type: " + requestType);
        }

        // Execute request
        // try (Response response = CLIENT.newCall(request).execute()) {
            // String responseBody = response.body().string();
            String responseBody = jsMakeRequest(requestType, requestStr);
            JSONObject responseJSON = new JSONObject(responseBody);
            // int statusCode = response.code();
            int statusCode = responseJSON.getInt("httpcode");

            // If Statement
            if (statusCode == Constants.SUCCESS_CODE) {
                return responseJSON;
            } else if (statusCode == Constants.API_KEY_ERROR) {
                throw new DataAccessException("API Key Error: " +
                        responseJSON.optString(Constants.ERROR_MESSAGE, "Unknown key error"));
            } else if (statusCode == Constants.BAD_REQUEST) {
                throw new DataAccessException("Bad Request: " +
                        responseJSON.optString(Constants.ERROR_MESSAGE, "Unknown request error"));
            } else {
                throw new DataAccessException("API error: " +
                        responseJSON.optString(Constants.ERROR_MESSAGE, "Unknown API error"));
            }
        // } catch (IOException | JSONException e) {
        //     throw new DataAccessException("Request failed: " + e.getMessage());
        // }
    }
}
