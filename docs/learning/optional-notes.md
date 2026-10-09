# Optional đúng chỗ

Nguồn: Excel `10 Học chi tiết!A13:M13` và [Optional Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html).

`TransactionAnalytics.findById` dùng `Optional.ofNullable(byId.get(id))`: không tìm thấy là kết quả hợp lệ. Caller chọn fallback hoặc `orElseThrow`. Null ID là lỗi lập trình, không ngầm coi là không tìm thấy.

- `of(value)`: yêu cầu non-null; `ofNullable(value)`: null thành empty; `empty()`: không có giá trị.
- `map`: biến đổi giá trị tìm thấy. `flatMap`: mapper đã trả Optional, tránh Optional lồng nhau.
- `orElse(fallback())`: đối số được tính trước khi gọi method, kể cả khi Optional có giá trị.
- `orElseGet(() -> fallback())`: chỉ tính fallback khi empty.
- `orElseThrow`: caller diễn đạt rõ thiếu kết quả là lỗi trong ngữ cảnh của mình.

Ví dụ đọc số tiền nếu tìm thấy: `analytics.findById("T1").map(Transaction::amount)`. Không dùng Optional cho field/parameter của Transaction; các thành phần bắt buộc kiểm tra null tại constructor.

`TransactionOptionalTest` có 4 kiểm thử: tìm thấy và map; không thấy và ném lỗi theo caller; fallback eager/lazy khi có giá trị; supplier chạy đúng một lần khi thiếu. AtomicInteger chỉ là dụng cụ quan sát side effect trong test, không phải yêu cầu code nghiệp vụ chạy đồng thời.

Review: lỗi nghiệp vụ không đồng nghĩa Optional.empty. Trạng thái REJECTED/FAILED vẫn là giao dịch tìm thấy; lookup không được loại chúng chỉ vì không SUCCESS.
