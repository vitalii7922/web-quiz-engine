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
import org.hyperskill.hstest.testcase.TestCase;
import org.junit.rules.TestRule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static tests.TestHelper.*;
import static tests.ApiTester.*;

public class WebQuizEngineTest extends SpringTest {

    public WebQuizEngineTest() {
        super(WebQuizEngine.class, 8889);
    }

    private static String quiz1 =
        "{\n" +
        "  \"title\": \"The Java Logo\", \n" +
        "  \"text\": \"What is depicted on the Java logo?\",\n" +
        "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"],\n" +
        "  \"answer\": [2]\n" +
        "}";

    private static String quiz2 =
        "{\n" +
        "  \"title\": \"The Ultimate Question\",\n" +
        "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
        "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"],\n" +
        "  \"answer\": [1]\n" +
        "}";

    private static String quiz3 =
        "{\n" +
        "  \"title\": \"Math1\",\n" +
        "  \"text\": \"Which of the following is equal to 4?\",\n" +
        "  \"options\": [\"1+3\",\"2+2\",\"8-1\",\"1+5\"],\n" +
        "  \"answer\": [0,1]\n" +
        "}";

    private static String quiz4 =
        "{\n" +
        "  \"title\": \"Math2\",\n" +
        "  \"text\": \"Which of the following is equal to 4?\",\n" +
        "  \"options\": [\"1+1\",\"2+2\",\"8-1\",\"5-1\"],\n" +
        "  \"answer\": [1,3]\n" +
        "}";

    private static String quiz5 =
        "{\n" +
        "  \"title\": \"Math3\",\n" +
        "  \"text\": \"Which of the following is equal to 4?\",\n" +
        "  \"options\": [\"2*3\",\"5*8\",\"8*0\",\"1*5\"],\n" +
        "  \"answer\": []\n" +
        "}";

    private static String quiz6 =
        "{\n" +
        "  \"title\": \"Math4\",\n" +
        "  \"text\": \"Which of the following is equal to 4?\",\n" +
        "  \"options\": [\"2*3\",\"5*8\",\"8*0\",\"1*5\"]\n" +
        "}";

    private static String quiz7 =
        "{\n" +
        "  \"title\": \"Math5\",\n" +
        "  \"text\": \"Which of the following is equal to 4?\",\n" +
        "  \"options\": [\"2^2\",\"2+2\",\"2-2\",\"2*2\"],\n" +
        "  \"answer\": [0,1,3]\n" +
        "}";

    private static String[] quizzes = new String[] {
        quiz1, quiz2, quiz3, quiz4, quiz5, quiz6, quiz7
    };

    private static int[] quizIds = new int[] {
        0, 0, 0, 0, 0, 0, 0
    };

    private static String error400noTitle =
        "{\n" +
        "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
        "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
        "  \"answer\": [1]\n" +
        "}";

    private static String error400emptyTitle =
        "{\n" +
        "  \"title\": \"\",\n" +
        "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
        "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
        "  \"answer\": [1]\n" +
        "}";

    private static String error400noText =
        "{\n" +
        "  \"title\": \"123123123\",\n" +
        "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
        "  \"answer\": [1]\n" +
        "}";

    private static String error400emptyText =
        "{\n" +
        "  \"title\": \"The Ultimate Question\",\n" +
        "  \"text\": \"\",\n" +
        "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
        "  \"answer\": [1]\n" +
        "}";

    private static String error400noOptions =
        "{\n" +
        "  \"title\": \"The Ultimate Question\",\n" +
        "  \"text\": \"123123123\",\n" +
        "  \"answer\": [1]\n" +
        "}";

    private static String error400emptyOptions =
        "{\n" +
        "  \"title\": \"The Ultimate Question\",\n" +
        "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
        "  \"options\": [ ]\n" +
        "  \"answer\": [ ]\n" +
        "}";

    private static String error400oneOption =
        "{\n" +
        "  \"title\": \"The Ultimate Question\",\n" +
        "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
        "  \"options\": [\"Everything goes right\"]\n" +
        "  \"answer\": [0]\n" +
        "}";

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
        () -> testAllQuizzes(0),

        () -> testCreateQuiz(0),
        () -> testQuizExists(0),
        () -> testQuizNotExists(0),

        () -> testAllQuizzes(1),

        () -> testCreateQuiz(1),
        () -> testQuizExists(1),
        () -> testQuizNotExists(1),

        () -> testAllQuizzes(2),

        () -> checkQuizSuccess(quizIds[0], "[0]", false),
        () -> checkQuizSuccess(quizIds[0], "[1]", false),
        () -> checkQuizSuccess(quizIds[0], "[2]", true),
        () -> checkQuizSuccess(quizIds[0], "[3]", false),

        () -> checkQuizSuccess(quizIds[1], "[0]", false),
        () -> checkQuizSuccess(quizIds[1], "[1]", true),
        () -> checkQuizSuccess(quizIds[1], "[2]", false),
        () -> checkQuizSuccess(quizIds[1], "[3]", false),

        () -> addIncorrectQuiz(error400noTitle),
        () -> addIncorrectQuiz(error400emptyTitle),
        () -> addIncorrectQuiz(error400noText),
        () -> addIncorrectQuiz(error400emptyText),
        () -> addIncorrectQuiz(error400noOptions),
        () -> addIncorrectQuiz(error400emptyOptions),
        () -> addIncorrectQuiz(error400oneOption),

        () -> testCreateQuiz(2),
        () -> testQuizExists(2),
        () -> testQuizNotExists(2),
        () -> checkQuizSuccess(quizIds[2], "[]", false),
        () -> checkQuizSuccess(quizIds[2], "[0]", false),
        () -> checkQuizSuccess(quizIds[2], "[1]", false),
        () -> checkQuizSuccess(quizIds[2], "[2]", false),
        () -> checkQuizSuccess(quizIds[2], "[3]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1]", true),
        () -> checkQuizSuccess(quizIds[2], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[2], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2,3]", false),

        () -> testCreateQuiz(3),
        () -> testQuizExists(3),
        () -> testQuizNotExists(3),
        () -> checkQuizSuccess(quizIds[3], "[]", false),
        () -> checkQuizSuccess(quizIds[3], "[0]", false),
        () -> checkQuizSuccess(quizIds[3], "[1]", false),
        () -> checkQuizSuccess(quizIds[3], "[2]", false),
        () -> checkQuizSuccess(quizIds[3], "[3]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true),
        () -> checkQuizSuccess(quizIds[3], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2,3]", false),

        () -> testCreateQuiz(4),
        () -> testQuizExists(4),
        () -> testQuizNotExists(4),
        () -> checkQuizSuccess(quizIds[4], "[]", true),
        () -> checkQuizSuccess(quizIds[4], "[0]", false),
        () -> checkQuizSuccess(quizIds[4], "[1]", false),
        () -> checkQuizSuccess(quizIds[4], "[2]", false),
        () -> checkQuizSuccess(quizIds[4], "[3]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[4], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2,3]", false),

        () -> testCreateQuiz(5),
        () -> testQuizExists(5),
        () -> testQuizNotExists(5),
        () -> checkQuizSuccess(quizIds[5], "[]", true),
        () -> checkQuizSuccess(quizIds[5], "[0]", false),
        () -> checkQuizSuccess(quizIds[5], "[1]", false),
        () -> checkQuizSuccess(quizIds[5], "[2]", false),
        () -> checkQuizSuccess(quizIds[5], "[3]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[5], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2,3]", false),

        () -> testCreateQuiz(6),
        () -> testQuizExists(6),
        () -> testQuizNotExists(6),
        () -> checkQuizSuccess(quizIds[6], "[]", false),
        () -> checkQuizSuccess(quizIds[6], "[0]", false),
        () -> checkQuizSuccess(quizIds[6], "[1]", false),
        () -> checkQuizSuccess(quizIds[6], "[2]", false),
        () -> checkQuizSuccess(quizIds[6], "[3]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true),
        () -> checkQuizSuccess(quizIds[6], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2,3]", false),
    };

    private CheckResult testCreateQuiz(int quizNum) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResp resp = new HttpResp(req.send(), url, "POST");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "id");

        JsonObject obj = json.getAsJsonObject();
        checkIsInt(resp, obj.get("id"), "id");

        quizIds[quizNum] = obj.get("id").getAsInt();

        return CheckResult.correct();
    }

    private CheckResult testQuizExists(int quizNum) {

        int quizId = quizIds[quizNum];
        String quiz = quizzes[quizNum];

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

        int quizId = quizIds[quizNum];

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

        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
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

    private CheckResult addIncorrectQuiz(String quiz) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quiz);
        HttpResp resp = new HttpResp(req.send(), url, "POST");
        checkStatusCode(resp, 400);
        return CheckResult.correct();
    }
}
