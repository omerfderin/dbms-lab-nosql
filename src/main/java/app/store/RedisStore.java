
package app.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import app.model.Student;
import com.google.gson.Gson;

public class RedisStore {
    static JedisPool jedisPool;
    static Gson gson = new Gson();

    public static void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);  // Artırıldı
        poolConfig.setMaxIdle(20);   // Artırıldı
        poolConfig.setMinIdle(10);   // Artırıldı
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setBlockWhenExhausted(false); // Non-blocking
        poolConfig.setMaxWaitMillis(-1); // Infinite wait
        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 5000); // 5s timeout
        
        // Test connection
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
        }
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
        
        try (Jedis jedis = jedisPool.getResource()) {
            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                String name = firstNames[i % firstNames.length] + " " + lastNames[i % lastNames.length];
                String department = departments[i % departments.length];
                Student s = new Student(id, name, department);
                jedis.set(id, gson.toJson(s));
                if ((i + 1) % 1000 == 0) {
                    System.out.println("Redis: Inserted " + (i + 1) + " records...");
                }
            }
        }
    }

    public static Student get(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(id);
            return json != null ? gson.fromJson(json, Student.class) : null;
        }
    }
}
