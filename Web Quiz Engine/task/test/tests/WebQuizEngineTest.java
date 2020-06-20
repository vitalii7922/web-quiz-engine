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

import static tests.TestHelper.*;
import static tests.ApiTester.*;


public class WebQuizEngineTest extends SpringTest {
    public WebQuizEngineTest() {
        super(WebQuizEngine.class, 8889);
    }

    private static int quizId1 = 0;
    private static int quizId2 = 0;

    private static String quiz1 =
        "{\n" +
        "  \"title\": \"The Java Logo\",\n" +
        "  \"text\": \"What is depicted on the Java logo?\",\n" +
        "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"],\n" +
        "  \"answer\": 2\n" +
        "}";

    private static String quiz2 =
        "{\n" +
        "  \"title\": \"The Ultimate Question\",\n" +
        "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
        "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"],\n" +
        "  \"answer\": 1\n" +
        "}";

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
        () -> testAllQuizzes(0),

        () -> testCreateQuiz(1),
        () -> testQuizExists(1),
        () -> testQuizNotExists(1),

        () -> testAllQuizzes(1),

        () -> testCreateQuiz(2),
        () -> testQuizExists(2),
        () -> testQuizNotExists(2),

        () -> testAllQuizzes(2),

        () -> checkQuizSuccess(quizId1, "0", false),
        () -> checkQuizSuccess(quizId1, "1", false),
        () -> checkQuizSuccess(quizId1, "2", true),
        () -> checkQuizSuccess(quizId1, "3", false),

        () -> checkQuizSuccess(quizId2, "0", false),
        () -> checkQuizSuccess(quizId2, "1", true),
        () -> checkQuizSuccess(quizId2, "2", false),
        () -> checkQuizSuccess(quizId2, "3", false),
    };

    private CheckResult testCreateQuiz(int quizNum) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizNum == 1 ? quiz1 : quiz2);
        HttpResp resp = new HttpResp(req.send(), url, "POST");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "id");

        JsonObject obj = json.getAsJsonObject();
        checkIsInt(resp, obj.get("id"), "id");

        if (quizNum == 1) {
            quizId1 = obj.get("id").getAsInt();
        } else {
            quizId2 = obj.get("id").getAsInt();
        }

        return CheckResult.correct();
    }

    private CheckResult testQuizExists(int quizNum) {

        int quizId = quizNum == 1 ? quizId1 : quizId2;
        String quiz = quizNum == 1 ? quiz1 : quiz2;

        String url = "/api/quizzes/" + quizId;

        HttpRequest req = get(url);
        HttpResp resp = new HttpResp(req.send(), url, "GET");

        checkStatusCode(resp, 200);

        JsonObject rightQuiz = getJson(quiz).getAsJsonObject();
        rightQuiz.remove("answer");
        rightQuiz.addProperty("id", quizId);

        JsonElement json = getJson(resp);
        checkIsObject(resp, json);
        checkObjectKey(resp, json, "id");
        checkObjectKey(resp, json, "title");
        checkObjectKey(resp, json, "text");
        checkObjectKey(resp, json, "options");

        JsonObject obj = json.getAsJsonObject();
        checkIsInt(resp, obj.get("id"), "id");
        checkIsString(resp, obj.get("title"), "title");
        checkIsString(resp, obj.get("text"), "text");
        checkIsArray(resp, obj.get("options"), "options");

        checkIntValue(resp, obj.get("id"), quizId, "id");

        if (!rightQuiz.equals(obj)) {
            return CheckResult.wrong(
                "The quiz sent to the program looked like this:\n" +
                getPrettyJson(rightQuiz) + "\n\n" +
                "But the received quiz looks like that:\n" +
                getPrettyJson(obj)
            );
        }

        return CheckResult.correct();
    }

    private CheckResult testQuizNotExists(int quizNum) {

        int quizId = quizNum == 1 ? quizId1 : quizId2;

        String url = "/api/quizzes/" + (quizId + 125);

        HttpRequest req = get(url);
        HttpResp resp = new HttpResp(req.send(), url, "GET");

        checkStatusCode(resp, 404);

        return CheckResult.correct();
    }

    private CheckResult testAllQuizzes(int count) {
        String url = "/api/quizzes";
        HttpResp resp = new HttpResp(get(url).send(), url, "GET");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsArray(resp, json);
        checkArrayLength(resp, json, count);

        JsonArray arr = json.getAsJsonArray();
        int index = 0;
        for (JsonElement elem : arr) {
            checkIsObject(resp, elem, "json[" + index + "]");
            ++index;
        }

        return CheckResult.correct();
    }

    private CheckResult checkQuizSuccess(int quizNum, String answerSent, boolean shouldResponse) {
        String url = "/api/quizzes/" + quizNum + "/solve";

        HttpRequest req = TestHelper.post(url, Map.of("answer", answerSent));
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
