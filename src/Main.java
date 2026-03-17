import model.Person;
import model.Student;
import service.StudentService;

public class Main {
    public static void main(String[] args) {

        // Tao object Person (lop cha)
        Person person = new Person("001", "Nguyen Van A");
        System.out.println("Person.getInfo(): " + person.getInfo());

        // Tao object Student (lop con)
        Student student = new Student("SV001", "Tran Van B", "CNTT-K68", 8.5);
        System.out.println("Student.getInfo(): " + student.getInfo());

        Person p = new Student("SV002", "Le Thi C", "DTVT-K68", 7.2);
        System.out.println("Polymorphism:     " + p.getInfo());

        System.out.println("Xep loai SV001: " + student.getClassification());
        System.out.println();

        StudentService service = new StudentService();

        // THEM 3 sinh vien
        System.out.println("\n[Them sinh vien]");
        service.addStudent(new Student("SV001", "Nguyen Van Nam", "CNTT-K68", 8.7));
        service.addStudent(new Student("SV002", "Tran Thi Lan", "CNTT-K68", 7.5));
        service.addStudent(new Student("SV003", "Le Hoang Minh", "DTVT-K68", 6.2));

        // Thu them trung MSSV
        boolean trung = service.addStudent(new Student("SV001", "Ai Do", "ABC", 5.0));
        System.out.println("Them trung SV001: " + (trung ? "Thanh cong" : "That bai (da ton tai)"));

        // HIEN THI tat ca
        System.out.println("\n[Danh sach sinh vien]");
        for (int i = 0; i < service.getAllStudents().size(); i++) {
            Student s = service.getAllStudents().get(i);
            System.out.printf("  %s | %-20s | %-10s | %.1f | %s%n",
                    s.getId(), s.getName(), s.getClassName(),
                    s.getScore(), s.getClassification());
        }

        // TIM KIEM theo MSSV (HashMap)
        System.out.println("\n[Tim theo MSSV: SV002]");
        Student found = service.findById("SV002");
        if (found != null) {
            System.out.println("  Tim thay: " + found.getInfo());
        }

        // TIM KIEM theo ten (ArrayList)
        System.out.println("\n[Tim theo ten: 'nam']");
        for (Student s : service.findByName("nam")) {
            System.out.println("  Ket qua: " + s.getInfo());
        }

        // SUA thong tin
        System.out.println("\n[Sua SV003: doi diem tu 6.2 -> 9.0]");
        service.editStudent("SV003", "Le Hoang Minh", "DTVT-K68", 9.0);
        Student sv3 = service.findById("SV003");
        System.out.println("  Sau khi sua: " + sv3.getInfo() + " | " + sv3.getClassification());

        // XOA
        System.out.println("\n[Xoa SV002]");
        service.deleteStudent("SV002");
        System.out.println("  Con lai " + service.getAllStudents().size() + " sinh vien.");

        // HIEN THI lai sau khi sua/xoa
        System.out.println("\n[Danh sach cuoi cung]");
        for (int i = 0; i < service.getAllStudents().size(); i++) {
            Student s = service.getAllStudents().get(i);
            System.out.printf("  %s | %-20s | %-10s | %.1f | %s%n",
                    s.getId(), s.getName(), s.getClassName(),
                    s.getScore(), s.getClassification());
        }

        System.out.println("File students.db da duoc tao trong thu muc project.");
        System.out.println("Chay lai app se thay du lieu van con (persistent).");
    }
}