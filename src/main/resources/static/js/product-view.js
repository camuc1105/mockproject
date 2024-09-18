/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
function addToCart(event, element) {
            event.preventDefault();  // Ngăn chặn hành động mặc định (chuyển trang)
            let url = element.getAttribute('href');
            // Gửi yêu cầu thêm sản phẩm
            fetch(url).then(response => {
                if (response.ok) {
                    alert("Sản phẩm được thêm vào giỏ hàng thành công !!");
                    // Cập nhật giỏ hàng trên giao diện nếu cần
                } else {
                    alert("Failed to add product to cart.");
                }
            }).catch(error => {
                console.error("Error:", error);
            });
        }