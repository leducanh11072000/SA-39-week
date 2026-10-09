# Tầm nhìn sản phẩm

Ví và chuyển tiền mô phỏng là sản phẩm chính. Dự án giúp người học đã có kinh nghiệm Java/Spring hiểu kỹ thuật giải quyết vấn đề gì, giải thích quyết định thiết kế, đánh đổi và tình huống thất bại. Không xử lý tiền thật.

## Phạm vi hiện tại đã duyệt

Java 21 + Maven + JUnit 5, một project `wallet-lab`, base package `com.example.walletlab`, package domain. Người học đã duyệt và yêu cầu triển khai code tuần 1: Money, Account, Transfer, TransferResult và kiểm thử. Đây là mô hình chuyển tiền thuần trong bộ nhớ, chưa có ứng dụng tương tác hoặc lưu trữ.

## Hướng sản phẩm dài hạn

Mô hình hóa tiền, tài khoản và chuyển tiền mô phỏng là hướng học ban đầu. Quản lý tài chính cá nhân là hướng mở rộng sau. Lộ trình 39 tuần định hướng học; không phải cam kết triển khai toàn bộ ngay lúc khởi tạo.

Phạm vi MVP nghiệp vụ cụ thể, người dùng, giao diện và cách lưu trữ: Chưa quyết định. Công nghệ cho hạ tầng và các module tương lai: Chưa quyết định. Chỉ bổ sung khi bài học và yêu cầu sản phẩm tạo ra nhu cầu rõ ràng.

## Các câu hỏi để thảo luận đúng thời điểm

- Chính sách Money và bất biến chuyển tiền đã chốt tại [ADR-0001](../decisions/0001-week-01-domain.md).
- Lưu trữ và bảo vệ bất biến khi có nhiều yêu cầu đồng thời: Chưa quyết định.
- Phần nào đủ để chứng minh bài học đã áp dụng được? Dùng tiêu chí tuần học và xác nhận của người học.
