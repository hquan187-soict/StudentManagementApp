package util;

import model.Student;

//thu vien lam viec voi database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

//class ho tro ket noi va thao tac voi database (SQLite)
public class DatabaseHelper {

    //duong dan toi file database
    private static final String DB_URL = "jdbc:sqlite:students.db";

    //lay ket noi toi database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    //tao bang students (chi tao neu chua ton tai)
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS students ("
                + "id TEXT PRIMARY KEY, "
                + "name TEXT NOT NULL, "
                + "class_name TEXT NOT NULL, "
                + "score REAL NOT NULL"
                + ")";

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            stmt.execute(sql);

            stmt.close();
            conn.close();

            System.out.println("[DB] Bang students da san sang.");
        } catch (SQLException e) {
            System.out.println("[DB] Loi tao bang: " + e.getMessage());
        }
    }

    //them sinh vien (INSERT)
    //dung PreparedStatement de tranh SQL Injection
    public static boolean insertStudent(Student student) {
        String sql = "INSERT INTO students (id, name, class_name, score) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            //gan gia tri cho dau ?
            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getClassName());
            pstmt.setDouble(4, student.getScore());

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
            return true;

        } catch (SQLException e) {
            System.out.println("[DB] Loi them SV: " + e.getMessage());
            return false;
        }
    }

    //lay danh sach tat ca sinh vien (SELECT)
    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Student student = new Student(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("class_name"),
                        rs.getDouble("score")
                );
                list.add(student);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("[DB] Loi doc du lieu: " + e.getMessage());
        }

        return list;
    }

    //cap nhat thong tin sinh vien (UPDATE theo id)
    public static boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, class_name = ?, score = ? WHERE id = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getClassName());
            pstmt.setDouble(3, student.getScore());
            pstmt.setString(4, student.getId());

            int rowsAffected = pstmt.executeUpdate();

            pstmt.close();
            conn.close();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("[DB] Loi cap nhat: " + e.getMessage());
            return false;
        }
    }

    //xoa sinh vien theo id (DELETE)
    public static boolean deleteStudent(String id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();

            pstmt.close();
            conn.close();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("[DB] Loi xoa: " + e.getMessage());
            return false;
        }
    }

    //tim kiem sinh vien theo id hoac name (LIKE)
    public static ArrayList<Student> searchStudents(String keyword) {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE id LIKE ? OR name LIKE ?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("class_name"),
                        rs.getDouble("score")
                );
                list.add(student);
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("[DB] Loi tim kiem: " + e.getMessage());
        }

        return list;
    }
}