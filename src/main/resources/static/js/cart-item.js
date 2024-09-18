/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
$(document).ready(function () {
	    // Tải trạng thái checkbox từ localStorage
	    loadCheckboxState();

	    // Sự kiện khi bấm vào checkbox "Chọn tất cả"
	    $('#selectAll').click(function () {
	        var checked = $(this).prop('checked');
	        $('.product-checkbox').prop('checked', checked);  // Chọn hoặc bỏ chọn tất cả sản phẩm
	        updateSelectedCount();
	        updateCartTotal();
	        saveCheckboxState();  // Lưu trạng thái checkbox
	    });

	    // Sự kiện khi chọn/bỏ chọn từng sản phẩm
	    $('.product-checkbox').click(function () {
	        $('#selectAll').prop(
	            'checked',
	            $('.product-checkbox:checked').length === $('.product-checkbox').length  // Kiểm tra nếu tất cả đã chọn
	        );
	        updateSelectedCount();
	        updateCartTotal();
	        saveCheckboxState();
	    });

	    updateSelectedCount();  // Cập nhật số sản phẩm đã chọn
	    updateCartTotal();  // Cập nhật tổng tiền
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