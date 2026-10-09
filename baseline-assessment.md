# Baseline assessment — Tuần 1

## Điểm xuất phát

Người học cho biết đã có kinh nghiệm Java/Spring; chưa có bài đánh giá trực tiếp để suy ra mức độ thành thạo. Mục tiêu là giải thích bản chất, đánh đổi và failure cases.

## Đầu ra code

- Money: record, compact constructor, BigDecimal, chuẩn hóa scale chính xác.
- Account: snapshot bất biến; Transfer: phép tính thuần bảo toàn tiền khi cùng currency.
- TransferResult: sealed hierarchy, switch đầy đủ và record patterns.
- JUnit 5: assertions, assertAll, assertThrows, parameterized tests và tên Given_When_Then.
- Chính sách đã duyệt: [ADR-0001](docs/decisions/0001-week-01-domain.md).

## Bằng chứng

Đã biên dịch và chạy trên Temurin JDK 21.0.12.1. Có 36 lượt kiểm thử đạt, không lỗi hoặc bỏ qua. [Bằng chứng chạy](docs/learning/week-01-test-results.md) tổng hợp từ báo cáo `target/surefire-reports/`. Kết quả này không thay thế xác nhận mức hiểu của người học.

## Người học tự đánh giá

Chưa xác nhận đã hiểu. Các câu hỏi dành cho buổi review:

1. Vì sao record không tự bảo đảm tính bất biến sâu? Vì sao Money hiện không cần defensive copy?
2. Vì sao chuẩn hóa scale ảnh hưởng equals/hashCode của BigDecimal trong record?
3. Vì sao `UNNECESSARY` chấp nhận `10.000 USD` nhưng từ chối `10.001 USD`?
4. Khi nào chuyển tiền bị Rejected, khi nào một hệ thống có thể trả Failed?
5. Vì sao snapshot bất biến vẫn không giải quyết chuyển tiền đồng thời hoặc transaction database?
6. Test nào bảo vệ bảo toàn tổng tiền, test nào phân biệt lỗi nghiệp vụ với lỗi lập trình?

## Trình bày tiếng Anh 5–7 phút

Chưa có bản ghi âm hoặc bằng chứng người học trình bày. Gợi ý dàn ý để tự chuẩn bị:

- 0:00–1:00: explain the simulated wallet and week-one scope.
- 1:00–2:30: explain Money invariants, normalization, and record equality.
- 2:30–4:00: walk through a successful transfer and an insufficient-funds rejection.
- 4:00–5:30: explain sealed results, exhaustive switches, and meaningful tests.
- 5:30–6:30: discuss persistence, concurrency, and what this model cannot guarantee.

## Bước tiếp theo

Người học review và giải thích từng quyết định, chọn một failure case để tự bổ sung test. Chỉ đánh dấu hiểu sau xác nhận; chưa mở rộng sang tuần 2.
