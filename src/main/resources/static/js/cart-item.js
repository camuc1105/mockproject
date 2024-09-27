$(document).ready(function() {
	// Tải trạng thái checkbox từ localStorage
	loadCheckboxState();

	// Sự kiện khi bấm vào checkbox "Chọn tất cả"
	$('#selectAll').click(function() {
		var checked = $(this).prop('checked');
		$('.product-checkbox').prop('checked', checked);  // Chọn hoặc bỏ chọn tất cả sản phẩm
		updateSelectedCount();
		updateCartTotal();
		saveCheckboxState();  // Lưu trạng thái checkbox
		updateCheckoutButton();  // Cập nhật trạng thái nút thanh toán
	});

	// Sự kiện khi chọn/bỏ chọn từng sản phẩm
	$('.product-checkbox').click(function() {
		$('#selectAll').prop(
			'checked',
			$('.product-checkbox:checked').length === $('.product-checkbox').length  // Kiểm tra nếu tất cả đã chọn
		);
		updateSelectedCount();
		updateCartTotal();
		saveCheckboxState();
		updateCheckoutButton();  // Cập nhật trạng thái nút thanh toán
	});

	// Sự kiện nút tăng số lượng
	$('.increase-quantity').on('click', function() {
		var $quantityInput = $(this).closest('td').find('.quantity-input');
		var currentQuantity = parseInt($quantityInput.val());
		$quantityInput.val(currentQuantity + 1);

		// Cập nhật tổng tiền cho sản phẩm này và tổng tiền của giỏ hàng
		updateItemTotal($quantityInput);
		updateCartTotal();
	});

	// Sự kiện nút giảm số lượng
	$('.decrease-quantity').on('click', function() {
		var $quantityInput = $(this).closest('td').find('.quantity-input');
		var currentQuantity = parseInt($quantityInput.val());
		if (currentQuantity > 1) { // Đảm bảo số lượng không dưới 1
			$quantityInput.val(currentQuantity - 1);
		}

		// Cập nhật tổng tiền cho sản phẩm này và tổng tiền của giỏ hàng
		updateItemTotal($quantityInput);
		updateCartTotal();
	});

	updateSelectedCount();  // Cập nhật số sản phẩm đã chọn
	updateCartTotal();  // Cập nhật tổng tiền
	updateCheckoutButton(); // Cập nhật trạng thái nút thanh toán khi load trang

	// Trigger form submission for selected products
	$('#checkout-form').submit(function(event) {
		event.preventDefault(); // Prevent the form from submitting normally
		updateCartForm(); // Update the hidden input with selected products
	});
});

// Hàm tải trạng thái checkbox từ localStorage
function loadCheckboxState() {
	var savedState = JSON.parse(localStorage.getItem('cartCheckboxState')) || {};
	$('.product-checkbox').each(function() {
		var productId = $(this).data('id');
		if (savedState[productId]) {
			$(this).prop('checked', true);  // Khôi phục trạng thái đã chọn
		}
	});
	updateSelectedCount();
	updateCartTotal();
	updateCheckoutButton();  // Cập nhật trạng thái nút thanh toán
}

// Hàm lưu trạng thái checkbox vào localStorage
function saveCheckboxState() {
	var state = {};
	$('.product-checkbox').each(function() {
		var productId = $(this).data('id');
		state[productId] = $(this).prop('checked');  // Lưu trạng thái checkbox
	});
	localStorage.setItem('cartCheckboxState', JSON.stringify(state));
}

// Hàm cập nhật số sản phẩm đã chọn
function updateSelectedCount() {
	$('#selected-count').text($('.product-checkbox:checked').length);  // Hiển thị số sản phẩm đã chọn
}

// Hàm tính lại tổng tiền
function updateCartTotal() {
	var total = 0;
	$('.product-checkbox:checked').each(function() {
		var itemTotal = parseFloat($(this).closest('tr').find('.item-total').attr('data-total'));
		total += itemTotal;  // Cộng tổng tiền của các sản phẩm đã chọn
	});
	$('#cart-total').text(total.toFixed(2));  // Hiển thị tổng tiền
}

// Hàm cập nhật trạng thái nút thanh toán
function updateCheckoutButton() {
	const checkoutButton = $('#checkout-button');
	const selectedCount = $('.product-checkbox:checked').length;
	checkoutButton.prop('disabled', selectedCount === 0); // Disable nếu không có sản phẩm nào được chọn
}

// Cập nhật tổng tiền cho sản phẩm dựa trên số lượng và giá
function updateItemTotal($quantityInput) {
	var price = parseFloat($quantityInput.closest('tr').find('td[data-price]').data('price'));
	var quantity = parseInt($quantityInput.val());
	var total = price * quantity;
	$quantityInput.closest('tr').find('.item-total').text(total.toFixed(2));
	$quantityInput.closest('tr').find('.item-total').attr('data-total', total);
}

// Cập nhật thông tin giỏ hàng vào form ẩn
function updateCartForm() {
	var cartItems = [];
	// Collect selected products
	$('.product-checkbox:checked').each(function() {
		var productId = $(this).data('id');
		var name = $(this).data('name');
		var color = $(this).closest('tr').find('td').eq(3).text();
		var size = $(this).closest('tr').find('td').eq(4).text();
		var quantity = parseInt($(this).closest('tr').find('input.quantity-input').val());
		var price = parseFloat($(this).closest('tr').find('td[data-price]').data('price'));
		var imgLink = $(this).closest('tr').find('img').attr('src');

		cartItems.push({
			productId: productId,
			name: name,
			color: color,
			size: size,
			quantity: quantity,
			price: price,
			imgLink: imgLink
		});
	});
	// Convert selected items to JSON and update the hidden input field
	$('#cart-items').val(JSON.stringify(cartItems));
	// Now submit the form
	$('#checkout-form')[0].submit();
}