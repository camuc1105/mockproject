/* @author Tran Thien Thanh 09/04/1996 */
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
        $('#totalPriceWithShipping').text(totalPrice.toLocaleString() + ' đ');

        // Set hidden input values
        $('#shippingMethodInput').val($('input[name="shippingMethod"]:checked').val());
        $('#totalPriceInput').val(totalPrice);
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
    updateTotalPrice();
});
