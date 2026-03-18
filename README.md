# Hệ thống Quản lý Học viên

Đồ án cuối khóa môn Lập trình Java.

## Mô tả

Ứng dụng desktop quản lý sinh viên sử dụng Java Swing + SQLite Database.

Chức năng chính:
- Thêm / Sửa / Xóa sinh viên
- Tìm kiếm theo tên hoặc MSSV
- Tự động xếp loại học lực (Giỏi, Khá, Trung bình, Yếu)
- Lưu dữ liệu vào SQLite database


## Cách chạy

1. Mở project bằng IntelliJ IDEA(hoặc VSCode, Esclipse,v.v..)
2. Thêm `sqlite-jdbc.jar` trong thư mục `lib/` vào Libraries (Trên IntelliJ IDEAFile → Project Structure → Modules → Dependencies → + → JARs)
3. Chạy file `Main.java`

## Thư viện sử dụng

- SQLite JDBC Driver 3.51.3.0