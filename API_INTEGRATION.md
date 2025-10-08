# API Integration Guide

## Cấu hình API

### 1. Thay đổi Base URL
Mở file `RetrofitClient.java` và thay đổi `BASE_URL`:

```java
private static final String BASE_URL = "https://your-api-domain.com/";
```

### 2. Endpoints API

#### Login API
- **Endpoint**: `POST /auth/login`
- **Request Body**:
```json
{
  "email": "user@example.com",
  "password": "string"
}
```
- **Response**:
```json
{
  "success": true,
  "message": "Login successful",
  "token": "jwt_token_here",
  "data": {
    "id": "user_id",
    "email": "user@example.com",
    "name": "User Name",
    "avatar": "avatar_url",
    "address": "address",
    "phone": "phone_number",
    "age": 25
  }
}
```

#### Register API
- **Endpoint**: `POST /auth/register`
- **Request Body**:
```json
{
  "email": "user@example.com",
  "password": "string",
  "name": "string",
  "avatar": "string",
  "address": "string",
  "phone": "string",
  "age": 0
}
```
- **Response**: Giống như Login API

## Các file đã tạo

### 1. Dependencies (build.gradle)
- Retrofit 2.9.0
- Gson Converter 2.9.0
- OkHttp Logging Interceptor 4.11.0

### 2. Models
- `LoginRequest.java` - Model cho request đăng nhập
- `RegisterRequest.java` - Model cho request đăng ký
- `AuthResponse.java` - Model cho response từ API

### 3. API Layer
- `ApiService.java` - Interface định nghĩa các API endpoints
- `RetrofitClient.java` - Singleton client để gọi API

### 4. Activities
- `LoginActivity.java` - Đã cập nhật để gọi API login
- `RegisterActivity.java` - Đã cập nhật để gọi API register

### 5. Permissions (AndroidManifest.xml)
- `INTERNET` - Cho phép kết nối Internet
- `ACCESS_NETWORK_STATE` - Kiểm tra trạng thái mạng
- `usesCleartextTraffic="true"` - Cho phép HTTP (development only)

## Tính năng

### LoginActivity
- ✅ Validate email và password
- ✅ Gọi API login
- ✅ Hiển thị loading state (nút disable khi đang xử lý)
- ✅ Xử lý response thành công
- ✅ Xử lý lỗi (network, server error)
- ✅ Chuyển sang MainActivity khi đăng nhập thành công

### RegisterActivity
- ✅ Validate tất cả 8 trường input
- ✅ Gọi API register
- ✅ Hiển thị loading state
- ✅ Xử lý response thành công
- ✅ Xử lý lỗi (network, server error)
- ✅ Chuyển về LoginActivity sau khi đăng ký thành công

## Lưu ý

1. **BASE_URL**: Nhớ thay đổi BASE_URL trong `RetrofitClient.java` thành URL API thực tế
2. **HTTPS**: Trong production, nên dùng HTTPS và remove `usesCleartextTraffic="true"`
3. **Token Storage**: Có thể lưu token vào SharedPreferences để sử dụng cho các API calls khác
4. **Error Handling**: Đã xử lý các trường hợp lỗi cơ bản, có thể mở rộng thêm

## Testing

Để test API:
1. Sync Gradle để tải dependencies
2. Thay đổi BASE_URL trong RetrofitClient.java
3. Build và chạy app
4. Kiểm tra Logcat để xem request/response details

## Logs

API calls sẽ được log ra Logcat với tags:
- `LoginActivity` - Logs từ màn hình đăng nhập
- `RegisterActivity` - Logs từ màn hình đăng ký

