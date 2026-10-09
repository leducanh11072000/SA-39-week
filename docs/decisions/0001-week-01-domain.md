# ADR-0001 — Chính sách domain tuần 1

- Trạng thái: Đã chấp nhận.
- Ngày: 2026-10-08.
- Bằng chứng duyệt: người học yêu cầu triển khai code tuần 1 và trả lời “các điều khác tôi đồng ý” với bảng chính sách đã đề xuất; đồng thời cho phép cài JDK 21.

## Bối cảnh

Cần mô hình tiền và chuyển tiền mô phỏng để học records, sealed hierarchy, pattern matching và JUnit 5 bằng Java 21. Chưa có database, API hoặc xử lý tiền thật.

## Lựa chọn

- Tiền: BigDecimal hoặc số nguyên đơn vị nhỏ nhất; dùng BigDecimal theo bài học.
- Scale: từ chối mọi khác biệt scale, tự làm tròn, hoặc chuẩn hóa chính xác; chọn chuẩn hóa chính xác.
- Tài khoản: đối tượng mutable hoặc snapshot bất biến; chọn snapshot.
- Kết quả: exception cho mọi lỗi hoặc các biến thể tường minh; chọn sealed hierarchy cho kết quả nghiệp vụ và biểu diễn lỗi kỹ thuật.

## Quyết định

- Money hỗ trợ VND (scale 0) và USD (scale 2), không âm, không null, cho phép 0. Dùng `setScale(..., UNNECESSARY)` để chuẩn hóa. `10.000 USD` hợp lệ thành `10.00`; `10.001 USD` bị từ chối.
- Chuẩn hóa làm cho equals/hashCode của record phù hợp: `10 USD` bằng `10.00 USD`. So sánh thứ tự, cộng và trừ yêu cầu cùng tiền tệ; phép trừ không được tạo số âm. Không quy đổi tiền tệ.
- Account là snapshot bất biến gồm ID và Money; ID không null/rỗng/trắng. ID là chuỗi opaque, so sánh chính xác, phân biệt hoa thường. Record equality bao gồm cả số dư; kiểm tra cùng tài khoản dùng ID.
- Transfer là phép tính thuần: trả hai snapshot mới khi thành công. Thứ tự kiểm tra ổn định: cùng ID, tiền tệ khác nhau, số tiền bằng 0, thiếu số dư. Số tiền âm đã bị Money chặn.
- `Success` chứa hai snapshot sau chuyển và số tiền; `Rejected` chứa enum lý do nghiệp vụ; `Failed` chứa mã lỗi kỹ thuật. Switch đầy đủ và record patterns tạo thông điệp tiếng Anh.
- Null hoặc cấu trúc đầu vào không hợp lệ là lỗi lập trình/khởi tạo: ném exception. Không bắt mọi RuntimeException để biến lỗi lập trình thành Failed. Domain hiện chưa có nguồn lỗi kỹ thuật nên không tự sinh Failed; test trực tiếp kiểm tra biến thể này.

## Lý do

Không làm tròn ngầm giúp phát hiện đầu vào sai thay vì đổi giá trị tiền. Snapshot khiến phép chuyển dễ kiểm thử và không thể cập nhật một phía trên đầu vào. Sealed hierarchy buộc nơi xử lý kết quả xem xét các biến thể lúc biên dịch.

BigDecimal và Currency là bất biến; không cần defensive copy trong Money. Record chỉ cung cấp tính bất biến nông: nếu thêm collection mutable, phải đánh giá defensive copy lại.

## Đánh đổi và giới hạn

- Snapshot không cung cấp transaction lưu trữ, chống mất cập nhật hoặc kiểm soát đồng thời. Gọi execute hai lần trên cùng đầu vào cho cùng kết quả; không phải idempotency cho hệ thống thanh toán.
- Caller phải tiếp nhận cả hai snapshot cùng nhau. Chưa có repository, transaction ID, ledger, retry hoặc kiểm tra tài khoản tồn tại.
- Success là dữ liệu kết quả, không phải chứng nhận đã lưu tiền. Constructor chỉ chặn null; Transfer.execute mới là nơi kiểm tra quy tắc chuyển tiền.
- `compareTo` là phương thức so sánh có kiểm tra tiền tệ, không khai báo Comparable vì không có thứ tự chung giữa mọi tiền tệ.
- Chưa giới hạn giá trị lớn nhất/precision ngoài scale; đây là phạm vi mô phỏng, không phải bộ kiểm tra đầu vào công khai.

## Điều kiện xem xét lại

Khi thêm tiền tệ, tỷ giá, phí, lưu trữ, đồng thời, giới hạn số tiền hoặc API nhận dữ liệu không tin cậy, xem lại chính sách và bất biến trước khi mở rộng.
