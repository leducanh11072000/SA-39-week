# Tuần 2 — Collections, Stream và mô hình miền bất biến

Trạng thái code: Đã triển khai và chạy kiểm thử. Trạng thái học: Chờ review; người học chưa xác nhận đã hiểu.

## Nguồn thực sự đã đọc

File [Lo_trinh_Senior_Java_Banking_39_tuan.xlsx](../../Lo_trinh_Senior_Java_Banking_39_tuan.xlsx):

- `02 Lộ trình 39 tuần!A6:F6`: chủ đề, mục tiêu xử lý lịch sử giao dịch, đầu ra transaction-analytics, bảng so sánh collection và 15 kiểm thử.
- `10 Học chi tiết!A11:M13`: ba bài, thứ tự, tài liệu, phạm vi đọc, bài tập và đầu ra.
- `04 Checklist tuần!A11:G16`: sáu tiêu chí nghiệm thu.
- `03 Kế hoạch tuần!A12:K18`: tóm tắt kiến thức, code/test, tiếng Anh 5–10 phút, tự kiểm tra 10 câu và retrospective.

Yêu cầu trực tiếp ngày 2026-10-09 cho phép viết tài liệu và thực thi code tuần 2. Các hướng dẫn commit trong Excel là nội dung lộ trình, không phải quyền tự commit; không thực hiện Git mutation.

## Mục tiêu và đầu ra từ lộ trình

Hiểu độ phức tạp collection, Stream pipeline, Optional, comparator và value object. Xử lý lịch sử giao dịch bằng lọc–nhóm–tổng hợp, giữ mô hình bất biến. Đầu ra: transaction-analytics, bảng chọn collection, ít nhất 15 kiểm thử. Các bài chi tiết yêu cầu 8 test analytics và 4 test Optional; mục tiêu tổng tuần vẫn là ít nhất 15.

## Bài 1 — Chọn đúng List, Set, Map

