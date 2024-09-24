//
// Tran Thao
//
// Hàm để chuyển đổi trạng thái đăng nhập
function toggleLoginState(isLoggedIn) {
    const userDropdownMenu = document.getElementById('userDropdownMenu');
    const guestMenu = document.getElementById('guestMenu');

    if (isLoggedIn) {
        userDropdownMenu.classList.remove('d-none');
        guestMenu.classList.add('d-none');
    } else {
        userDropdownMenu.classList.add('d-none');
        guestMenu.classList.remove('d-none');
    }
}
// Hàm để cập nhật thông tin người dùng
// function updateUserInfo(username, avatarUrl) {
//     document.getElementById('username').textContent = username;
//     document.getElementById('userAvatar').src = avatarUrl;
// }

// Giả lập đăng nhập
// document.addEventListener('DOMContentLoaded', () => {
//     const loginLink = document.querySelector('.login-box a');
//     loginLink.addEventListener('click', (e) => {
//         e.preventDefault();
//         // Giả lập quá trình đăng nhập thành công
//         toggleLoginState(true);
//         updateUserInfo('Người Dùng', '/path_to_avatar');
//     });
// });

// Xử lý đăng xuất
document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.getElementById('logoutBtn');
    logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();
        toggleLoginState(false);
    });
});