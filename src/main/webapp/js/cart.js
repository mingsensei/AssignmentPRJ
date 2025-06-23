$(document).ready(function() {
    // Xử lý sự kiện click nút xóa item
    $(document).on('click', '.remove-item', function(e) {
        e.preventDefault();
        
        const itemId = $(this).data('id');
        const itemRow = $(this).closest('tr');
        
        // Gửi request AJAX để xóa item
        $.ajax({
            url: contextPath + '/cart',
            method: 'POST',
            data: { id: itemId },
            success: function(response) {
                if (response.success) {
                    // Xóa dòng item khỏi bảng với hiệu ứng fade
                    itemRow.fadeOut(300, function() {
                        $(this).remove();
                    });
                    
                    // Cập nhật tổng tiền
                    $('.text-large strong').text('$' + response.newTotal.toFixed(2));
                    
                    // Hiển thị thông báo thành công
                    alert(response.message || 'Xóa sản phẩm thành công!');
                } else {
                    alert('Lỗi: ' + response.error);
                }
            },
            error: function() {
                alert('Đã xảy ra lỗi khi xóa sản phẩm');
            }
        });
    });
});