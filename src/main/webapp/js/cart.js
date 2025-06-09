
    $(document).ready(function() {
    $(".remove-item").click(function(e) {
        e.preventDefault();

        const $row = $(this).closest("tr");
        const index = $(this).data("index");

        $.ajax({
            url: 'remove-item', // Servlet URL
            method: 'POST',
            data: { index: index },
            success: function(response) {
                // Nếu xóa thành công, loại bỏ dòng HTML
                $row.remove();

                // Cập nhật lại tổng giá trị đơn hàng
                $("#total-price").text('$' + response.newTotal.toFixed(2));
            },
            error: function() {
                alert("Error while removing item!");
            }
        });
    });
});

