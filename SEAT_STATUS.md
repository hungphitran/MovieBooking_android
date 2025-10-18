# Hệ Thống Hiển Thị Trạng Thái Ghế

## Tổng Quan
Hệ thống hỗ trợ 4 trạng thái ghế với màu sắc riêng biệt để người dùng dễ dàng phân biệt.

## Các Trạng Thái Ghế

### 1. **Available (Trống)**
- **Trạng thái**: `"available"`
- **Màu sắc**: Trắng (`#FFFFFF`)
- **Có thể chọn**: ✅ Có
- **Mô tả**: Ghế trống, sẵn sàng để đặt

### 2. **Selected (Đang chọn)**
- **Trạng thái**: Ghế `available` + `isSelected = true`
- **Màu sắc**: Xanh lá (`#4CAF50`)
- **Có thể bỏ chọn**: ✅ Có
- **Mô tả**: Ghế đang được người dùng chọn nhưng chưa đặt

### 3. **Reserved (Đang giữ chỗ)**
- **Trạng thái**: `"reserved"`
- **Màu sắc**: Vàng cam (`#FF9800`)
- **Có thể chọn**: ❌ Không
- **Mô tả**: Ghế đang được giữ chỗ tạm thời bởi người dùng khác (có thể có timeout)

### 4. **Booked (Đã đặt)**
- **Trạng thái**: `"booked"`
- **Màu sắc**: Xám (`#9E9E9E`)
- **Có thể chọn**: ❌ Không
- **Mô tả**: Ghế đã được đặt và thanh toán thành công

## Logic Hiển Thị (SeatAdapter)

```java
if (seat.isBooked()) {
    // Ghế đã được đặt - màu xám
    cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_booked));
    cardSeat.setEnabled(false);
} else if (seat.isReserved()) {
    // Ghế đang được giữ chỗ - màu vàng cam
    cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_reserved));
    cardSeat.setEnabled(false);
} else if (seat.isSelected()) {
    // Ghế đang chọn - màu xanh lá
    cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_selected));
    cardSeat.setEnabled(true);
} else {
    // Ghế trống - màu trắng
    cardSeat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.seat_available));
    cardSeat.setEnabled(true);
}
```

## Xử Lý Click

Chỉ cho phép click vào ghế `available` (không phải `booked` hoặc `reserved`):

```java
cardSeat.setOnClickListener(v -> {
    if (!seat.isBooked() && !seat.isReserved() && listener != null) {
        seat.setSelected(!seat.isSelected());
        listener.onSeatClick(seat);
        notifyItemChanged(getAdapterPosition());
    }
});
```

## Model Seat

### Thuộc tính chính:
- `status`: String - Trạng thái từ API (`"available"`, `"booked"`, `"reserved"`)
- `isSelected`: boolean - Trạng thái local khi người dùng chọn ghế

### Methods:
- `isBooked()`: Kiểm tra ghế đã được đặt
- `isReserved()`: Kiểm tra ghế đang được giữ chỗ
- `isSelected()`: Kiểm tra ghế đang được chọn bởi người dùng hiện tại

## Màu Sắc (colors.xml)

```xml
<color name="seat_available">#FFFFFF</color>   <!-- Trắng -->
<color name="seat_selected">#4CAF50</color>    <!-- Xanh lá -->
<color name="seat_reserved">#FF9800</color>    <!-- Vàng cam -->
<color name="seat_booked">#9E9E9E</color>      <!-- Xám -->
```

## UI Legend

Màn hình hiển thị legend (chú thích) để người dùng hiểu ý nghĩa của từng màu:

```
⬜ Trống  |  🟩 Đang chọn  |  🟧 Đang giữ  |  ⬛ Đã đặt
```

## Luồng Hoạt Động

1. **Load ghế từ API** → Nhận danh sách ghế với trạng thái `status`
2. **Hiển thị ghế** → Adapter phân loại và hiển thị màu tương ứng
3. **Click ghế** → Chỉ cho phép click ghế `available`
4. **Chọn ghế** → Đổi `isSelected = true`, màu chuyển sang xanh lá
5. **Đặt vé** → Gửi danh sách ghế đã chọn lên server
6. **Cập nhật trạng thái** → Server cập nhật `status = "booked"`

## Lưu Ý

- Ghế `reserved` thường có timeout (ví dụ: 5-10 phút)
- Backend nên có cơ chế tự động chuyển `reserved` → `available` khi hết hạn
- Frontend nên refresh danh sách ghế định kỳ để cập nhật trạng thái realtime

