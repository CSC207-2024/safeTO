package singleton;

import com.google.gson.Gson;

public class GsonSingleton {
    private static final Gson instance = new Gson();

    private GsonSingleton() {
        // private constructor to prevent instantiation
    }

    public static Gson getGson() {
        return instance;
    }
}
