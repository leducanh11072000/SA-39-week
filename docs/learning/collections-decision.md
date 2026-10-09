# Chọn collection cho wallet-lab

Nguồn bài: Excel `10 Học chi tiết!A11:M11`. Bảng dưới là áp dụng và giải thích cho project.

| Nhu cầu | Lựa chọn | Chi phí điển hình | Đánh đổi |
| --- | --- | --- | --- |
| Lịch sử có thứ tự | List; ArrayList khi xây, List.copyOf khi giữ snapshot | ArrayList get O(1), thêm cuối amortized O(1), tìm theo giá trị O(n) | Giữ thứ tự nhưng không tự chặn ID trùng |
| ID duy nhất | Set<String>, ví dụ HashSet hoặc keySet của chỉ mục | HashSet lookup/add trung bình O(1) khi hash phân bố tốt | Không cung cấp thứ tự thời gian; không lưu nội dung để phát hiện xung đột |
| Số dư theo accountId | Map<String, Money> khi mỗi tài khoản có một currency | HashMap lookup/put trung bình O(1) | Cần quản lý tính đúng của số dư; đây là bảng chọn, chưa triển khai kho số dư |
| Tra cứu giao dịch | Map<String, Transaction> | Chỉ mục xây O(n), lookup trung bình O(1) với hash map | Dùng thêm O(n) bộ nhớ, đổi lại không quét list mỗi lần |
| Tổng tiền gửi theo tài khoản | Map<AccountCurrency, Money> | Một lần duyệt O(n), ngoài chi phí BigDecimal | Không trộn currency; không được gọi tổng này là số dư |

LinkedList get(i) là O(n); chèn/xóa O(1) chỉ khi đã ở đúng vị trí qua iterator, tìm vị trí vẫn O(n). TreeMap/TreeSet cho thứ tự khóa và thao tác O(log n); chưa cần vì chỉ top 5 cần sắp xếp. BigDecimal có chi phí phụ thuộc độ dài số, nên O(n) ở đây mô tả số phần tử được duyệt.

Code dùng LinkedHashMap tạm để kiểm tra ID trùng và giữ lần xuất hiện đầu, sau đó List.copyOf/Map.copyOf. Mutation bên trong collector là cơ chế gom kết quả; không sửa input hay accumulator chia sẻ bên ngoài lambda. Set ID lấy từ map đã bất biến, tránh duy trì thêm bản sao chỉ mục.

Top 5 hiện dùng sort O(n log n), bộ nhớ O(n), đơn giản để học comparator; heap kích thước 5 có thể giảm chi phí nhưng chỉ xem xét khi dữ liệu/đo đạc đòi hỏi. Stream không lưu dữ liệu, collection có lưu dữ liệu. Chưa có benchmark và không đưa ra cam kết hiệu năng production.
