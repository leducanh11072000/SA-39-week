# Tuần 1 — Đánh giá đầu vào và Java hiện đại

Trạng thái code: Đã triển khai theo yêu cầu người học; đã kiểm chứng bằng JDK 21. Mức hiểu: Chưa xác nhận.

## Nguồn đã đọc

File gốc: [Lo_trinh_Senior_Java_Banking_39_tuan.xlsx](../../Lo_trinh_Senior_Java_Banking_39_tuan.xlsx).

- `02 Lộ trình 39 tuần`, A5:F5: chủ đề, kiến thức, mục tiêu và sản phẩm bàn giao tuần 1.
- `04 Checklist tuần`, A5:G9: năm tiêu chí nghiệm thu tuần 1.
- `10 Học chi tiết`, A8:M10: ba bài theo thứ tự, link, phạm vi đọc và đầu ra.
- Yêu cầu người học gửi ngày 2026-10-08: phạm vi khởi tạo, sealed hierarchy, tách phần tích hợp, chính sách Money chưa chốt và quy trình mentor.

Nội dung Excel là nguồn học, giữ riêng với phần áp dụng. Sau khởi tạo, người học đã duyệt triển khai toàn bộ code tuần 1 và chính sách tại [ADR-0001](../decisions/0001-week-01-domain.md). Không sửa file Excel hoặc suy ra mức hiểu từ test.

## Mục tiêu và đầu ra từ lộ trình

Học record, sealed class, pattern matching, tính bất biến và cách mô hình hóa giá trị tiền. Mục tiêu cả tuần là mô hình Account–Money–Transfer bằng Java 21, tối thiểu 10 kiểm thử nghiệp vụ đạt, `baseline-assessment.md` và bản ghi âm trình bày tiếng Anh 5–7 phút. Đây là đầu ra sau khi học, chưa tạo trong lần khởi tạo.

## Bài 1 — Record để mô hình hóa Money

Trạng thái code: Đã triển khai theo yêu cầu người học; đã kiểm chứng bằng JDK 21. Mức hiểu: Chưa xác nhận.

