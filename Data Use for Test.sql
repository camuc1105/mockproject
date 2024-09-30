/* QUERY XỬ LÝ GỌI XÓA DB
use MockProject

drop table userrole
drop table role
drop table image
drop table discount
drop table orderdetail
drop table transaction_history
drop table payment
drop table orders
drop table product
drop table category
drop table users

select * from orders
select * from orderdetail
select * from transaction_history
select * from category
select * from product
select * from image
select * from discount
select * from users
select * from role
select * from userrole
*/

-- CHÚ Ý INSERT TỪNG BẢNG DATA ĐỂ HẠN CHẾ LỖI
-- LÀM THEO THỨ TỰ TỪNG BẢNG 1
-- TỰ ĐĂNG KÝ MỘT USER (MÀN HÌNH LÚC NÀY SẼ KHÔNG VỀ ĐƯỢC TRANG HOME)

-- Thêm dữ liệu vào bảng Role
INSERT INTO ROLE (role) VALUES
('ADMIN'),
('USER');

-- Thêm dữ liệu vào bảng Users
INSERT INTO USERS (email, password, user_name, phone, address, status, created_Date) VALUES
('qwe123@gmail.com', 'password123', N'Nguyen Thị Ánh', '0901234567', N'123 Đường ABC, Quận 1, TP.HCM', N'Active', GETDATE()),
('asd123@gmail.com', 'password456', N'Lê Văn Hải', '0912345678', N'456 Đường DEF, Quận 2, TP.HCM', N'Active', GETDATE()),
('zxc123@gmail.com', 'password789', N'Phạm Văn Đức', '0923456789', N'789 Đường GHI, Quận 3, TP.HCM', N'Active', GETDATE());

-- Thêm dữ liệu vào bảng User-Role
INSERT INTO USERROLE (user_Id, role_id) VALUES
(1, 1),  -- USER 1 THÊM ROLE ADMIN
(1, 2)  -- THÊM DÒNG NÀY NẾU CHƯA APPLY CODE BỔ SUNG CỦA PHÚC

-- Thêm dữ liệu vào bảng Category
INSERT INTO CATEGORY (category_name, created_date) VALUES
(N'Áo sơ mi', GETDATE()),
(N'Quần jeans', GETDATE()),
(N'Áo thun', GETDATE()),
(N'Quần tây', GETDATE()),
(N'Áo khoác', GETDATE()),
(N'Áo len', GETDATE()),
(N'Quần short', GETDATE()),
(N'Áo vest', GETDATE())

-- Thêm dữ liệu vào bảng Product
INSERT INTO PRODUCT (product_Name, category_Id, color, size, price, quantity, description, user_id, created_Date) 
VALUES
-- Áo sơ mi
(N'Áo sơ mi trắng', 1, N'Trắng', 'L', 250000, 100, N'Áo sơ mi trắng nam, chất liệu cotton', 1, GETDATE()),
(N'Áo sơ mi xanh dương', 1, N'Xanh dương', 'M', 270000, 50, N'Áo sơ mi xanh dương nam, chất liệu linen', 1, GETDATE()),
(N'Áo sơ mi đen', 1, N'Đen', 'XL', 280000, 60, N'Áo sơ mi đen nam, chất liệu cotton', 1, GETDATE()),
(N'Áo sơ mi hồng', 1, N'Hồng', 'S', 260000, 80, N'Áo sơ mi hồng nữ, chất liệu silk', 1, GETDATE()),

-- Quần jeans
(N'Quần jeans đen', 2, N'Đen', 'M', 370000, 60, N'Quần jeans đen, chất liệu denim', 1, GETDATE()),
(N'Quần jeans xanh dương', 2, N'Xanh dương', 'L', 350000, 75, N'Quần jeans xanh dương, chất liệu denim', 1, GETDATE()),
(N'Quần jeans trắng', 2, N'Trắng', 'M', 380000, 55, N'Quần jeans trắng, chất liệu denim', 1, GETDATE()),
(N'Quần jeans hồng', 2, N'Hồng', 'S', 360000, 50, N'Quần jeans hồng, chất liệu denim', 1, GETDATE()),
(N'Quần jeans tím', 2, N'Tím', 'XL', 400000, 40, N'Quần jeans tím, chất liệu denim', 1, GETDATE()),

