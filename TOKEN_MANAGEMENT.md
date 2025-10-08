# Token Management Documentation

## Tổng quan
Ứng dụng đã được tích hợp hệ thống quản lý token để xử lý xác thực người dùng.

## Cách hoạt động

### 1. Đăng nhập (Login)
Khi người dùng đăng nhập thành công:
- Token từ server được lưu vào SharedPreferences
- Email người dùng cũng được lưu
- Token sẽ được lưu **lâu dài** (rememberMe = true), người dùng không cần đăng nhập lại khi mở app

### 2. Lưu trữ Token
Token được lưu trong `SharedPreferences` với các key:
- `auth_token`: JWT token từ server
- `user_email`: Email của người dùng
- `remember_me`: Flag để xác định token có nên được giữ lại hay không

### 3. Sử dụng Token
Token tự động được thêm vào **tất cả** các API request thông qua `AuthInterceptor`:
- Mỗi request gửi đi sẽ tự động có header: `Authorization: Bearer {token}`
- Bạn không cần thêm token thủ công vào các API call

### 4. Đăng xuất (Logout)
Khi người dùng đăng xuất:
- Token và tất cả thông tin xác thực bị xóa
- Người dùng được chuyển về màn hình đăng nhập

### 5. Splash Screen
Khi mở app:
- `SplashActivity` kiểm tra token có tồn tại không
- Nếu có token → chuyển thẳng đến `MainActivity`
- Nếu không có token → chuyển đến `LoginActivity`

## Các API quan trọng của TokenManager

```java
// Lưu token (lâu dài)
TokenManager.getInstance(context).saveToken(token, email, true);

// Lưu token (session only - xóa khi tắt app)
TokenManager.getInstance(context).saveToken(token, email, false);

// Lấy token
String token = TokenManager.getInstance(context).getToken();

// Lấy email đã lưu
String email = TokenManager.getInstance(context).getEmail();

// Kiểm tra đã đăng nhập chưa
boolean isLoggedIn = TokenManager.getInstance(context).isLoggedIn();

// Xóa token khi đăng xuất
TokenManager.getInstance(context).clearToken();
```

## Sử dụng trong code

### Gọi API với token tự động
```java
// Token sẽ tự động được thêm vào request
RetrofitClient.getInstance(context).getApiService()
    .getSomeData()
    .enqueue(new Callback<ResponseType>() {
        // Handle response
    });
```

### Kiểm tra trạng thái đăng nhập
```java
if (TokenManager.getInstance(this).isLoggedIn()) {
    // User is logged in
} else {
    // User needs to login
}
```

### Đăng xuất
```java
// Clear token
TokenManager.getInstance(this).clearToken();

// Navigate to login
Intent intent = new Intent(this, LoginActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);
finish();
```

## Luồng hoạt động

```
1. App Start
   → SplashActivity
   → Check token exists?
      - Yes → MainActivity (đã đăng nhập)
      - No → LoginActivity (cần đăng nhập)

2. Login Success
   → Save token + email
   → Navigate to MainActivity
   → Tất cả API calls tự động có token

3. Logout
   → Clear token + email
   → Navigate to LoginActivity
   
4. App Close & Reopen
   → Token vẫn còn (rememberMe = true)
   → Auto navigate to MainActivity
```

## Files liên quan

1. **TokenManager.java** - Quản lý token trong SharedPreferences
2. **AuthInterceptor.java** - Tự động thêm token vào API requests
3. **RetrofitClient.java** - Khởi tạo Retrofit với AuthInterceptor
4. **LoginActivity.java** - Lưu token sau khi login thành công
5. **MainActivity.java** - Xóa token khi logout
6. **SplashActivity.java** - Kiểm tra token khi app khởi động
7. **MovieBookingApplication.java** - Khởi tạo RetrofitClient khi app start

## Lưu ý quan trọng

- Token được lưu **an toàn** trong SharedPreferences với MODE_PRIVATE
- Token tự động được thêm vào **tất cả** API requests
- Người dùng **không cần** đăng nhập lại khi mở app (trừ khi đã logout)
- Khi server trả về lỗi 401 (Unauthorized), bạn có thể xóa token và yêu cầu đăng nhập lại

