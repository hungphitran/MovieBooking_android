# Hiển Thị Số Ghế Available Trong Danh Sách Showtime

## 📊 API Response Structure

API trả về danh sách showtime với thông tin số ghế available:

```json
{
    "success": true,
    "data": [
        {
            "showtimeId": "68f1f48fcde94827b5315854",
            "movieId": 1311031,
            "showDate": "2025-10-20",
            "startTime": "16:00:00",
            "endTime": "18:00:00",
            "movie": { ... },
            "availableSeats": 35  ← Số ghế trống
        }
    ]
}
```

## ✅ Đã Thực Hiện

### 1. **Model Showtime.java**
- Đã có trường `availableSeats` với annotation `@SerializedName("availableSeats")`
- Tự động map từ API response

### 2. **ShowtimeAdapter.java - Logic Hiển Thị**

```java
// Hiển thị số ghế available
// Nếu có totalSeats thì hiển thị cả hai, nếu không chỉ hiển thị available
if (showtime.getTotalSeats() > 0) {
    tvAvailableSeats.setText(showtime.getAvailableSeats() + "/" + showtime.getTotalSeats() + " ghế trống");
} else {
    tvAvailableSeats.setText(showtime.getAvailableSeats() + " ghế trống");
}
```

### 3. **Layout item_showtime.xml**
- Đã có TextView `tvAvailableSeats` để hiển thị số ghế
- Icon: 💺
- Format: "35 ghế trống" hoặc "35/100 ghế trống"

## 🎯 Cách Hoạt Động

### Trường hợp 1: API chỉ trả về `availableSeats`
**API Response:**
```json
{
    "availableSeats": 35
}
```
**Hiển thị:** "35 ghế trống"

### Trường hợp 2: API trả về cả `availableSeats` và `totalSeats`
**API Response:**
```json
{
    "availableSeats": 35,
    "totalSeats": 100
}
```
**Hiển thị:** "35/100 ghế trống"

## 📱 Giao Diện Hiển Thị

```
┌─────────────────────────────────────┐
│ Demon Slayer: Infinity Castle      │
│ 📅 2025-10-20                       │
│ 🕐 16:00:00 - 18:00:00             │
│ 💺 35 ghế trống                    │ ← Hiển thị ở đây
└─────────────────────────────────────┘
```

## 🔄 Luồng Dữ Liệu

1. **API** → Trả về `availableSeats` trong response
2. **Gson** → Deserialize vào `Showtime.availableSeats`
3. **ShowtimeAdapter** → Lấy giá trị và hiển thị
4. **UI** → Người dùng thấy số ghế trống

## 📌 Lưu Ý

- **availableSeats** được tính từ backend (chỉ đếm ghế có `status = "available"`)
- **KHÔNG bao gồm** ghế `"reserved"` (đang giữ chỗ)
- **KHÔNG bao gồm** ghế `"booked"` (đã đặt)
- Dữ liệu được cập nhật realtime khi load danh sách showtime
- Hỗ trợ **Swipe to Refresh** để cập nhật dữ liệu mới

## ✨ Tính Năng Bổ Sung

- **SwipeRefreshLayout**: Kéo xuống để làm mới danh sách
- **Click vào showtime**: Chuyển đến màn hình chi tiết để chọn ghế
- **Empty State**: Hiển thị "Không có lịch chiếu nào" khi danh sách trống
- **Loading State**: ProgressBar khi đang tải dữ liệu

