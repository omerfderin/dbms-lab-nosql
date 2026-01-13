package app;

import com.google.gson.Gson;

import app.model.Student;
import app.store.HazelcastStore;
import app.store.MongoStore;
import app.store.RedisStore;
import static spark.Spark.get;
import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        port(8080);
        Gson gson = new Gson();

        try {
            System.out.println("Redis başlatılıyor...");
            RedisStore.init();
            System.out.println("Redis başarıyla başlatıldı!");

            System.out.println("Hazelcast başlatılıyor...");
            HazelcastStore.init();
            System.out.println("Hazelcast başarıyla başlatıldı!");

            System.out.println("MongoDB başlatılıyor...");
            MongoStore.init();
            System.out.println("MongoDB başarıyla başlatıldı!");
        } catch (Exception e) {
            System.err.println("Başlatma hatası: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        get("/nosql-lab-rd/*", (req, res) -> {
            try {
                res.type("application/json");
                String path = req.pathInfo();
                String studentId = path.contains("=") ? path.substring(path.indexOf("=") + 1) : path.substring(path.lastIndexOf("/") + 1);
                Student student = RedisStore.get(studentId);
                if (student == null) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("Öğrenci bulunamadı"));
                }
                return gson.toJson(student);
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new ErrorResponse("Hata: " + e.getMessage()));
            }
        });

        get("/nosql-lab-hz/*", (req, res) -> {
            res.type("application/json");
            String path = req.pathInfo();
            String studentId = path.contains("=") ? path.substring(path.indexOf("=") + 1) : path.substring(path.lastIndexOf("/") + 1);
            return gson.toJson(HazelcastStore.get(studentId));
        });

        get("/nosql-lab-mon/*", (req, res) -> {
            res.type("application/json");
            String path = req.pathInfo();
            String studentId = path.contains("=") ? path.substring(path.indexOf("=") + 1) : path.substring(path.lastIndexOf("/") + 1);
            return gson.toJson(MongoStore.get(studentId));
        });

        System.out.println("Spark sunucusu http://localhost:8080 adresinde başlatıldı");
        System.out.println("Hazır endpoint'ler:");
        System.out.println("  - http://localhost:8080/nosql-lab-rd/student_no=2025000001");
        System.out.println("  - http://localhost:8080/nosql-lab-hz/student_no=2025000001");
        System.out.println("  - http://localhost:8080/nosql-lab-mon/student_no=2025000001");
    }
    
    static class ErrorResponse {
        String error;
        ErrorResponse(String error) {
            this.error = error;
        }
    }
}