**Nội dung gốc:** 1,5 giờ; [Collections Framework](https://dev.java/learn/api/collections-framework/). Đọc Overview, Collection hierarchy, Choosing a Collection Implementation. Viết bảng chọn collection cho lịch sử giao dịch, mã giao dịch duy nhất và số dư theo accountId. Đầu ra: `collections-decision.md`.

**Áp dụng:** [bảng quyết định](collections-decision.md), List giữ thứ tự đầu vào; Set biểu diễn ID duy nhất; Map phục vụ lookup và nhóm tổng hợp. Transaction là record bất biến, TransactionAnalytics giữ snapshot đầu vào.

**Câu hỏi review:** vì sao Set<Transaction> chưa đủ đảm bảo ID duy nhất? Vì sao không dùng LinkedList cho truy cập vị trí? Map số dư khác Map doanh số chuyển tiền thế nào?

## Bài 2 — Stream pipeline không side effect

**Nội dung gốc:** 1,75 giờ; [link Streams từ Excel](https://dev.java/learn/api/streams/). Đọc map/filter/reduce, terminal operations, collect, parallel stream caveats. Lọc SUCCESS, nhóm theo tài khoản, tính tổng BigDecimal và top 5. Đầu ra: `TransactionAnalytics.java` và 8 test.

**Áp dụng:** `successfulTransactions`, `totalsBySourceAccount`, `totalSuccessful`, `topFiveSuccessful`. Có 15 test analytics: rỗng, trạng thái không thành công, nhiều tài khoản/tiền tệ, số thập phân, dữ liệu trùng, top 5, tie-break, defensive copy và đầu ra bất biến.

Pipeline chỉ được duyệt khi terminal operation chạy. Code dùng filter/map/reduce/collect/toList; không cập nhật biến tổng hoặc list bên ngoài lambda. Collector có accumulator nội bộ là bình thường; đó không phải sửa state của caller. Không dùng parallelStream vì chưa có đo đạc cho thấy lợi ích, và sorted/ghép collector cũng có chi phí.

**Câu hỏi review:** vì sao tạo stream chưa tính tổng? Vì sao không dùng peek để ghi nghiệp vụ? Vì sao không cộng USD với VND? Khi bằng số tiền, cần tie-break gì?

## Bài 3 — Optional đúng chỗ

**Nội dung gốc:** 1 giờ; [Optional Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html). Đọc of/ofNullable/empty/map/flatMap/orElseGet/orElseThrow. Hàm tìm giao dịch trả Optional; so sánh orElse và orElseGet bằng test có side effect. Đầu ra `optional-notes.md` và 4 test.

**Áp dụng:** `findById` trả Optional, không đặt Optional ở field hoặc tham số. [Ghi chú Optional](optional-notes.md) và `TransactionOptionalTest` có đủ 4 test. Counter có side effect chỉ dùng trong test để quan sát fallback, không dùng trong pipeline nghiệp vụ.

**Câu hỏi review:** ofNullable phù hợp với Map.get thế nào? Khi nào dùng map và flatMap? orElse có gọi fallback khi đã tìm thấy giao dịch không?

## Phạm vi áp dụng và lựa chọn triển khai

Đây là các quy ước triển khai cho bài tập, không phải chi tiết nguyên văn của Excel hoặc quyết định sản phẩm đã được thảo luận riêng:

- Giữ một Maven project và package domain. “Module” transaction-analytics được thực hiện bằng nhóm class chức năng, không thêm Maven submodule/hạ tầng.
- Transaction chứa ID, tài khoản gửi/nhận, Money và trạng thái SUCCESS/REJECTED/FAILED. SUCCESS phải dương và khác tài khoản; REJECTED có thể lưu lần thử chuyển 0/cùng tài khoản. Không mô hình hóa đầu vào âm vì Money đã cấm.
- Nhóm theo tài khoản gửi + currency. Tổng là lượng tiền chuyển đi thành công, không phải số dư hoặc dòng tiền ròng.
- Top 5 là giao dịch SUCCESS lớn nhất trong currency được chỉ định, giảm dần theo tiền, tăng dần theo ID khi bằng tiền. Đây không phải top 5 tài khoản. Lịch sử giữ thứ tự đầu vào, chưa có timestamp để suy ra thứ tự thời gian.
- Cùng ID và nội dung bằng nhau: chỉ giữ lần đầu. Cùng ID khác nội dung/trạng thái: báo lỗi, không chọn ngầm bản mới nhất. Lịch sử là snapshot, không phải event log có nhiều chuyển trạng thái trên cùng ID.
- Input được copy; output List/Map/Set không cho sửa. Transaction và Money bất biến nên không cần deep copy thêm.
- Lookup ID không thấy trả empty, null là lỗi lập trình. Map/Set không cam kết thứ tự duyệt; List có thứ tự rõ ràng.
- Chưa nối tự động Transfer.execute với lịch sử, chưa có persistence, ngày giờ, phân trang, cập nhật số dư hoặc đối soát ledger. Quyết định tích hợp/lưu trữ: **Chưa quyết định**.

## Đối chiếu tài liệu web ngày 2026-10-09

- Collections: công cụ web không trích xuất được nội dung; chưa xác minh tên mục hiện tại, giữ nguyên phạm vi Excel.
- Link Streams gốc trả 404. Tài liệu tương đương đã đọc: [Java 21 java.util.stream](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html), các mục Stream operations and pipelines, Parallelism, Non-interference, Stateless behaviors, Side-effects và Reduction. Giữ nguyên mục tiêu học, ghi rõ nguồn thay thế.
- Optional: API Java 21 truy cập được, đúng các phương thức bài yêu cầu.

## Kiểm chứng và nghiệm thu

Lệnh thực tế, sau khi chọn JDK 21:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
mvn -o -B "-Dmaven.repo.local=.m2/repository" test
```

`-o` dùng cache đã tải; máy mới bỏ `-o` để Maven tải dependency. Ngày 2026-10-09: 58 test đạt, 0 failures/errors/skipped; trong đó 22 test mới (15 analytics, 4 Optional, 3 Transaction). Xem [bằng chứng](week-02-test-results.md).

- [x] Code dùng List, Set, Map theo ba nhu cầu; có bảng so sánh.
- [ ] Người học giải thích được lựa chọn collection, lazy evaluation và side effect.
- [x] Optional dùng ở kết quả lookup, không ở field/tham số.
- [x] Tổng hợp đúng với fixture test, không trộn tiền tệ.
- [x] Có test rỗng, trùng giống nhau và trùng xung đột.
- [x] Không sửa input hoặc state bên ngoài trong pipeline; có kiểm thử bất biến.
- [ ] Người học hoàn thành trình bày tiếng Anh 5–10 phút và tự ghi ba lỗi.
- [ ] Tự kiểm tra 10 câu và retrospective.

Code đã chạy và test đã đạt không có nghĩa người học đã hiểu. Chưa kết luận toàn tuần đạt.

## Tự kiểm tra và retrospective

1. List, Set, Map phù hợp nhu cầu nào?
2. ArrayList và LinkedList khác nhau về get(i) thế nào?
3. Vì sao cần chỉ mục ID nếu đã có List?
4. Trùng ID khác nội dung phải xử lý ra sao?
5. Khi nào filter thực sự chạy?
6. Vì sao parallelStream không mặc nhiên nhanh hơn?
7. Tổng tiền chuyển đi khác số dư thế nào?
8. Comparator có tie-break giúp gì?
9. Khi nào fallback của orElseGet được gọi?
10. Vì sao List.copyOf đủ cho Transaction hiện tại nhưng có thể không đủ cho record chứa phần tử mutable?

Điểm: Chưa đánh giá. Điều đã hiểu: Chưa xác nhận. Tồn đọng: review các quy ước analytics, tự trình bày và xác nhận mức hiểu. Dàn ý tiếng Anh: collection choices → immutable history → successful aggregation → duplicate/currency failure cases → Optional fallback → limitations (mỗi phần khoảng 1 phút).
