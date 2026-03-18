package model;

public class Student extends Person { //ke thua tu thang cha Person

    private String className;  // ma lop
    private double score;      // Diem trung binh tren thang 10

    public Student() {
        super();
        this.className = "";
        this.score = 0.0;
    }

    //constructor day du
    public Student(String id, String name, String className, double score) {
        super(id, name);
        this.className = className;
        this.score = score;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    //ghi de Getinfor cua thang cha Person
    @Override
    public String getInfo() {
        return super.getInfo() + ", Lớp: " + className + ", Điểm: " + String.format("%.1f", score);
    }

    // --- PHAN LOAI HOC LUC ---
    // Dung if-else phan loai theo thang diem 10
    public String getClassification() {
        if (score >= 8.5) {
            return "Giỏi";
        } else if (score >= 7.0) {
            return "Khá";
        } else if (score >= 5.0) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }
}