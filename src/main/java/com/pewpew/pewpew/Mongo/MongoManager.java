package com.pewpew.pewpew.mongo;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

public class MongoManager {
    private static MongoModule mongoModule = MongoModule.getInstanse();

//    @Nullable
    public static User getUser(String email) {
        return mongoModule.provideDatastore().find(
                User.class, "email", email).get();
    }
    public static User getUser(String email, String password) {
        return mongoModule.provideDatastore().find(
                User.class, "email", email).field("password").equal(password).get();
    }
    public static User getUser(ObjectId userId) {
        return mongoModule.provideDatastore().find(User.class, "_id", userId).get();
    }

//    @NotNull
    public static Boolean userExist(User newUser) {
        User user = getUser(newUser.getEmail());
        return user == null;
    }
}
