package tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.WebQuizEngine;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.exception.outcomes.FatalError;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import static tests.TestHelper.*;
import static tests.ApiTester.*;

public class WebQuizEngineTest extends SpringTest {
    public WebQuizEngineTest() {
        super(WebQuizEngine.class, 8889, "../quizdb.mv.db");
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


    private static String login1 = "test@google.com";
    private static String pass1 = "qwerty";

    private static String login2 = "user@google.com";
    private static String pass2 = "12345";

    private static HttpRequest auth(HttpRequest req, int user) {
        String login;
        String pass;
        if (user == 1) {
            login = login1;
            pass = pass1;
        } else if (user == 2) {
            login = login2;
            pass = pass2;
        } else {
            login = fakeLogin;
            pass = fakePass;
        }
        return req.basicAuth(login, pass);
    }

    private static String fakeLogin = "qwerty@google.com";
    private static String fakePass = "456534";

    private static String noAtInLogin_login = "google.com";
    private static String noAtInLogin_pass = "asddfggh";

    private static String noDotInLogin_login = "well@googlecom";
    private static String noDotInLogin_pass = "rtyfgcxsfd";

    private static String loginExist_login = "test@google.com";
    private static String loginExist_pass = "rtyfgcxsfd1";

    private static String shortPassword_login = "someuser@gmail.com";
    private static String shortPassword_pass = "1234";

    private static String shortPassword2_login = "someuser2@gmail.com";
    private static String shortPassword2_pass = "123";

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
        // Test login
        () -> testRegister(login1, pass1, 200),
        () -> testRegister(login2, pass2, 200),
        () -> testRegister(noAtInLogin_login, noAtInLogin_pass, 400),
        () -> testRegister(noDotInLogin_login, noDotInLogin_pass, 400),
        () -> testRegister(loginExist_login, loginExist_pass, 400),
        () -> testRegister(shortPassword_login, shortPassword_pass, 400),
        () -> testRegister(shortPassword2_login, shortPassword2_pass, 400),


        // Add 2 quizzes and check their existence
        () -> testAllQuizzes(0, 0,1),
        () -> testAllQuizzes(0, 0,2),

        () -> testCreateQuiz(0, 1),
        () -> testQuizExists(0, 2),
        () -> testQuizNotExists(0, 2, 125),

        () -> testAllQuizzes(1, 0,1),

        () -> testCreateQuiz(1, 2),
        () -> testQuizExists(1, 2),
        () -> testQuizNotExists(1, 2, 125),

        () -> testAllQuizzes(2, 0,2),


        // No auth operations tests
        () -> testAllQuizzesNoAuth(),
        () -> testCreateQuizNoAuth(1),
        () -> testCreateQuizNoAuth(2),
        () -> testSolveQuizNoAuth(quizIds[0], "[0]"),
        () -> testSolveQuizNoAuth(quizIds[1], "[1]"),
        () -> testDeleteQuizzesNoAuth(quizIds[0]),
        () -> testDeleteQuizzesNoAuth(quizIds[1]),
        () -> testCompletedQuizzesNoAuth(),


        // Fake auth operations tests
        () -> testAllQuizzesFakeAuth(),
        () -> testCreateQuizFakeAuth(1),
        () -> testCreateQuizFakeAuth(2),
        () -> testSolveQuizFakeAuth(quizIds[0], "[0]"),
        () -> testSolveQuizFakeAuth(quizIds[1], "[1]"),
        () -> testDeleteQuizzesFakeAuth(quizIds[0]),
        () -> testDeleteQuizzesFakeAuth(quizIds[1]),
        () -> testCompletedQuizzesFakeAuth(),


        // Solve two quizzes
        () -> checkQuizSuccess(quizIds[0], "[0]", false, 1),
        () -> checkQuizSuccess(quizIds[0], "[1]", false, 2),
        () -> checkQuizSuccess(quizIds[0], "[2]", true, 1),
        () -> checkQuizSuccess(quizIds[0], "[3]", false, 2),

        () -> checkQuizSuccess(quizIds[1], "[0]", false, 1),
        () -> checkQuizSuccess(quizIds[1], "[1]", true, 2),
        () -> checkQuizSuccess(quizIds[1], "[2]", false, 1),
        () -> checkQuizSuccess(quizIds[1], "[3]", false, 2),


        // Check completed
        () -> testCompletedQuizzes(1, 1, 0 ,0, quizIds[0]),
        () -> testCompletedQuizzes(1, 2, 0, 0, quizIds[1]),


        // Test database save
        () -> testAllQuizzes(2, 0, 1),
        () -> testAllQuizzes(2, 0,2),
        () -> reloadServer(),
        () -> testAllQuizzes(2, 0,1),
        () -> testAllQuizzes(2, 0,2),
        () -> checkQuizSuccess(quizIds[0], "[2]", true, 2),
        () -> checkQuizSuccess(quizIds[0], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[1], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[1], "[1]", true, 1),


        // Check completed
        () -> testCompletedQuizzes(2, 1, 0 ,1, quizIds[0]),
        () -> testCompletedQuizzes(2, 1, 0 ,0, quizIds[1]),

        () -> testCompletedQuizzes(2, 2, 0, 1, quizIds[1]),
        () -> testCompletedQuizzes(2, 2, 0, 0, quizIds[0]),


        // Test wrongly created quizzes
        () -> addIncorrectQuiz(error400noTitle, 1),
        () -> addIncorrectQuiz(error400emptyTitle, 2),
        () -> addIncorrectQuiz(error400noText, 1),
        () -> addIncorrectQuiz(error400emptyText, 2),
        () -> addIncorrectQuiz(error400noOptions, 1),
        () -> addIncorrectQuiz(error400emptyOptions, 2),
        () -> addIncorrectQuiz(error400oneOption, 1),


        // Test multiple answers
        () -> testCreateQuiz(2, 1),
        () -> testQuizExists(2, 1),
        () -> testQuizNotExists(2, 1, 125),
        () -> checkQuizSuccess(quizIds[2], "[]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0,1]", true, 2),
        () -> checkQuizSuccess(quizIds[2], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[0,1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[2], "[1,2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2,3]", false, 1),

        () -> testCreateQuiz(3, 1),
        () -> testQuizExists(3, 1),
        () -> testQuizNotExists(3, 1, 125),
        () -> checkQuizSuccess(quizIds[3], "[]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0,1]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[0,1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[3], "[1,2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2,3]", false, 1),

        () -> testCreateQuiz(4, 1),
        () -> testQuizExists(4, 1),
        () -> testQuizNotExists(4, 1, 125),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[4], "[1,2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2,3]", false, 2),

        () -> testCreateQuiz(5, 1),
        () -> testQuizExists(5, 1),
        () -> testQuizNotExists(5, 1, 125),
        () -> checkQuizSuccess(quizIds[5], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[5], "[0]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[1,2,3]", false, 1),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2,3]", false, 1),

        () -> testCreateQuiz(6, 1),
        () -> testQuizExists(6, 1),
        () -> testQuizNotExists(6, 1, 125),
        () -> checkQuizSuccess(quizIds[6], "[]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[1]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[1,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[6], "[1,2,3]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2,3]", false, 2),

        () -> testAllQuizzes(7, 0,2),
        () -> reloadServer(),
        () -> testAllQuizzes(7, 0,2),
        () -> checkQuizSuccess(quizIds[5], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[5], "[0]", false, 2),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false, 1),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true, 2),


        // Check completed
        () -> testCompletedQuizzes(5, 1, 0 ,4, quizIds[0]),
        () -> testCompletedQuizzes(5, 1, 0 ,3, quizIds[1]),
        () -> testCompletedQuizzes(5, 1, 0 ,2, quizIds[4]),
        () -> testCompletedQuizzes(5, 1, 0 ,1, quizIds[5]),
        () -> testCompletedQuizzes(5, 1, 0 ,0, quizIds[5]),

        () -> testCompletedQuizzes(6, 2, 0, 5, quizIds[1]),
        () -> testCompletedQuizzes(6, 2, 0, 4, quizIds[0]),
        () -> testCompletedQuizzes(6, 2, 0, 3, quizIds[2]),
        () -> testCompletedQuizzes(6, 2, 0, 2, quizIds[3]),
        () -> testCompletedQuizzes(6, 2, 0, 1, quizIds[6]),
        () -> testCompletedQuizzes(6, 2, 0, 0, quizIds[6]),


        // Test pagination completed quizzes
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[4], "[]", true, 1),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true, 1),

        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true, 2),
        () -> checkQuizSuccess(quizIds[5], "[]", true, 2),

        () -> testCompletedQuizzes(10, 1, 0 ,0, quizIds[6]),
        () -> testCompletedQuizzes(10, 1, 0 ,1, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,2, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,3, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,4, quizIds[4]),
        () -> testCompletedQuizzes(10, 1, 0 ,5, quizIds[4]),
        () -> testCompletedQuizzes(1, 1, 1 ,0, quizIds[0]),

        () -> testCompletedQuizzes(10, 2, 0 ,0, quizIds[5]),
        () -> testCompletedQuizzes(10, 2, 0 ,1, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,2, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,3, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,4, quizIds[3]),
        () -> testCompletedQuizzes(10, 2, 0 ,5, quizIds[3]),
        () -> testCompletedQuizzes(2, 2, 1 ,1, quizIds[1]),
        () -> testCompletedQuizzes(2, 2, 1 ,0, quizIds[0]),


        // Test delete
        () -> testDelete(quizIds[0], 1, 204),
        () -> testDelete(quizIds[0], 1, 404),
        () -> testQuizNotExists(0, 1, 0),
        () -> testQuizNotExists(0, 2, 0),
        () -> testQuizExists(1, 1),
        () -> testQuizExists(1, 2),

        () -> testDelete(quizIds[1], 1, 403),
        () -> testDelete(quizIds[1], 1, 403),
        () -> testDelete(quizIds[1], 2, 204),
        () -> testDelete(quizIds[1], 2, 404),
        () -> testDelete(quizIds[1], 1, 404),
        () -> testQuizNotExists(0, 1, 0),
        () -> testQuizNotExists(0, 2, 0),
        () -> testQuizNotExists(1, 1, 0),
        () -> testQuizNotExists(1, 2, 0),

        () -> testAllQuizzes(5, 0,1),
        () -> reloadServer(),
        () -> testAllQuizzes(5, 0,2),
        () -> testQuizNotExists(0, 1, 0),
        () -> testQuizNotExists(0, 2, 0),
        () -> testQuizNotExists(1, 1, 0),
        () -> testQuizNotExists(1, 2, 0),


        // Test pagination all quizzes
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testCreateQuiz(6, 1),
        () -> testCreateQuiz(6, 2),
        () -> testAllQuizzes(10, 0,1),
        () -> testAllQuizzes(10, 0,2),
        () -> testAllQuizzes(5, 1,1),
        () -> testAllQuizzes(5, 1,2),
    };

    private CheckResult testRegister(String login, String password, int status) {
        JsonObject json = new JsonObject();
        json.addProperty("email", login);
        json.addProperty("password", password);

        String url = "/api/register";
        HttpRequest req = post(url, getPrettyJson(json));
        HttpResp resp = new HttpResp(req.send(), url, "POST");

        checkStatusCode(resp, status);
        return CheckResult.correct();
    }

    private CheckResult testCreateQuizNoAuth(int quizNum) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResp resp = new HttpResp(req.send(), url, "POST");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCreateQuizFakeAuth(int quizNum) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResp resp = new HttpResp(auth(req, 3).send(), url, "POST");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testSolveQuizNoAuth(int quizNum, String answerSent) {
        String url = "/api/quizzes/" + quizNum + "/solve";
        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResp resp = new HttpResp(req.send(), url, "POST");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testSolveQuizFakeAuth(int quizNum, String answerSent) {
        String url = "/api/quizzes/" + quizNum + "/solve";
        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResp resp = new HttpResp(auth(req, 3).send(), url, "POST");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testAllQuizzesNoAuth() {
        String url = "/api/quizzes";
        HttpResp resp = new HttpResp(get(url).send(), url, "GET");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testAllQuizzesFakeAuth() {
        String url = "/api/quizzes";
        HttpResp resp = new HttpResp(auth(get(url), 3).send(), url, "GET");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testDeleteQuizzesNoAuth(int quizNum) {
        String url = "/api/quizzes/" + quizNum;
        HttpRequest req = delete(url);
        HttpResp resp = new HttpResp(req.send(), url, "DELETE");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testDeleteQuizzesFakeAuth(int quizNum) {
        String url = "/api/quizzes/" + quizNum;
        HttpRequest req = delete(url);
        HttpResp resp = new HttpResp(auth(req, 3).send(), url, "DELETE");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCompletedQuizzesNoAuth() {
        String url = "/api/quizzes/completed";
        HttpRequest req = get(url).setGetParam("page", "0");
        HttpResp resp = new HttpResp(req.send(), url + "?page=0", "GET");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCompletedQuizzesFakeAuth() {
        String url = "/api/quizzes/completed";
        HttpRequest req = get(url).setGetParam("page", "0");
        HttpResp resp = new HttpResp(auth(req, 3).send(), url + "?page=0", "GET");
        checkStatusCode(resp, 401);
        return CheckResult.correct();
    }

    private CheckResult testCreateQuiz(int quizNum, int user) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quizzes[quizNum]);
        HttpResp resp = new HttpResp(auth(req, user).send(), url, "POST");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "id");

        JsonObject obj = json.getAsJsonObject();
        checkIsInt(resp, obj.get("id"), "id");

        quizIds[quizNum] = obj.get("id").getAsInt();

        return CheckResult.correct();
    }

    private CheckResult testQuizExists(int quizNum, int user) {
        int quizId = quizIds[quizNum];
        String quiz = quizzes[quizNum];

        String url = "/api/quizzes/" + quizId;

        HttpRequest req = get(url);
        HttpResp resp = new HttpResp(auth(req, user).send(), url, "GET");

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

    private CheckResult testQuizNotExists(int quizNum, int user, int shift) {
        int quizId = quizIds[quizNum];

        String url = "/api/quizzes/" + (quizId + shift);

        HttpRequest req = get(url);
        HttpResp resp = new HttpResp(auth(req, user).send(), url, "GET");

        checkStatusCode(resp, 404);

        return CheckResult.correct();
    }

    private CheckResult testAllQuizzes(int count, int page, int user) {
        String url = "/api/quizzes";
        HttpRequest req = get(url).setGetParam("page", "" + page);
        HttpResp resp = new HttpResp(auth(req, user).send(), url + "?page=" + page, "GET");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "content");

        JsonObject obj = json.getAsJsonObject();
        JsonElement contentElem = obj.get("content");

        checkIsArray(resp, contentElem);
        checkArrayLength(resp, contentElem, count, "content");

        JsonArray arr = contentElem.getAsJsonArray();
        int index = 0;
        for (JsonElement elem : arr) {
            String path = "content[" + index + "]";
            checkIsObject(resp, elem, path);
            JsonObject currObj = elem.getAsJsonObject();

            checkObjectKey(resp, currObj, "id", path);
            checkIsInt(resp, currObj.get("id"), path + ".id");

            checkObjectKey(resp, currObj, "title", path);
            checkIsString(resp, currObj.get("title"), path + ".title");

            checkObjectKey(resp, currObj, "text", path);
            checkIsString(resp, currObj.get("text"), path + ".text");

            checkObjectKey(resp, currObj, "options", path);
            checkIsArray(resp, currObj.get("options"), path + ".options");

            ++index;
        }

        return CheckResult.correct();
    }

    private CheckResult testCompletedQuizzes(int count, int user, int page, int indexForCheckingQuizId, int quizNum) {
        String url = "/api/quizzes/completed";
        HttpRequest req = get(url).setGetParam("page", "" + page);
        HttpResp resp = new HttpResp(auth(req, user).send(), url + "?page=" + page, "GET");

        checkStatusCode(resp, 200);
        JsonElement json = getJson(resp);

        checkIsObject(resp, json);
        checkObjectKey(resp, json, "content");

        JsonObject obj = json.getAsJsonObject();
        JsonElement contentElem = obj.get("content");

        checkIsArray(resp, contentElem);
        checkArrayLength(resp, contentElem, count, "content");

        JsonArray arr = contentElem.getAsJsonArray();
        int index = 0;
        for (JsonElement elem : arr) {
            String path = "content[" + index + "]";
            checkIsObject(resp, elem, path);
            JsonObject currObj = elem.getAsJsonObject();

            checkObjectKey(resp, currObj, "id", path);
            checkIsInt(resp, currObj.get("id"), path + ".id");

            checkObjectKey(resp, currObj, "completedAt", path);
            checkIsString(resp, currObj.get("completedAt"), path + ".completedAt");

            if (index == indexForCheckingQuizId) {
                checkIntValue(resp, currObj.get("id"), quizNum, path + ".id");
            }

            ++index;
        }

        return CheckResult.correct();
    }

    private CheckResult checkQuizSuccess(int quizNum, String answerSent, boolean shouldResponse, int user) {
        String url = "/api/quizzes/" + quizNum + "/solve";

        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResp resp = new HttpResp(auth(req, user).send(), url, "POST");

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

    private CheckResult addIncorrectQuiz(String quiz, int user) {
        String url = "/api/quizzes";
        HttpRequest req = post(url, quiz);
        HttpResp resp = new HttpResp(auth(req, user).send(), url, "POST");
        checkStatusCode(resp, 400);
        return CheckResult.correct();
    }

    private CheckResult testDelete(int quizNum, int user, int status) {
        String url = "/api/quizzes/" + quizNum;
        HttpRequest req = delete(url);
        HttpResp resp = new HttpResp(auth(req, user).send(), url, "DELETE");
        checkStatusCode(resp, status);
        return CheckResult.correct();
    }

    private CheckResult reloadServer() {
        try {
            reloadSpring();
        } catch (Exception ex) {
            throw new FatalError(ex.getMessage(), ex);
        }
        return CheckResult.correct();
    }
}
