
package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import app.model.Student;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        hz = HazelcastClient.newHazelcastClient(); // config dosyasına bağlanır
        map = hz.getMap("students");
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
            map.put(id, s);
            if ((i + 1) % 1000 == 0) {
                System.out.println("Hazelcast: Inserted " + (i + 1) + " records...");
            }
        }
    }

    public static Student get(String id) {
        return map.get(id);
    }
}