-- Áo thun
(N'Áo thun đỏ', 3, N'Đỏ', 'M', 150000, 200, N'Áo thun đỏ, chất liệu cotton', 1, GETDATE()),
(N'Áo thun vàng', 3, N'Vàng', 'L', 160000, 180, N'Áo thun vàng, chất liệu cotton', 1, GETDATE()),
(N'Áo thun cam', 3, N'Cam', 'M', 140000, 160, N'Áo thun cam, chất liệu polyester', 1, GETDATE()),
(N'Áo thun đen', 3, N'Đen', 'S', 170000, 190, N'Áo thun đen, chất liệu cotton', 1, GETDATE()),
(N'Áo thun trắng', 3, N'Trắng', 'XL', 155000, 170, N'Áo thun trắng, chất liệu cotton', 1, GETDATE()),

-- Quần tây
(N'Quần tây đen', 4, N'Đen', 'L', 400000, 90, N'Quần tây đen, chất liệu vải kaki', 1, GETDATE()),
(N'Quần tây xanh dương', 4, N'Xanh dương', 'M', 420000, 70, N'Quần tây xanh dương, chất liệu vải kaki', 1, GETDATE()),
(N'Quần tây trắng', 4, N'Trắng', 'XL', 380000, 65, N'Quần tây trắng, chất liệu vải kaki', 1, GETDATE()),
(N'Quần tây vàng', 4, N'Vàng', 'S', 430000, 55, N'Quần tây vàng, chất liệu vải kaki', 1, GETDATE()),

-- Áo khoác
(N'Áo khoác đen', 5, N'Đen', 'L', 600000, 30, N'Áo khoác da nam, chất liệu da thật', 1, GETDATE()),
(N'Áo khoác tím', 5, N'Tím', 'M', 450000, 40, N'Áo khoác dù, chất liệu chống nước', 1, GETDATE()),
(N'Áo khoác trắng', 5, N'Trắng', 'XL', 500000, 35, N'Áo khoác dù trắng, chống thấm nước', 1, GETDATE()),
(N'Áo khoác cam', 5, N'Cam', 'S', 480000, 25, N'Áo khoác thể thao cam, chất liệu nylon', 1, GETDATE()),

-- Áo len
(N'Áo len xanh lá', 6, N'Xanh lá', 'M', 320000, 80, N'Áo len cổ tròn, chất liệu len cao cấp', 1, GETDATE()),
(N'Áo len đỏ', 6, N'Đỏ', 'L', 300000, 60, N'Áo len cổ tim, chất liệu len cao cấp', 1, GETDATE()),
(N'Áo len đen', 6, N'Đen', 'XL', 340000, 70, N'Áo len cổ lọ, chất liệu len cao cấp', 1, GETDATE()),
(N'Áo len hồng', 6, N'Hồng', 'S', 310000, 65, N'Áo len hồng nữ, chất liệu len mềm', 1, GETDATE()),

-- Quần short
(N'Quần short be', 7, N'Vàng', 'L', 180000, 100, N'Quần short kaki vàng, chất liệu vải thô', 1, GETDATE()),
(N'Quần short jeans', 7, N'Xanh dương', 'M', 200000, 120, N'Quần short jeans xanh dương, chất liệu denim', 1, GETDATE()),
(N'Quần short trắng', 7, N'Trắng', 'S', 190000, 110, N'Quần short trắng, chất liệu cotton', 1, GETDATE()),
(N'Quần short đen', 7, N'Đen', 'XL', 220000, 90, N'Quần short đen, chất liệu jeans', 1, GETDATE()),

