
package app.store;

import com.mongodb.client.*;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        client = MongoClients.create("mongodb://localhost:27017"); // bağlantı adresi burada
        collection = client.getDatabase("nosqllab").getCollection("students");
        collection.drop(); // eski kayıtları temizle
        String[] departments = {
            "Classical Turkish Music",
            "Turkish Folk Music",
            "Computer Science",
            "Electrical Engineering",
            "Mechanical Engineering",
            "Civil Engineering",
            "Mathematics",
            "Physics",
            "Chemistry",
            "Biology"
        };
        String[] firstNames = {
            "Münip", "Nağme", "Aysun", "Ahmet", "Mehmet", "Ayşe", "Fatma", "Ali", "Veli", "Zeynep",
            "Emre", "Deniz", "Cem", "Elif", "Burak", "Can", "Ege", "Arda", "Selin", "Berk"
        };
        String[] lastNames = {
            "Utandı", "Yarkın", "Gültekin", "Yılmaz", "Kaya", "Demir", "Şahin", "Çelik", "Yıldız", "Arslan",
            "Öztürk", "Doğan", "Kılıç", "Aydın", "Özdemir", "Polat", "Özkan", "Çetin", "Kurt", "Şimşek"
        };
        
        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            String name = firstNames[i % firstNames.length] + " " + lastNames[i % lastNames.length];
            String department = departments[i % departments.length];
            Student s = new Student(id, name, department);
            collection.insertOne(Document.parse(gson.toJson(s)));
            if ((i + 1) % 1000 == 0) {
                System.out.println("MongoDB: Inserted " + (i + 1) + " records...");
            }
        }
    }

    public static Student get(String id) {
        Document doc = collection.find(new Document("student_no", id)).first();
        return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
    }
}
