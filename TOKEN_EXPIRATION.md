# Token Expiration Handling

## Tổng quan
Hệ thống tự động kiểm tra và xử lý token hết hạn trong ứng dụng MovieBooking Android.

## Cơ chế hoạt động

### 1. **TokenExpirationInterceptor**
- Interceptor được thêm vào OkHttpClient
- Tự động kiểm tra mọi response từ server
- Khi nhận response code **401 (Unauthorized)**:
  - Xóa token đã hết hạn khỏi SharedPreferences
  - Tự động chuyển về màn hình đăng nhập
  - Hiển thị thông báo "Phiên đăng nhập đã hết hạn"

### 2. **Luồng xử lý**

```
User thực hiện request → API Server
                            ↓
                    Response code 401?
                            ↓
                         ✓ YES
                            ↓
              TokenExpirationInterceptor
                            ↓
                  ┌─────────┴─────────┐
                  ↓                   ↓
          Clear Token          Redirect to Login
                  └─────────┬─────────┘
                            ↓
              Show "Session expired" message
```

### 3. **Các file đã được cập nhật**

#### a. **TokenExpirationInterceptor.java** (NEW)
```java
// Tự động xử lý response code 401
if (response.code() == 401) {
    TokenManager.getInstance(context).clearToken();
    Intent intent = new Intent(context, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.putExtra("session_expired", true);
    context.startActivity(intent);
}
```

#### b. **RetrofitClient.java** (UPDATED)
```java
// Thêm TokenExpirationInterceptor vào OkHttpClient
TokenExpirationInterceptor tokenExpirationInterceptor = 
    new TokenExpirationInterceptor(context);

OkHttpClient okHttpClient = new OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(tokenExpirationInterceptor) // ← NEW
    .addInterceptor(loggingInterceptor)
    .build();
```

#### c. **LoginActivity.java** (UPDATED)
```java
// Hiển thị thông báo khi session hết hạn
if (getIntent().getBooleanExtra("session_expired", false)) {
    Toast.makeText(this, 
        "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.", 
        Toast.LENGTH_LONG).show();
}
```

## Lợi ích

✅ **Tự động**: Không cần kiểm tra thủ công trong mỗi Activity
✅ **Bảo mật**: Token hết hạn được xóa ngay lập tức
✅ **User-friendly**: Thông báo rõ ràng cho người dùng
✅ **Tập trung**: Xử lý tại một điểm duy nhất (Interceptor)

## Test Cases

### Test 1: Token hết hạn khi xem profile
1. Đăng nhập vào ứng dụng
2. Đợi token hết hạn (hoặc xóa token trên server)
3. Vào Profile Fragment
4. **Kết quả mong đợi**: Tự động redirect về Login với thông báo

### Test 2: Token hết hạn khi cập nhật thông tin
1. Đăng nhập vào ứng dụng
2. Vào Edit Profile
3. Token hết hạn trong lúc đó
4. Click "Lưu thay đổi"
5. **Kết quả mong đợi**: Redirect về Login với thông báo

### Test 3: Token hết hạn khi đổi mật khẩu
1. Đăng nhập vào ứng dụng
2. Vào Change Password
3. Nhập thông tin mật khẩu
4. Token hết hạn
5. Click "Đổi mật khẩu"
6. **Kết quả mong đợi**: Redirect về Login với thông báo

## Response Codes

| Code | Meaning | Action |
|------|---------|--------|
| 401 | Unauthorized - Token expired/invalid | Clear token + Redirect to Login |
| 403 | Forbidden - No permission | Show error message |
| 200 | Success | Continue normally |

## Architecture

```
┌─────────────────────────────────────────────────┐
│            Application Layer                     │
│  (Activities, Fragments)                        │
└────────────────┬────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────┐
│            Retrofit Client                       │
│  ┌───────────────────────────────────────────┐ │
│  │  OkHttp Interceptor Chain                 │ │
│  │  1. AuthInterceptor (Add token)           │ │
│  │  2. TokenExpirationInterceptor (Check)    │ │ ← NEW
│  │  3. LoggingInterceptor (Debug)            │ │
│  └───────────────────────────────────────────┘ │
└────────────────┬────────────────────────────────┘
                 │
                 ↓
┌─────────────────────────────────────────────────┐
│            API Server                            │
└─────────────────────────────────────────────────┘
```

## Notes

- Interceptor hoạt động cho **TẤT CẢ** các API requests
- Không cần thêm code xử lý trong từng Activity/Fragment
- Token được xóa tự động khi hết hạn
- Intent flags đảm bảo clear back stack khi logout

## Future Improvements

1. **Token Refresh**: Tự động refresh token thay vì logout
2. **Retry Logic**: Tự động retry request sau khi refresh token
3. **Offline Mode**: Cache data khi không có kết nối
4. **Analytics**: Track số lần token expire để tối ưu thời gian sống token

