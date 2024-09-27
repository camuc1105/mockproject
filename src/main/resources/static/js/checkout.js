/* @author Tran Thien Thanh 09/04/1996 */
$(document).ready(function() {
	// Function to format numbers as currency
    function formatCurrency(value) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
            .format(value)
            .replace(/\D00(?=\D*$)/, ' đ');  // Replace unwanted decimals with ' đ'
    }
	
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
        const orderTotal = parseFloat($('#orderTotalPrice').data('total')); // Use data attribute instead of raw text
        
        const totalPrice = orderTotal + shippingCost;

        // Use formatCurrency function to format the displayed values
        $('#shippingCost').text(formatCurrency(shippingCost));
        $('#totalPriceWithShipping').text(formatCurrency(totalPrice));

        // Set hidden input values
        $('#shippingMethodInput').val(shippingCost);
        $('#totalPriceInput').val(totalPrice);
    }
    
    // Format prices on page load
    function formatAllPrices() {
        // Format order total price
        const orderTotal = parseFloat($('#orderTotalPrice').data('total'));
        $('#orderTotalPrice').text(formatCurrency(orderTotal));

        // Format each item's price and total
        $('.item-price').each(function() {
            const itemPrice = parseFloat($(this).data('price'));
            $(this).text(formatCurrency(itemPrice));
        });

        $('.item-total').each(function() {
            const itemTotal = parseFloat($(this).data('total'));
            $(this).text(formatCurrency(itemTotal));
        });
    }

    // Handle shipping method change
    $('input[name="shippingMethod"]').on('change', function() {
        updateTotalPrice();
    });

    // Handle payment method selection and update the hidden input
    $('input[name="paymentMethod"]').on('change', function() {
        const selectedPaymentMethod = $('input[name="paymentMethod"]:checked').val();
        $('#paymentMethodInput').val(selectedPaymentMethod);  // Update the hidden input
        $('#checkoutBtn').prop('disabled', false);  // Enable the checkout button
        $('#paymentWarning').hide();  // Hide payment warning
    });

    // Initial calculation
    formatAllPrices();
    updateTotalPrice();
});