-- Áo vest
(N'Áo vest đen', 8, N'Đen', 'L', 700000, 50, N'Áo vest đen nam, chất liệu cao cấp', 1, GETDATE()),
(N'Áo vest xám', 8, N'Xanh dương', 'M', 720000, 40, N'Áo vest xám nam, chất liệu cao cấp', 1, GETDATE()),
(N'Áo vest trắng', 8, N'Trắng', 'XL', 740000, 35, N'Áo vest trắng nam, chất liệu cao cấp', 1, GETDATE()),
(N'Áo vest đỏ', 8, N'Đỏ', 'S', 710000, 45, N'Áo vest đỏ nam, chất liệu cao cấp', 1, GETDATE());

-- Thêm dữ liệu vào bảng Image
INSERT INTO IMAGE (product_Id, img_link, created_date) VALUES
(1, 'ao_somitrang.jpg', GETDATE()),
(2, 'ao_somixanhduong.jpg', GETDATE()),
(3, 'ao_somiden.jpg', GETDATE()),
(4, 'ao_somihong.jpg', GETDATE()),

(5, 'quan_jeansden.jpg', GETDATE()),
(6, 'quan_jeansxanhduong.jpg', GETDATE()),
(7, 'quan_jeanstrang.jpg', GETDATE()),
(8, 'quan_jeanshong.jpg', GETDATE()),
(9, 'quan_jeanstim.jpg', GETDATE()),

(10, 'ao_thundo.jpg', GETDATE()),
(11, 'ao_thunvang.jpg', GETDATE()),
(12, 'ao_thuncam.jpg', GETDATE()),
(13, 'ao_thunden.jpg', GETDATE()),
(14, 'ao_thuntrang.jpg', GETDATE()),

(15, 'quan_tayden.jpg', GETDATE()),
(16, 'quan_tayxanhduong.jpg', GETDATE()),
(17, 'quan_taytrang.jpg', GETDATE()),
(18, 'quan_tayvang.jpg', GETDATE()),

(19, 'ao_khoacden.jpg', GETDATE()),
(20, 'ao_khoactim.jpg', GETDATE()),
(21, 'ao_khoactrang.jpg', GETDATE()),
(22, 'ao_khoaccam.jpg', GETDATE()),

(23, 'ao_lenxanhla.jpg', GETDATE()),
(24, 'ao_lendo.jpg', GETDATE()),
(25, 'ao_lenden.jpg', GETDATE()),
(26, 'ao_lenhong.jpg', GETDATE()),

(27, 'quan_shortvang.jpg', GETDATE()),
(28, 'quan_shortxanhduong.jpg', GETDATE()),
(29, 'quan_shorttrang.jpg', GETDATE()),
(30, 'quan_shortden.jpg', GETDATE()),

(31, 'ao_vestden.jpg', GETDATE()),
(32, 'ao_vestxanhduong.jpg', GETDATE()),
(33, 'ao_vesttrang.jpg', GETDATE()),
(34, 'ao_vestdo.jpg', GETDATE());

-- Thêm dữ liệu vào bảng Payment
INSERT INTO PAYMENT (payment_Method, description, status, created_Date) VALUES
(N'Tiền mặt', N'Thanh toán bằng tiền mặt', N'Hoạt động', GETDATE()),
(N'Thẻ tín dụng', N'Thanh toán qua thẻ tín dụng', N'Hoạt động', GETDATE()),
(N'Chuyển khoản ngân hàng', N'Thanh toán qua chuyển khoản', N'Hoạt động', GETDATE()),
(N'Ví điện tử', N'Thanh toán qua ví điện tử', N'Tạm dừng', GETDATE());
 
