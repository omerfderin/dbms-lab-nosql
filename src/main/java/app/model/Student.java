
package app.model;

<<<<<<< HEAD
import java.io.Serializable;

public class Student implements Serializable {
    public String student_no;
    public String name;
    public String department;

    public Student(String student_no, String name, String department) {
        this.student_no = student_no;
        this.name = name;
        this.department = department;
=======
public class Student {
    public String ogrenciNo;
    public String adSoyad;
    public String bolum;

    public Student(String ogrenciNo, String adSoyad, String bolum) {
        this.ogrenciNo = ogrenciNo;
        this.adSoyad = adSoyad;
        this.bolum = bolum;
>>>>>>> cf73ee49e10c66a79cf3c78bcd3c0e7875a38e3e
    }
}
