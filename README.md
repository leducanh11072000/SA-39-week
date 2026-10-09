# wallet-lab

Ứng dụng ví và chuyển tiền mô phỏng để học Java/Banking theo lộ trình 39 tuần. Không xử lý tiền thật. Quản lý tài chính cá nhân là hướng mở rộng sau.

## Trạng thái

Đã triển khai code tuần 1 bằng Java 21 + Maven + JUnit 5: Money, Account, Transfer, TransferResult và các test nghiệp vụ. Chưa có API hoặc lưu trữ. Tên Maven là `wallet-lab`; dự án nằm trực tiếp trong thư mục hiện tại. Base package: `com.example.walletlab`.

## Cách học

Đọc [trạng thái hiện tại](docs/learning/current.md), rồi [tuần 1](docs/learning/week-01.md). Mỗi bài bắt đầu từ tình huống nghiệp vụ, câu hỏi và trade-off; chỉ triển khai một thay đổi nhỏ sau khi người học duyệt. Chạy được code, test đạt và người học đã hiểu là ba trạng thái riêng.

## Môi trường và kiểm tra trên Windows

Cần JDK 21 và Maven 3.9.x; Git dùng khi người học bắt đầu quản lý phiên bản. Dependency duy nhất khai báo trực tiếp là JUnit Jupiter 5.14.4 (test scope, gồm hỗ trợ parameterized tests). Compiler và Surefire được ghim phiên bản để cấu hình biên dịch/test rõ ràng.

Trong PowerShell, tại thư mục dự án:

```powershell
java -version
javac -version
mvn -version
git --version
mvn validate
mvn test
```

Trước `mvn test`, kiểm tra cả Java, javac và Java runtime của Maven là JDK 21. Lần chạy đầu cần truy cập Maven repository để tải plugin/dependency. POM dùng `maven.compiler.release=21`; không bật preview features.

Môi trường ban đầu ngày 2026-10-08: JDK 17.0.12 và Maven 3.9.9. Người học đã cho phép cài JDK 21 và thay mặc định Java/Maven. Kết quả kiểm chứng mới ghi ở phần tuần 1 bên dưới. `.git` đã có sẵn; `git status` báo dubious ownership nên chưa xác minh được trạng thái Git. Không khởi tạo Git, commit, push hoặc sửa safe.directory.

`mvn validate` chỉ kiểm tra mô hình Maven. Dùng `mvn test` để kiểm chứng nghiệp vụ; báo cáo ở `target/surefire-reports/`. Mức hiểu của người học được xác nhận riêng.

## Tài liệu và cấu trúc

- [AGENTS.md](AGENTS.md): quy tắc cộng tác với coding mentor.
- [Tầm nhìn sản phẩm](docs/product/vision.md): sản phẩm chính, phạm vi hiện tại và hướng mở rộng.
- [Bài học tuần 1](docs/learning/week-01.md): nguồn, thứ tự học, đầu ra và nghiệm thu.
- [Trạng thái học](docs/learning/current.md): điểm tiếp tục ngắn gọn.
- [Mẫu tuần học](docs/learning/week-template.md): dùng khi mở tuần mới.
- [Hướng dẫn ADR](docs/decisions/README.md) và [mẫu ADR](docs/decisions/adr-template.md): lưu quyết định sau thảo luận.
- `pom.xml`: cấu hình build; `.gitignore`: loại trừ đầu ra build và file IDE cục bộ.
- `src/main/java/com/example/walletlab/wallet/domain/`: Money, Account, Transfer và TransferResult.
- `src/test/java/com/example/walletlab/wallet/domain/`: bốn lớp test tương ứng.

## Kiểm chứng khởi tạo (lịch sử trước khi triển khai)

- `mvn -o "-Dmaven.repo.local=.m2/repository" validate`: BUILD SUCCESS, exit code 0 trên JDK 17. Chạy offline với đường dẫn repository cục bộ để chỉ kiểm tra cấu hình. Trình khởi động Maven có in `Access is denied.` trước log; chưa xác định nguyên nhân, nhưng Maven vẫn hoàn tất validate.
- Kiểm tra cấu trúc: đủ 12 file yêu cầu (tính cả hai `.gitkeep`), XML hợp lệ, release 21, một dependency trực tiếp và không có file `.java`.
- SHA-256 Excel trước/sau giống nhau: `DF5E00E486A4110E8B1288459CB0B3DC3558481CE347F5D83CBDC06316683987`.
- Chưa chạy `mvn test`, chưa kiểm chứng tải dependency/plugin hoặc build bằng JDK 21. Chưa có test nghiệp vụ; người học chưa xác nhận đã hiểu bài nào.

## Domain tuần 1

Chính sách được duyệt trong [ADR-0001](docs/decisions/0001-week-01-domain.md): VND scale 0, USD scale 2, chuẩn hóa chính xác không làm tròn; Money không âm và cho phép 0. Chuyển tiền phải dương, khác ID, cùng tiền tệ và đủ số dư.

`new Transfer(source, destination, amount).execute()` trả `Success` chứa hai snapshot tài khoản mới hoặc `Rejected` chứa lý do. Đầu vào không bị sửa. `TransferResult.message()` dùng pattern switch và record patterns Java 21. `Failed` biểu diễn lỗi kỹ thuật, chưa có nguồn phát sinh trong domain thuần.

Đây là phép tính trên snapshot, chưa bảo đảm transaction lưu trữ, đồng thời hoặc idempotency. Không có main/CLI: chạy kịch bản qua JUnit tests. [Baseline assessment](baseline-assessment.md) chứa câu hỏi review và dàn ý tiếng Anh; chưa xác nhận người học đã hiểu, chưa có bản ghi âm.

## Kiểm chứng tuần 1

Đã cài Temurin JDK 21.0.12.1 tại `C:\Program Files\Eclipse Adoptium\jdk-21`, cập nhật JAVA_HOME/PATH cấp máy; giữ nguyên JDK 17. Mở lại terminal/IDE để nhận biến môi trường mới. IDE có SDK riêng cần chọn JDK 21 ở Project SDK và Maven runner nếu vẫn đang ghim 17.

Lệnh đã kiểm chứng: `mvn -B "-Dmaven.repo.local=.m2/repository" test`. **36 lượt kiểm thử đạt, 0 failures, 0 errors, 0 skipped**; xem [bằng chứng](docs/learning/week-01-test-results.md). Repository cục bộ `.m2/repository` đã được gitignore; lệnh `mvn test` thông thường dùng cache Maven của tài khoản và có thể tải dependency lại.

Code đã chạy/test đã đạt. Người học đã hiểu: Chưa xác nhận. Chưa hoàn tất phần trình bày tiếng Anh.

## Tuần 2

Đã thêm Transaction bất biến và TransactionAnalytics: lịch sử duy nhất theo ID, lọc SUCCESS, nhóm tổng theo tài khoản gửi/currency, tổng theo currency, top 5 và lookup Optional. Không tự lưu lịch sử sau Transfer.execute. Xem [bài tuần 2](docs/learning/week-02.md), [bảng collection](docs/learning/collections-decision.md), [Optional](docs/learning/optional-notes.md) và [kết quả 58 test đạt](docs/learning/week-02-test-results.md). Trong đó 22 test mới; trạng thái hiểu vẫn chờ người học xác nhận.
