$(document).ready(function() {
    // Xử lý sự kiện click nút xóa item
    $(document).on('click', '.remove-item', function(e) {
        e.preventDefault();

        // Đổi tên biến cho rõ ràng
        const courseId = $(this).data('id');
        const itemRow = $('#cart-row-' + courseId); // Tìm đến thẻ tr bằng id đã đặt trong JSP

        if (!confirm('Bạn có chắc chắn muốn xóa khóa học này khỏi giỏ hàng?')) {
            return;
        }

        $.ajax({
            // SỬA 1: Sửa lại URL cho đúng action 'remove'
            url: contextPath + '/cart/remove',
            method: 'POST',
            // SỬA 2: Sửa lại tên tham số thành 'courseId'
            data: { courseId: courseId },
            success: function(response) {
                if (response.success) {
                    // Xóa dòng item khỏi bảng với hiệu ứng fade
                    itemRow.fadeOut(500, function() {
                        $(this).remove();
                        // Kiểm tra nếu giỏ hàng trống thì hiển thị thông báo
                        if ($('tbody tr').length === 0) {
                            location.reload(); // Tải lại trang để hiển thị thông báo giỏ hàng trống
                        }
                    });

                    // SỬA 3: Cập nhật lại tổng tiền với định dạng cho đúng
                    const formattedTotal = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(response.newTotal);
                    $('#cart-total').text(formattedTotal);

                } else {
                    alert('Lỗi từ server: ' + response.error);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // Hiển thị lỗi chi tiết hơn để dễ debug
                console.error("AJAX Error: ", textStatus, errorThrown);
                alert('Không thể kết nối đến máy chủ để xóa sản phẩm.');
            }
        });
    });
});