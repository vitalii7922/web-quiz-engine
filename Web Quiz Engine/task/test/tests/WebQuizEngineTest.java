package tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.WebQuizEngine;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.Map;

import static tests.ApiTester.checkArrayLength;
import static tests.ApiTester.checkBooleanValue;
import static tests.ApiTester.checkIsArray;
import static tests.ApiTester.checkIsBoolean;
import static tests.ApiTester.checkIsObject;
import static tests.ApiTester.checkIsString;
import static tests.ApiTester.checkObjectKey;
import static tests.TestHelper.checkStatusCode;
import static tests.TestHelper.getJson;

public class WebQuizEngineTest extends SpringTest {
    public WebQuizEngineTest() {
        super(WebQuizEngine.class, 8889);
    }

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
        this::checkQuizReceived,
        () -> checkQuizSuccess("2", true),
        () -> checkQuizSuccess("1", false)
    };

    private CheckResult checkQuizReceived() {
        String url = "/api/quiz";
        HttpResp resp = new HttpResp(get(url).send(), url, "GET");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "title");
        checkObjectKey(resp, json, "text");
        checkObjectKey(resp, json, "options");

        JsonObject obj = json.getAsJsonObject();

        checkIsString(resp, obj.get("title"), "title");
        checkIsString(resp, obj.get("text"), "text");
        checkIsArray(resp, obj.get("options"), "options");

        JsonArray arr = obj.get("options").getAsJsonArray();
        checkArrayLength(resp, arr, 4, "options");

        checkIsString(resp, arr.get(0), "options[0]");
        checkIsString(resp, arr.get(1), "options[1]");
        checkIsString(resp, arr.get(2), "options[2]");
        checkIsString(resp, arr.get(3), "options[3]");

        return CheckResult.correct();
    }

    private CheckResult checkQuizSuccess(String answerSent, boolean shouldResponse) {
        String url = "/api/quiz";

        HttpRequest req = post(url, Map.of("answer", answerSent));
        HttpResp resp = new HttpResp(req.send(), url, "POST");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "success");
        checkObjectKey(resp, json, "feedback");

        JsonObject obj = json.getAsJsonObject();

        checkIsBoolean(resp, obj.get("success"), "success");
        checkIsString(resp, obj.get("feedback"), "feedback");

        checkBooleanValue(resp, obj.get("success"), shouldResponse, "success");

        return CheckResult.correct();
    }
}
