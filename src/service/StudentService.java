package service;

import model.Student;
import util.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

//lop xu ly logic nghiep vu (trung gian giua UI va database)
public class StudentService {

    private ArrayList<Student> studentList;      // danh sach sinh vien
    private HashMap<String, Student> studentMap; // tim nhanh theo id

    //khoi tao danh sach + load du lieu tu database
    public StudentService() {
        studentList = new ArrayList<>();
        studentMap = new HashMap<>();

        //tao bang neu chua ton tai
        DatabaseHelper.createTable();

        //nap du lieu tu database vao bo nho
        loadFromDatabase();
    }

    //doc du lieu tu database vao list + map
    private void loadFromDatabase() {
        ArrayList<Student> fromDB = DatabaseHelper.getAllStudents();

        for (int i = 0; i < fromDB.size(); i++) {
            Student s = fromDB.get(i);
            studentList.add(s);
            studentMap.put(s.getId(), s); // key = id
        }

        System.out.println("[Service] Da load " + studentList.size() + " SV tu database.");
    }

    //them sinh vien
    //kiem tra trung id bang HashMap (nhanh)
    public boolean addStudent(Student student) {
        if (studentMap.containsKey(student.getId())) {
            return false; // trung id
        }

        //luu vao database
        boolean savedToDB = DatabaseHelper.insertStudent(student);
        if (!savedToDB) {
            return false; // loi DB
        }

        //them vao bo nho
        studentList.add(student);
        studentMap.put(student.getId(), student);
        return true;
    }

    //sua thong tin sinh vien theo id
    public boolean editStudent(String id, String newName, String newClassName, double newScore) {
        //tim nhanh bang HashMap
        Student student = studentMap.get(id);
        if (student == null) {
            return false;
        }

        //cap nhat trong bo nho
        //student la tham chieu nen list tu dong cap nhat
        student.setName(newName);
        student.setClassName(newClassName);
        student.setScore(newScore);

        //dong bo xuong database
        DatabaseHelper.updateStudent(student);
        return true;
    }

    //xoa sinh vien theo id
    public boolean deleteStudent(String id) {
        Student student = studentMap.get(id);
        if (student == null) {
            return false;
        }

        //xoa trong database
        DatabaseHelper.deleteStudent(id);

        //xoa trong bo nho
        studentList.remove(student);
        studentMap.remove(id);
        return true;
    }

    //tim theo id (O(1) voi HashMap)
    public Student findById(String id) {
        return studentMap.get(id);
    }

    //tim theo ten (duyet list)
    //khong phan biet hoa/thuong
    public ArrayList<Student> findByName(String keyword) {
        ArrayList<Student> result = new ArrayList<>();
        for (int i = 0; i < studentList.size(); i++) {
            Student s = studentList.get(i);
            if (s.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    //lay toan bo danh sach
    public ArrayList<Student> getAllStudents() {
        return studentList;
    }
}