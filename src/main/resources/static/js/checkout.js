/*@author Tran Thien Thanh 09/04/1996*/
$(document).ready(function() {
	// Calculate delivery dates
	const today = new Date();
	const standardDeliveryDate = new Date(today);
	standardDeliveryDate.setDate(today.getDate() + 2);
	const fastDeliveryDate = new Date(today);
	fastDeliveryDate.setDate(today.getDate() + 1);

	$('#standardDeliveryDate').text('Dự kiến nhận hàng: ' + standardDeliveryDate.toLocaleDateString());
	$('#fastDeliveryDate').text('Dự kiến nhận hàng: ' + fastDeliveryDate.toLocaleDateString());

	// Update total price with shipping cost
	function updateTotalPrice() {
		const shippingCost = parseInt($('input[name="shippingMethod"]:checked').val());
		const orderTotal = parseFloat($('#orderTotalPrice').text().replace(/[^\d.]/g, ''));
		const totalPrice = orderTotal + shippingCost;

		$('#shippingCost').text(shippingCost.toLocaleString() + ' đ');
		$('#selectedShippingMethod').text($('input[name="shippingMethod"]:checked').next().text().split(':')[0].trim());
		$('#totalPriceWithShipping').text(totalPrice.toLocaleString() + ' đ');

		// Set hidden input values
		$('#shippingMethodInput').val($('input[name="shippingMethod"]:checked').val());
		$('#totalPriceInput').val(totalPrice);
	}

	$('input[name="shippingMethod"]').on('change', function() {
		updateTotalPrice();
	});

	// Enable the order button when a payment method is selected
	$('input[name="paymentMethod"]').on('change', function() {
		$('#checkoutBtn').prop('disabled', false);
		$('#paymentWarning').hide();
	});

	// Initial calculation
	updateTotalPrice();
});