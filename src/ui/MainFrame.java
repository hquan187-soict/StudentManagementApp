package ui;

import model.Student;
import service.StudentService;

// Thu vien Swing - tao giao dien desktop
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Thu vien AWT - bo tri layout
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;


public class MainFrame extends JFrame {

    // Service xu ly logic (se goi toi DatabaseHelper ben trong)
    private StudentService studentService;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtClass;
    private JTextField txtScore;
    private JTextField txtSearch;

    public MainFrame() {
        // Tao service -> tu dong ket noi DB va load du lieu
        studentService = new StudentService();

        setTitle("Hệ thống quản lí sinh viên");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLocationRelativeTo(null) = hien thi giua man hinh
        setLocationRelativeTo(null);

        // Tao giao dien
        initUI();

        // Load du lieu tu DB hien thi len bang
        refreshTable();
    }

    // ==========================================
    // TAO GIAO DIEN
    // ==========================================
    private void initUI() {

        // --- PANEL CHINH ---
        // BorderLayout: chia cua so thanh 5 vung (NORTH, SOUTH, EAST, WEST, CENTER)
        // Tham so (10, 10) = khoang cach 10px giua cac vung
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 8, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        // Hang 1
        inputPanel.add(new JLabel("MSSV:"));
        txtId = new JTextField();
        inputPanel.add(txtId);

        inputPanel.add(new JLabel("Họ tên:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        // Hang 2
        inputPanel.add(new JLabel("Lớp:"));
        txtClass = new JTextField();
        inputPanel.add(txtClass);

        inputPanel.add(new JLabel("Điểm"));
        txtScore = new JTextField();
        inputPanel.add(txtScore);
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        //phan bang danh sach
        String[] columns = {"MSSV", "Họ tên", "Lớp", "Điểm", "Xếp loại"};
        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) { //khong cho moi nguoi sua tren bang
                return false;
            }
        };

        table = new JTable(tableModel);
        // Chi cho phep chon 1 dong cung luc
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Lang nghe su kien bam vao la co the sua ngay io tren bang
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                // getValueAt(row, cot) lay gia tri tai o (dong, cot)
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtClass.setText(tableModel.getValueAt(row, 2).toString());
                txtScore.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        // JScrollPane:cuon khi nhieu du lieu
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER); //tu dong gian dua dong

        //cac nut bam
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        JButton btnAdd = new JButton("Thêm SV");
        JButton btnEdit = new JButton("Sửa SV");
        JButton btnDelete = new JButton("Xóa SV");
        JButton btnClear = new JButton("Làm mới");
        JButton btnShowAll = new JButton("Hiện tất cả");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnShowAll);

        // Thanh tim kiem
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchPanel.add(new JLabel("Tìm kiếm(Tên/MSSV):"));
        txtSearch = new JTextField(15);  // Do rong 15 ky tu
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("Tìm");
        searchPanel.add(btnSearch);

        // Gop nut + tim kiem vao 1 panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(searchPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Them panel chinh vao JFrame (cua so)
        add(mainPanel);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        btnShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
    }

    //them sinh vien
    private void addStudent() {
        // .trim() = bo khoang trang dau cuoi, vd: "  SV001  " -> "SV001"
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String className = txtClass.getText().trim();
        String scoreStr = txtScore.getText().trim();

        // Kiem tra khong duoc de trong
        if (id.isEmpty() || name.isEmpty() || className.isEmpty() || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Kiem tra diem co phai la so hop le khong
        double score;
        try {
            score = Double.parseDouble(scoreStr);
            if (score < 0 || score > 10) {
                JOptionPane.showMessageDialog(this, "Điểm sinh viên phải nằm trong khoảng từ 0 đến 10");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm sai định dạng. ");
            return;
        }

        // Tao object Student bang Constructor
        Student student = new Student(id, name, className, score);

        // Goi service -> luu DB + ArrayList + HashMap
        boolean success = studentService.addStudent(student);

        if (success) {
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Bạn đã thêm sinh viên thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "MSSV này đã tồn tại");
        }
    }

    //sua thong tin sv
    private void editStudent() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên trên bảng để sửa!");
            return;
        }

        String name = txtName.getText().trim();
        String className = txtClass.getText().trim();
        String scoreStr = txtScore.getText().trim();

        if (name.isEmpty() || className.isEmpty() || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đẩy đủ thông tin !");
            return;
        }

        double score;
        try {
            score = Double.parseDouble(scoreStr);
            if (score < 0 || score > 10) {
                JOptionPane.showMessageDialog(this, "Điểm sinh viên phải nằm trong khoảng từ 0 đến 10");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm sai định dạng!");
            return;
        }

        boolean success = studentService.editStudent(id, name, className, score);
        if (success) {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy MSSS: " + id);
        }
    }

    //xoa sv
    private void deleteStudent() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Sinh Viên muốn xóa!");
            return;
        }

        // showConfirmDialog: hop thoai xac nhan Yes/No
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sinh viên với MSSV: " + id + "không ?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentService.deleteStudent(id);
            if (success) {
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên!");
            }
        }
    }

    //tim kiem ttin ve sinh vien
    private void searchStudent() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập từ khóa tìm kiếm");
            return;
        }

        // Thu 1: Tim theo MSSV (HashMap - nhanh O(1))
        Student found = studentService.findById(keyword);
        if (found != null) {
            tableModel.setRowCount(0);
            addStudentToTable(found);
            return;
        }

        // Thu 2: Tim theo ten (duyet ArrayList)
        ArrayList<Student> results = studentService.findByName(keyword);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên hợp lệ!");
        } else {
            tableModel.setRowCount(0);
            for (int i = 0; i < results.size(); i++) {
                addStudentToTable(results.get(i));
            }
        }
    }


    // Cap nhat toan bo bang tu danh sach
    private void refreshTable() {
        tableModel.setRowCount(0);  // Xoa het dong cu
        ArrayList<Student> list = studentService.getAllStudents();
        for (int i = 0; i < list.size(); i++) {
            addStudentToTable(list.get(i));
        }
    }

    // Them 1 dong sinh vien vao bang
    private void addStudentToTable(Student s) {
        Object[] row = {
                s.getId(),
                s.getName(),
                s.getClassName(),
                String.format(java.util.Locale.US, "%.1f", s.getScore()),
                s.getClassification()
        };
        tableModel.addRow(row);
    }

    // Xoa toan bo form nhap lieu
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtClass.setText("");
        txtScore.setText("");
        txtSearch.setText("");
        table.clearSelection();
    }
}