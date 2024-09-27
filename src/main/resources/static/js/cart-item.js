$(document).ready(function() {
    loadCheckboxState();
    loadQuantityState();

    $('#selectAll').click(function() {
        var checked = $(this).prop('checked');
        $('.product-checkbox').prop('checked', checked);
        updateSelectedCount();
        updateCartTotal();
        saveCheckboxState();
        updateCheckoutButton();
    });

    $('.product-checkbox').click(function() {
        $('#selectAll').prop(
            'checked',
            $('.product-checkbox:checked').length === $('.product-checkbox').length
        );
        updateSelectedCount();
        updateCartTotal();
        saveCheckboxState();
        updateCheckoutButton();
    });

    $('.increase-quantity').on('click', function() {
        var $quantityInput = $(this).closest('td').find('.quantity-input');
        var currentQuantity = parseInt($quantityInput.val());
        $quantityInput.val(currentQuantity + 1);

        updateItemTotal($quantityInput);
        updateCartTotal();
        saveQuantityState(); // Lưu số lượng vào localStorage
    });

    $('.decrease-quantity').on('click', function() {
        var $quantityInput = $(this).closest('td').find('.quantity-input');
        var currentQuantity = parseInt($quantityInput.val());
        if (currentQuantity > 1) {
            $quantityInput.val(currentQuantity - 1);
        }

        updateItemTotal($quantityInput);
        updateCartTotal();
        saveQuantityState(); // Lưu số lượng vào localStorage
    });

    updateSelectedCount();
    updateCartTotal();
    updateCheckoutButton();

    // Trigger form submission for selected products
    $('#checkout-form').submit(function(event) {
        event.preventDefault(); // Prevent the form from submitting normally
        updateCartForm(); // Update the hidden input with selected products
    });
});

function saveQuantityState() {
    var quantityState = {};
    $('.quantity-input').each(function() {
        var productId = $(this).data('id');
        quantityState[productId] = $(this).val();
    });
    localStorage.setItem('cartQuantityState', JSON.stringify(quantityState));
}

// Hàm khôi phục số lượng sản phẩm từ localStorage
function loadQuantityState() {
    var savedQuantities = JSON.parse(localStorage.getItem('cartQuantityState')) || {};
    $('.quantity-input').each(function() {
        var productId = $(this).data('id');
        if (savedQuantities[productId]) {
            $(this).val(savedQuantities[productId]);
            updateItemTotal($(this)); // Cập nhật thành tiền cho mỗi sản phẩm
        }
    });
    updateCartTotal(); // Cập nhật tổng tiền sau khi khôi phục số lượng
}


function loadCheckboxState() {
	var savedState = JSON.parse(localStorage.getItem('cartCheckboxState')) || {};
	$('.product-checkbox').each(function() {
		var productId = $(this).data('id');
		if (savedState[productId]) {
			$(this).prop('checked', true);
		}
	});
	updateSelectedCount();
	updateCartTotal();
	updateCheckoutButton();
}

function saveCheckboxState() {
	var state = {};
	$('.product-checkbox').each(function() {
		var productId = $(this).data('id');
		state[productId] = $(this).prop('checked');
	});
	localStorage.setItem('cartCheckboxState', JSON.stringify(state));
}

function updateSelectedCount() {
	$('#selected-count').text($('.product-checkbox:checked').length);
}
	function updateCartTotal() {
		var total = 0;
		$('.product-checkbox:checked').each(function() {
			var itemTotal = parseFloat($(this).closest('tr').find('.item-total').attr('data-total'));
			total += itemTotal;
		});
		$('#cart-total').text(total.toFixed(2));
	}

	function updateCheckoutButton() {
		const checkoutButton = $('#checkout-button');
		const selectedCount = $('.product-checkbox:checked').length;
		checkoutButton.prop('disabled', selectedCount === 0);
	}

	function updateItemTotal($quantityInput) {
		var price = parseFloat($quantityInput.closest('tr').find('td[data-price]').data('price'));
		var quantity = parseInt($quantityInput.val());
		var total = price * quantity;
		$quantityInput.closest('tr').find('.item-total').text(total.toFixed(2));
		$quantityInput.closest('tr').find('.item-total').attr('data-total', total);
	}

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