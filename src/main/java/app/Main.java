
package app;

<<<<<<< HEAD
import com.google.gson.Gson;

import app.model.Student;
import app.store.HazelcastStore;
import app.store.MongoStore;
import app.store.RedisStore;
import static spark.Spark.get;
import static spark.Spark.port;
=======
import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;
>>>>>>> cf73ee49e10c66a79cf3c78bcd3c0e7875a38e3e

public class Main {
    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

<<<<<<< HEAD
        try {
            System.out.println("Initializing Redis...");
            RedisStore.init();
            System.out.println("Redis initialized successfully!");

            System.out.println("Initializing Hazelcast...");
            HazelcastStore.init();
            System.out.println("Hazelcast initialized successfully!");

            System.out.println("Initializing MongoDB...");
            MongoStore.init();
            System.out.println("MongoDB initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Redis endpoint
        get("/nosql-lab-rd/*", (req, res) -> {
            try {
                res.type("application/json");
                String path = req.pathInfo();
                String studentId = path.contains("=") ? path.substring(path.indexOf("=") + 1) : path.substring(path.lastIndexOf("/") + 1);
                Student student = RedisStore.get(studentId);
                if (student == null) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("Student not found"));
                }
                return gson.toJson(student);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new ErrorResponse("Error: " + e.getMessage()));
            }
        });

        // Hazelcast endpoint
        get("/nosql-lab-hz/*", (req, res) -> {
            res.type("application/json");
            String path = req.pathInfo();
            String studentId = path.contains("=") ? path.substring(path.indexOf("=") + 1) : path.substring(path.lastIndexOf("/") + 1);
            return gson.toJson(HazelcastStore.get(studentId));
        });

        // MongoDB endpoint
        get("/nosql-lab-mon/*", (req, res) -> {
            res.type("application/json");
            String path = req.pathInfo();
            String studentId = path.contains("=") ? path.substring(path.indexOf("=") + 1) : path.substring(path.lastIndexOf("/") + 1);
            return gson.toJson(MongoStore.get(studentId));
        });

        System.out.println("Spark server started on http://localhost:8080");
        System.out.println("Endpoints ready:");
        System.out.println("  - http://localhost:8080/nosql-lab-rd/student_no=2025000001");
        System.out.println("  - http://localhost:8080/nosql-lab-hz/student_no=2025000001");
        System.out.println("  - http://localhost:8080/nosql-lab-mon/student_no=2025000001");
    }
    
    static class ErrorResponse {
        String error;
        ErrorResponse(String error) {
            this.error = error;
        }
=======
        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        get("/nosql-lab-rd/ogrenci_no=:id", (req, res) ->
            gson.toJson(RedisStore.get(req.params(":id"))));

        get("/nosql-lab-hz/ogrenci_no=:id", (req, res) ->
            gson.toJson(HazelcastStore.get(req.params(":id"))));

        get("/nosql-lab-mon/ogrenci_no=:id", (req, res) ->
            gson.toJson(MongoStore.get(req.params(":id"))));
>>>>>>> cf73ee49e10c66a79cf3c78bcd3c0e7875a38e3e
    }
}
