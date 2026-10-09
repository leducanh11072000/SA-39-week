# Bằng chứng kiểm thử tuần 1

Ngày chạy: 2026-10-08, 23:33 (Asia/Bangkok).

- Runtime: Eclipse Temurin 21.0.12.1+1; Maven 3.9.9; compiler release 21.
- JUnit Jupiter 5.14.4; Surefire 3.5.2.
- Lệnh đã chạy: `mvn -B "-Dmaven.repo.local=.m2/repository" test`.
- Kết quả Maven: BUILD SUCCESS, exit code 0.
- Bảng dưới được tổng hợp trực tiếp từ XML Surefire, không suy đoán từ mã nguồn.

| Lớp test | Lượt chạy | Failures | Errors | Skipped |
| --- | ---: | ---: | ---: | ---: |
| AccountTest | 5 | 0 | 0 | 0 |
| MoneyTest | 15 | 0 | 0 | 0 |
| TransferResultTest | 7 | 0 | 0 | 0 |
| TransferTest | 9 | 0 | 0 | 0 |
| **Tổng** | **36** | **0** | **0** | **0** |

Số lượt bao gồm từng bộ dữ liệu của parameterized tests. Báo cáo gốc nằm ở `target/surefire-reports/` (đầu ra build, không đưa vào Git). Đây là bằng chứng chạy code/test, không xác nhận người học đã hiểu.

JDK 21 được cài từ MSI chính thức Adoptium, đã kiểm tra SHA-256 `454cfd334b9ca91c96dd8c2de97fcef6b9f1f98be9172ff076711f1c6b44e4e0`. Bộ cài cập nhật JAVA_HOME/PATH cấp máy. Java, javac và Maven sau nạp lại môi trường đều dùng 21.0.12.1. Bản JDK 17 vẫn tồn tại.
