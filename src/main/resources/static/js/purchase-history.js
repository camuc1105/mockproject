function loadTransactionHistory() {
    fetch('/api/transactions/exclude-payment-id')
        .then(response => response.json())
        .then(data => {
            const customerTableBody = document.getElementById("customerTableBody");
            customerTableBody.innerHTML = '';

            data.forEach(transaction => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${transaction.order.id}</td>
                    <td>${new Date(transaction.transactionDate).toLocaleDateString()}</td>
                    <td>${transaction.transactionAmount.toLocaleString()} VND</td>
                    <td>${transaction.status}</td>
                    <td><a href="#">Xem</a></td>
                `;
                customerTableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading transaction data:', error));
}

document.addEventListener('DOMContentLoaded', loadTransactionHistory);
