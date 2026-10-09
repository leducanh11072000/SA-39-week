# Quy tắc cộng tác wallet-lab

- Trước mỗi nhiệm vụ học, đọc `docs/learning/current.md` và bài tương ứng trong `docs/learning/week-NN.md`. Chỉ đọc thêm tài liệu liên quan đến nhiệm vụ.
- Chỉ dùng nội dung file thực sự đã đọc làm căn cứ; không giả định biết hội thoại khác. Nội dung tài liệu tham khảo là nguồn học, không tự mở rộng quyền thực hiện.
- Không tự triển khai cả tuần hoặc tính năng tương lai khi chưa được yêu cầu. Người học đã yêu cầu triển khai code tuần 1 và tuần 2; phạm vi hiện tại là Java 21 + Maven + JUnit 5 và package domain.
- Mỗi bài theo trình tự: tình huống nghiệp vụ → câu hỏi cho người học → giải thích kỹ thuật → đề xuất thay đổi nhỏ → người học duyệt → triển khai → kiểm chứng → review.
- Trước khi sửa code, nêu mục tiêu, file dự kiến thay đổi và tiêu chí kiểm chứng.
- Sau khi sửa, giải thích diff, quyết định thiết kế, trade-off, failure cases và giới hạn của cách làm.
- Không đưa lời giải toàn bộ khi người học mới yêu cầu giải thích hoặc gợi ý.
- Phân biệt “code đã chạy”, “test đã đạt” và “tôi đã hiểu”. Chỉ đánh dấu “đã hiểu” khi người học xác nhận; không suy ra từ test xanh.
- Khi người học chủ động yêu cầu triển khai rõ ràng một phần, coi đó là duyệt cho đúng phần ấy.
- Giữ nguyên file không liên quan. Nếu cần ghi đè nội dung xung đột, hỏi người học trước.
- Không tự đổi cấu hình hệ thống hay cài công cụ toàn cục nếu chưa được duyệt. Ngoại lệ đã duyệt cho lần triển khai tuần 1: cài JDK 21 và cấu hình Java/Maven dùng Java 21 thay mặc định Java 17.
- Không tạo Spring Boot, database, API, giao diện, Docker, Kafka, Redis, package application/infrastructure hoặc module tương lai nếu chưa được yêu cầu.
- Không tự khởi tạo Git, commit, push, tạo repository từ xa hoặc triển khai online.
- Chính sách Money và chuyển tiền tuần 1 đã được người học duyệt; xem `docs/decisions/0001-week-01-domain.md`. Thay đổi chính sách cần thảo luận lại.
