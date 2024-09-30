//
// Tran Thao
//
function togglePassword() {
    let passwordInput = document.getElementById("password");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";  // Hiển thị mật khẩu
    } else {
        passwordInput.type = "password";  // Ẩn mật khẩu
    }
}
// Hàm để quay lại trang trước đó
function goBack() {
    window.history.back();  // Quay lại trang trước đó
}
// Hàm validate form (bổ sung nếu cần)
function validateForm(event) {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var errorMessage = document.getElementById("error-message");

    if (!email || !password) {
        event.preventDefault(); // Ngăn không cho submit
        errorMessage.style.display = "block"; // Hiển thị lỗi
        errorMessage.textContent = "Email hoặc mật khẩu không được để trống!";
        return false; // Không submit form
    }

    return true;
}

// Gán sự kiện validate cho form nếu cần
document.querySelector("form").addEventListener("submit", validateForm);