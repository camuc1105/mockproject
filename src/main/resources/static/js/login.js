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