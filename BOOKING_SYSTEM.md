# Hướng Dẫn Sử Dụng Hệ Thống Đặt Vé

## Tổng Quan

Hệ thống đặt vé đã được tích hợp với các chức năng sau:
1. **Xem danh sách lịch chiếu** - ShowtimeListActivity
2. **Xem chi tiết lịch chiếu và chọn ghế** - ShowtimeDetailActivity
3. **Đặt vé với API** - POST /api/bookings

## API Endpoints

### 1. Lấy Danh Sách Showtimes
```
GET /api/showtimes
GET /api/showtimes?movieId={movieId}
```

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": "showtime123",
      "movieId": 1,
      "showDate": "2024-01-15",
      "startTime": "19:00",
      "endTime": "21:30",
      "availableSeats": 50,
      "totalSeats": 100,
      "movie": {
        "id": 1,
        "title": "Movie Title",
        ...
      }
    }
  ]
}
```

### 2. Lấy Chi Tiết Showtime (bao gồm danh sách ghế)
```
GET /api/showtimes/{id}
```

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "showtime": {
      "id": "showtime123",
      "movieId": 1,
      "showDate": "2024-01-15",
      "startTime": "19:00",
      "endTime": "21:30"
    },
    "seats": [
      {
        "id": "A1_showtime123",
        "seatNumber": "A1",
        "row": "A",
        "column": 1,
        "isBooked": false,
        "price": 100000
      },
      {
        "id": "A2_showtime123",
        "seatNumber": "A2",
        "row": "A",
        "column": 2,
        "isBooked": true,
        "price": 100000
      }
    ]
  }
}
```

### 3. Đặt Vé
```
POST /api/bookings
Content-Type: application/json
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "customerEmail": "nguyenvana@example.com",
  "seatIds": [
    "A1_showtime123",
    "A2_showtime123"
  ],
  "showtimeId": "showtime123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "id": "booking456",
    "customerEmail": "nguyenvana@example.com",
    "showtimeId": "showtime123",
    "seatIds": ["A1_showtime123", "A2_showtime123"],
    "totalPrice": 200000,
    "bookingDate": "2024-01-10T10:30:00",
    "status": "confirmed"
  }
}
```

## Cấu Trúc File Đã Tạo

### Models
- `Showtime.java` - Model cho lịch chiếu
- `Seat.java` - Model cho ghế ngồi
- `Booking.java` - Model cho đặt vé

### Schema (Request/Response)
- `ShowtimesResponse.java` - Response danh sách showtimes
- `ShowtimeDetailResponse.java` - Response chi tiết showtime
- `BookingRequest.java` - Request đặt vé
- `BookingResponse.java` - Response đặt vé

### Activities
- `ShowtimeListActivity.java` - Màn hình danh sách lịch chiếu
- `ShowtimeDetailActivity.java` - Màn hình chi tiết lịch chiếu và chọn ghế

### Adapters
- `ShowtimeAdapter.java` - Adapter hiển thị danh sách showtimes
- `SeatAdapter.java` - Adapter hiển thị danh sách ghế (grid layout)

### Layouts
- `activity_showtime_list.xml` - Layout danh sách lịch chiếu
- `item_showtime.xml` - Layout item cho mỗi showtime
- `activity_showtime_detail.xml` - Layout chi tiết showtime với chọn ghế
- `item_seat.xml` - Layout item cho mỗi ghế

### API Service
`ApiService.java` đã được cập nhật với các endpoints:
- `getShowtimes()` - Lấy tất cả showtimes
- `getShowtimesByMovie(movieId)` - Lấy showtimes theo phim
- `getShowtimeDetail(showtimeId)` - Lấy chi tiết showtime
- `createBooking(bookingRequest)` - Đặt vé

## Cách Sử Dụng Trong Code

### 1. Mở Danh Sách Showtimes
```java
// Xem tất cả showtimes
Intent intent = new Intent(context, ShowtimeListActivity.class);
startActivity(intent);

// Xem showtimes của một phim cụ thể
Intent intent = new Intent(context, ShowtimeListActivity.class);
intent.putExtra("movieId", movieId);
startActivity(intent);
```

### 2. Mở Chi Tiết Showtime
```java
Intent intent = new Intent(context, ShowtimeDetailActivity.class);
intent.putExtra("showtimeId", "showtime123");
intent.putExtra("movieTitle", "Movie Name"); // Optional
startActivity(intent);
```

### 3. Flow Đặt Vé
1. User vào ShowtimeListActivity → chọn lịch chiếu
2. Mở ShowtimeDetailActivity → hiển thị sơ đồ ghế
3. User chọn ghế → cập nhật tổng tiền
4. User nhấn "Đặt Vé" → gọi API POST /api/bookings
5. Thành công → hiển thị dialog xác nhận với mã booking

## Màu Sắc Ghế

Đã thêm vào `colors.xml`:
```xml
<color name="seat_available">#FFFFFF</color>  <!-- Ghế trống - Trắng -->
<color name="seat_selected">#4CAF50</color>   <!-- Ghế đang chọn - Xanh lá -->
<color name="seat_booked">#9E9E9E</color>     <!-- Ghế đã đặt - Xám -->
```

## Xác Thực (Authentication)

Hệ thống sử dụng email từ `TokenManager` để đặt vé:
- User phải đăng nhập trước khi đặt vé
- Email được lấy từ `TokenManager.getInstance(context).getEmail()`
- Token được tự động thêm vào header bởi `AuthInterceptor`

## Lưu Ý Quan Trọng

1. **Base URL**: Cập nhật base URL trong `RetrofitClient.java` để trỏ đến server của bạn
2. **Authentication**: User cần đăng nhập để có token và email
3. **Seat Layout**: Adapter sử dụng GridLayoutManager với 8 cột (8 ghế mỗi hàng)
4. **Price Format**: Sử dụng `NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))` cho định dạng tiền Việt Nam

## Testing

Để test hệ thống:
1. Đảm bảo API backend đang chạy
2. Đăng nhập vào app
3. Mở ShowtimeListActivity
4. Chọn một showtime
5. Chọn ghế và đặt vé
6. Kiểm tra response từ server

## Tích Hợp Vào App Hiện Tại

Bạn có thể thêm nút "Xem Lịch Chiếu" ở:
- **MainActivity/HomeFragment**: Thêm nút để xem tất cả showtimes
- **MovieDetailActivity**: Thêm nút "Đặt Vé" để xem showtimes của phim đó
- **Menu Navigation**: Thêm menu item cho Showtimes

Ví dụ trong MovieDetailActivity:
```java
Button btnBookTicket = findViewById(R.id.btnBookTicket);
btnBookTicket.setOnClickListener(v -> {
    Intent intent = new Intent(this, ShowtimeListActivity.class);
    intent.putExtra("movieId", movieId);
    startActivity(intent);
});
```

