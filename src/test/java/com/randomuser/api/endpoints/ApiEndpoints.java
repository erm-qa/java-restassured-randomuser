package com.randomuser.api.endpoints;

public class ApiEndpoints {
    public static final String USERS = "/";
    public static final String USERS_WITH_PARAMS = USERS + "?results={results}";
    public static final String USERS_WITH_GENDER = USERS + "?gender={gender}";
    public static final String USERS_WITH_NATIONALITY = USERS + "?nat={nat}";
    public static final String USERS_WITH_SEED = USERS + "?seed={seed}";
}
