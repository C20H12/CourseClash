package data_access;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.DataAccessException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static data_access.Constants.*;

/**
 * Utility class for making HTTP requests to the API.
 * Supports GET and POST methods with automatic handling of API key,
 * JSON parsing, and error handling.
 */
public class StaticMethods {

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Make a request to the API using GET or POST.
     * Automatically adds the API key to parameters and handles JSON parsing.
     *
     * @param requestType "GET" or "POST"
     * @param method      The endpoint path (e.g., "/test-api")
     * @param params      Map of request parameters (key-value pairs), can be null
     * @param apiKey      The API key for authentication
     * @return JSONObject parsed from the API response
     * @throws DataAccessException if request fails or API returns an error
     */
    public static JSONObject makeApiRequest(
            String requestType,
            String method,
            Map<String, String> params,
            String apiKey
    ) throws DataAccessException {

        // Ensure params map exists and includes API key
        if (params == null) params = new HashMap<>();
        params.put("key", apiKey);

        Request request;

        if ("GET".equalsIgnoreCase(requestType)) {
            // Build query string
            StringBuilder query = new StringBuilder();
            if (!params.isEmpty()) {
                query.append("?");
                params.forEach((k, v) -> query.append(k).append("=").append(v).append("&"));
                query.setLength(query.length() - 1); // remove trailing &
            }

            request = new Request.Builder()
                    .url(API_URL + method + query)
                    .get()
                    .build();

        } else if ("POST".equalsIgnoreCase(requestType)) {
            // Build form body for POST
            FormBody.Builder formBuilder = new FormBody.Builder();
            params.forEach(formBuilder::add);
            RequestBody formBody = formBuilder.build();

            request = new Request.Builder()
                    .url(API_URL + method)
                    .post(formBody)
                    .build();

        } else {
            throw new IllegalArgumentException("Unsupported request type: " + requestType);
        }

        // Execute request
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject responseJSON = new JSONObject(responseBody);
            int statusCode = response.code();

            // If Statement
            if (statusCode == SUCCESS_CODE) {
                return responseJSON;
            } else if (statusCode == API_KEY_ERROR) {
                throw new DataAccessException("API Key Error: " +
                        responseJSON.optString(ERROR_MESSAGE, "Unknown key error"));
            } else if (statusCode == BAD_REQUEST) {
                throw new DataAccessException("Bad Request: " +
                        responseJSON.optString(ERROR_MESSAGE, "Unknown request error"));
            } else {
                throw new DataAccessException("API error: " +
                        responseJSON.optString(ERROR_MESSAGE, "Unknown API error"));
            }
        } catch (IOException | JSONException e) {
            throw new DataAccessException("Request failed: " + e.getMessage());
        }
    }
}