-- Thêm dữ liệu vào bảng Discount
INSERT INTO DISCOUNT (product_Id, discount_Percent, start_Date, end_Date, created_Date) 
VALUES
(1, 10.00, GETDATE(), DATEADD(DAY, 30, GETDATE()), GETDATE()),  -- Giảm giá 10% trong 30 ngày
(2, 15.00, DATEADD(DAY, -7, GETDATE()), GETDATE(), GETDATE()),  -- Giảm giá 15%, bắt đầu 7 ngày trước và kết thúc hôm nay
(3, 20.00, GETDATE(), DATEADD(DAY, 2, GETDATE()), GETDATE()),   -- Giảm giá 20%, trong 2 ngày tiếp theo
(4, 25.00, DATEADD(DAY, -3, GETDATE()), GETDATE(), GETDATE()),  -- Giảm giá 25%, bắt đầu 3 ngày trước và kết thúc hôm nay
(5, 30.00, GETDATE(), DATEADD(DAY, 5, GETDATE()), GETDATE()),   -- Sản phẩm 5, giảm giá 30% trong 5 ngày
(6, 10.00, GETDATE(), DATEADD(DAY, 20, GETDATE()), GETDATE()),  -- Giảm giá 10% trong 20 ngày
(7, 5.00, GETDATE(), DATEADD(DAY, 3, GETDATE()), GETDATE()),    -- Giảm giá 5% trong 3 ngày
(8, 15.00, DATEADD(DAY, -10, GETDATE()), GETDATE(), GETDATE()), -- Giảm giá 15%, bắt đầu 10 ngày trước và kết thúc hôm nay
(9, 20.00, GETDATE(), DATEADD(DAY, 2, GETDATE()), GETDATE()),   -- Giảm giá 20% trong 2 ngày tiếp theo
(10, 25.00, GETDATE(), DATEADD(DAY, 14, GETDATE()), GETDATE()); -- Giảm giá 25% trong 14 ngày

-- ĐẾN NGANG NÀY KHUYẾN KHÍCH TỰ TẠO ORDER THỦ CÔNG
-- HOẶC SỬ DỤNG CODE BÊN DƯỚI, CHÚ Ý CHỈNH SỬA ORDER_ID GIỐNG TRONG DB
-- VÀ TỔNG TIỀN CÓ THỂ CHƯA CHÍNH XÁC 

select * from orders
select * from orderdetail
select * from transaction_history
-- Thêm dữ liệu vào bảng Orders
INSERT INTO ORDERS (user_Id, order_Date, shipping_Price, total_Price, status, updated_Date) 
VALUES
(2, GETDATE(), 20000, 950000, N'Chờ phê duyệt', GETDATE()),  -- Đơn hàng 1
(2, GETDATE(), 10000, 1200000, N'Đang xử lý', GETDATE()),   -- Đơn hàng 2
(3, GETDATE(), 20000, 750000, N'Hoàn tất', GETDATE()),      -- Đơn hàng 3
(3, DATEADD(DAY, -2, GETDATE()), 10000, 850000, N'Hoàn tất', GETDATE()),  -- Đơn hàng 4
(4, GETDATE(), 20000, 1100000, N'Chờ phê duyệt', GETDATE()); -- Đơn hàng 5

-- Thêm dữ liệu vào bảng OrderDetail
INSERT INTO ORDERDETAIL (order_Id, product_Id, quantity, sub_total) 
VALUES
(1, 1, 2, 500000),   -- Đơn hàng 1
(1, 2, 1, 270000),   
(1, 3, 1, 180000),   

(2, 4, 1, 850000),   -- Đơn hàng 2
(2, 5, 2, 600000),

(3, 6, 2, 400000),   -- Đơn hàng 3
(3, 7, 1, 350000),

(4, 8, 1, 400000),   -- Đơn hàng 4
(4, 9, 1, 450000),

(5, 10, 3, 750000);  -- Đơn hàng 5

-- Thêm dữ liệu vào bảng Transaction History
INSERT INTO TRANSACTION_HISTORY (order_Id, payment_Id, transaction_Date, transaction_Amount, status) 
VALUES
(7, 1, GETDATE(), 970000, N'Hoàn tất'),  -- Giao dịch cho đơn hàng 1
(8, 2, GETDATE(), 1200000, N'Hoàn tất'),  -- Giao dịch cho đơn hàng 2
(9, 3, GETDATE(), 750000, N'Hoàn tất'),   -- Giao dịch cho đơn hàng 3
(10, 1, GETDATE(), 850000, N'Hoàn tất'),   -- Giao dịch cho đơn hàng 4
(11, 2, NULL, 1100000, N'Chờ thanh toán'); -- Giao dịch cho đơn hàng 5