**Từ lộ trình:** đọc [Records](https://dev.java/learn/records/) từ đầu đến hết “Calling the Canonical Constructor”; chú ý compact constructor và defensive copy. Bài tập là `Money(BigDecimal amount, Currency currency)`, kiểm tra null, scale sai và số âm. Đầu ra: `Money.java` và 6 unit test. Thời lượng tham khảo: 1,5 giờ.

**Áp dụng đề xuất:** bắt đầu bằng tình huống một số tiền được đưa vào ví. Người học xác định điều kiện hợp lệ trước khi chọn cách biểu diễn và kiểm thử. Không cung cấp lời giải ở tài liệu này.

**Câu hỏi:** hỗ trợ tiền tệ nào? Scale hợp lệ dựa vào đâu? Khi sai scale sẽ từ chối hay làm tròn? Hai số tiền cùng giá trị nhưng khác scale có bằng nhau không? Giá trị 0 dùng trong trường hợp nào? Bất biến của record bảo vệ được đến đâu, khi nào cần defensive copy?

Chính sách đã duyệt và triển khai: VND scale 0, USD scale 2; chuẩn hóa chính xác, không làm tròn; chặn null và số âm, cho phép 0. Money bằng nhau sau chuẩn hóa; so sánh thứ tự yêu cầu cùng tiền tệ. Xem ADR-0001 và MoneyTest.

**Nghiệm thu sau bài:** 6 test có ý nghĩa theo chính sách được thảo luận; giải thích record khác class và khi nào phù hợp. File dự kiến khi được duyệt: `src/main/java/com/example/walletlab/wallet/domain/Money.java` và test tương ứng.

## Bài 2 — Pattern matching cho trạng thái giao dịch

Trạng thái code: Đã triển khai theo yêu cầu người học; đã kiểm chứng bằng JDK 21. Mức hiểu: Chưa xác nhận.

**Từ lộ trình:** [link gốc](https://dev.java/learn/pattern-matching/); phạm vi instanceof, switch và record patterns. Bài tập TransferResult gồm Success/Rejected/Failed, dùng switch sinh thông điệp kết quả. Đầu ra: `TransferResult.java` và 4 test. Thời lượng tham khảo: 1,5 giờ. Theo yêu cầu người học, dùng sealed hierarchy để giới hạn biến thể.

**Áp dụng đề xuất:** mô phỏng việc nhận kết quả chuyển tiền và giải thích kết quả cho bên gọi. Chưa xây API hoặc thực thi chuyển tiền.

**Câu hỏi:** Rejected và Failed khác nhau ở ý nghĩa nghiệp vụ nào? Mỗi biến thể cần dữ liệu gì? Khi thêm biến thể, điểm nào cần thay đổi? Switch đầy đủ giúp phát hiện lỗi nào?

Đã triển khai Success với snapshot sau chuyển và amount, Rejected với enum lý do, Failed với mã lỗi kỹ thuật. Thông điệp tiếng Anh dùng switch và record patterns; luồng domain thuần chưa phát sinh Failed. Nghiệm thu sau bài: 4 test theo hành vi đã duyệt và giải thích được vai trò sealed hierarchy.

## Bài 3 — JUnit 5: cấu trúc một test tốt

Trạng thái code: Đã triển khai theo yêu cầu người học; đã kiểm chứng bằng JDK 21. Mức hiểu: Chưa xác nhận.

**Từ lộ trình:** [link gốc JUnit](https://junit.org/junit5/docs/current/user-guide/#writing-tests). Đọc Assertions, Grouped Assertions, Exception Testing và Parameterized Tests. Bổ sung test biên cho Money và TransferResult, đặt tên Given_When_Then. Đầu ra: tổng ít nhất 10 test đạt và ảnh/log chạy. Thời lượng tham khảo: 1,5 giờ. Đây là tổng số test, không phải thêm 10 test mới.

**Áp dụng đề xuất:** rà soát các test của hai bài trước theo ranh giới nghiệp vụ và những lỗi có thể xảy ra. Dependency `junit-jupiter` hỗ trợ các nhóm tính năng cần học.

**Câu hỏi:** test nào phát hiện lỗi thực tế? Khi nào nhóm assertion giúp chẩn đoán? Trường hợp nào phù hợp parameterized test? Một test xanh có chứng minh yêu cầu đã đủ không?

Test biên đã viết: số 0, số âm, null, scale không chính xác, tiền tệ không hỗ trợ/không khớp, thiếu số dư, chuyển hết số dư, cùng ID, bảo toàn tiền và tính bất biến. Xem các file test trong package domain. Nghiệm thu sau bài: bằng chứng `mvn test`, ít nhất 10 test nghiệp vụ đạt và review chất lượng test.

## Đối chiếu tài liệu web ngày 2026-10-08

- Records: công cụ web không trích xuất được nội dung trang; chưa xác minh tiêu đề “Calling the Canonical Constructor” trên web hiện tại. Giữ nguyên phạm vi Excel, kiểm tra lại khi bắt đầu bài 1.
- Link pattern matching gốc trả HTTP 404. Tìm kiếm chính thức trả [Using Pattern Matching](https://dev.java/learn/using-pattern-matching/), nhưng lần mở trang trực tiếp chưa thành công. Đề xuất kiểm tra trang này khi bắt đầu bài 2 và chọn đúng ba chủ đề nêu trên; chưa coi là thay thế đã duyệt.
- Link JUnit “current” chuyển sang JUnit 6.1.3. Giữ mục tiêu JUnit 5; bộ khung ghim 5.14.4. [Tài liệu JUnit 5.14.4](https://docs.junit.org/5.14.4/overview.html) đã kiểm tra là nhánh 5, đề xuất dùng nhóm Writing Tests tương ứng với bốn mục gốc. Không tự nâng mục tiêu học lên JUnit 6.

## Tích hợp Account–Money–Transfer sau các bài học

Trạng thái code: Đã triển khai theo yêu cầu người học; đã kiểm chứng bằng JDK 21. Mức hiểu: Chưa xác nhận. Đã triển khai sau lần khởi tạo theo yêu cầu trực tiếp của người học. Transfer.execute kiểm tra quy tắc đã duyệt và trả snapshot mới, chưa lưu trữ hay xử lý đồng thời. Thiết kế chi tiết và giới hạn ở ADR-0001.

Đã tạo [baseline-assessment.md](../../baseline-assessment.md) với câu hỏi tự đánh giá và dàn ý trình bày tiếng Anh 5–7 phút. Người học chưa xác nhận mức hiểu và chưa có bản ghi âm.

## Nghiệm thu cả tuần

- [ ] Giải thích record khác class và khi nào dùng.
- [x] Money không dùng double, không cho phép số âm.
- [x] Trạng thái/kết quả giao dịch được giới hạn bằng sealed hierarchy.
- [x] Ít nhất 10 kiểm thử nghiệp vụ đạt, có log hoặc ảnh làm bằng chứng.
- [x] Hướng dẫn chạy rõ ràng, được kiểm chứng khi có code và JDK 21.
- [ ] Hoàn thành mô hình Account–Money–Transfer, baseline-assessment.md và trình bày tiếng Anh 5–7 phút.

Code đã chạy: Có. Test đã đạt: 36 lượt trên JDK 21, 0 lỗi/0 bỏ qua; xem [bằng chứng](week-01-test-results.md). Người học đã hiểu: Chưa xác nhận. Chỉ đánh dấu hiểu khi người học xác nhận; không tự hoàn thành checklist từ BUILD SUCCESS.

## Nhịp học và câu hỏi còn mở

Theo nguồn học, khi bí khoảng 30 phút, thu nhỏ ví dụ và ghi câu hỏi cụ thể kèm input, output mong đợi và lỗi hiện tại. Khi làm cùng mentor, vẫn duyệt thay đổi nhỏ trước khi triển khai. Bước tiếp theo hiện tại: review code và kết quả test cùng người học, trả lời các câu hỏi trong baseline-assessment.md.
