package tests;

import com.google.gson.*;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;

import java.util.Map;

class HttpResp {
    private String url;
    private String method;
    private HttpResponse resp;

    HttpResp(HttpResponse resp, String url, String method) {
        this.url = url;
        this.resp = resp;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getRequest() {
        return getMethod() + " " + getUrl();
    }

    public int getStatusCode() {
        return resp.getStatusCode();
    }

    public Map<String, String> getHeaders() {
        return resp.getHeaders();
    }

    public byte[] getRawContent() {
        return resp.getRawContent();
    }

    public String getContent() {
        return resp.getContent();
    }

    public JsonElement getJson() {
        return resp.getJson();
    }
}

public class TestHelper {
    static void checkStatusCode(HttpResp resp, int status) {
        if (resp.getStatusCode() != status) {
            throw new WrongAnswer(
                resp.getRequest() +
                    " should respond with status code " + status + ", " +
                    "responded: " + resp.getStatusCode() + "\n\n" +
                    "Response body:\n\n" + resp.getContent()
            );
        }
    }

    static void checkHeader(HttpResp resp, String header, String value) {
        Map<String, String> headers = resp.getHeaders();
        if (!headers.containsKey(header)) {
            throw new WrongAnswer(
                resp.getRequest() +
                    " should respond with header \"Content-Type\", " +
                    "but this header is not found in the response."
            );
        }
        String actualValue = headers.get(header);
        if (!actualValue.equals(value)) {
            throw new WrongAnswer(
                resp.getRequest() +
                    " should respond with header \"Content-Type\" being " +
                    "equal to " + value + " but in the response header " +
                    "\"Content-Type\" is equal to " + actualValue + "."
            );
        }
    }

    static JsonElement getJson(HttpResp resp) {
        checkHeader(resp,
            HttpHeaders.CONTENT_TYPE,
            ContentType.APPLICATION_JSON.getMimeType()
        );
        try {
            return resp.getJson();
        } catch (Exception ex) {
            throw new WrongAnswer(
                resp.getRequest() + " should return a valid JSON"
            );
        }
    }

    static JsonElement getJson(String json) {
        return new JsonParser().parse(json);
    }
}
