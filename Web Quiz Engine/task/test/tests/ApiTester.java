package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;

import static java.lang.Math.abs;

public class ApiTester {

    private static void checkJson(
        boolean passed,
        HttpResp resp,
        JsonElement json,
        String path,
        String should) {

        if (!passed) {
            if (path.length() != 0) {
                path = " in the JSON in path \"" + path + "\"";
            }
            throw new WrongAnswer(
                resp.getRequest() + " should " + should + path +
                    ".\nFound: \n\n" + getPrettyJson(json)
            );
        }
    }

    static String getPrettyJson(JsonElement json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }


    // ========== Object

    static void checkIsObject(HttpResp resp, JsonElement json) {
        checkIsObject(resp, json, "");
    }

    static void checkIsObject(HttpResp resp, JsonElement json, String path) {
        checkJson(json.isJsonObject(), resp, json, path, "contain a JSON object");
    }


    // ========== Array

    static void checkIsArray(HttpResp resp, JsonElement json) {
        checkIsArray(resp, json, "");
    }

    static void checkIsArray(HttpResp resp, JsonElement json, String path) {
        checkJson(json.isJsonArray(), resp, json, path, "contain a JSON array");
    }


    // ========== Null

    static void checkIsNull(HttpResp resp, JsonElement json) {
        checkIsNull(resp, json, "");
    }

    static void checkIsNull(HttpResp resp, JsonElement json, String path) {
        checkJson(json.isJsonNull(), resp, json, path, "be equal to a null");
    }


    // ========== String

    static void checkIsString(HttpResp resp, JsonElement json) {
        checkIsString(resp, json, "");
    }

    static void checkIsString(HttpResp resp, JsonElement json, String path) {
        checkJson(json.isJsonPrimitive() && json.getAsJsonPrimitive().isString(),
            resp, json, path, "contain a string");
    }

    static void checkStringValue(HttpResp resp, JsonElement json, String value, String path) {
        checkIsString(resp, json, path);
        checkJson(json.getAsString().equals(value), resp, json, path, "be equal to \"" + value + "\"");
    }


    // ========== Boolean

    static void checkIsBoolean(HttpResp resp, JsonElement json) {
        checkIsBoolean(resp, json, "");
    }

    static void checkIsBoolean(HttpResp resp, JsonElement json, String path) {
        checkJson(json.isJsonPrimitive() && json.getAsJsonPrimitive().isBoolean(),
            resp, json, path, "contain a boolean");
    }

    static void checkBooleanValue(HttpResp resp, JsonElement json, boolean value, String path) {
        checkIsBoolean(resp, json, path);
        checkJson(json.getAsBoolean() == value, resp, json, path, "be equal to " + value);
    }


    // ========== Int

    static void checkIsInt(HttpResp resp, JsonElement json) {
        checkIsInt(resp, json, "");
    }

    static void checkIsInt(HttpResp resp, JsonElement json, String path) {
        try {
            json.getAsInt();
        } catch (NumberFormatException ex) {
            checkJson(false, resp, json, path,"contain a number");
        }
    }

    static void checkIntValue(HttpResp resp, JsonElement json, int value, String path) {
        checkIsInt(resp, json, path);
        checkJson(json.getAsInt() == value, resp, json, path, "be equal to " + value);
    }


    // ========= Double

    static void checkIsDouble(HttpResp resp, JsonElement json) {
        checkIsDouble(resp, json, "");
    }

    static void checkIsDouble(HttpResp resp, JsonElement json, String path) {
        try {
            json.getAsDouble();
        } catch (NumberFormatException ex) {
            checkJson(false, resp, json, path,"contain a floating-point number");
        }
    }

    static void checkDoubleValue(HttpResp resp, JsonElement json, double value, String path) {
        checkIsDouble(resp, json, path);
        checkJson(abs(json.getAsDouble() - value) < 1e-6, resp, json, path, "be equal to " + value);
    }


    // ========== ObjectKey

    static void checkObjectKey(HttpResp resp, JsonElement json, String key) {
        checkObjectKey(resp, json, key, "");
    }

    static void checkObjectKey(HttpResp resp, JsonElement json, String key, String path) {
        checkIsObject(resp, json, path);
        checkJson(json.getAsJsonObject().has(key),
            resp, json, path, "contain a key \"" + key + "\" in object");
    }


    // ========== ArrayLength

    static void checkArrayLength(HttpResp resp, JsonElement json, int length) {
        checkArrayLength(resp, json, length, "");
    }

    static void checkArrayLength(HttpResp resp, JsonElement json, int length, String path) {
        checkIsArray(resp, json, path);
        checkJson(json.getAsJsonArray().size() == length,
            resp, json, path, "contain a JSON array with length " + length);
    }

}
