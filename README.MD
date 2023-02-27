## Project Sodini

#  Giới thiệu 

- Project Sodini là dự án cá nhân được thực hiện bởi `vn-vna` để làm tư liệu cho
bài tập cuối khóa của CLB Công nghệ Thông Tin Trường Đại học Kinh tế Quốc Dân (NITC)

- Dự án là được triển khai bằng ngôn ngữ lập trình Java, cùng với build tool 
`gradle-kts`

- Dự án được triển khai trên ý tưởng là một bot discord được xây dựng với mục đích tổng quát 
hóa và sử dụng các kiến thức học được từ khóa học Java của Câu lạc bộ NITC và 
các vấn đề thực tiễn.

# Tính năng

1. Tính năng codebase
  - Sử dụng thư viện JDA nổi tiếng để thiết kế và tương tác với Discord API
  - Sử dụng thư viện lombok tăng tính hiệu quả của code. 
  - Sử dụng Dependency Injection trên toàn bộ codebase giúp quá trình cải tạo và
    mở rộng dự án trở nên dễ dàng hơn
  - Thiết kế các module rõ ràng, riêng biệt giúp cấu trúc tổng thể dễ dàng sửa 
    chữa khi gặp lỗi và dễ dàng thao tác trên codebase hơn.

2. Tính năng bot mẫu

|OK|Tính năng|
|---|---|
|V|Xem avatar thành viên|
|V|Xem thông tin thành viên|
|V|Timeout thành viên|
|X|Tìm anime|
|X|Tìm nguồn ảnh|

# Cách sử dụng

1. Chuẩn bị
  - Java 17 trở lên
  - Gradle
  - Inteliji IDEA
  - Một bot (và token của bot)
  - Một file `sodini.xml` được viết theo mẫu `src/main/resources/sodini-template.xml`

2. Thực thi
  - Sử dụng Inteliji IDEA mở project
  - Copy `sodini.xml` vào vị trí `src/main/resources/sodini.xml`
  - Click run trên cửa sổ Inteliji IDEA
