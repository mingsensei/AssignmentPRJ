const InputValidator = {

    // === VALIDATION RULES ===

    isEmpty: function(input) {
        return !input || input.trim() === "";
    },

    isValidEmail: function(email) {
        return /^[\w.-]+@[\w.-]+\.\w{2,}$/.test(email);
    },

    isValidPhone: function(phone) {
        return /^\+?\d{9,15}$/.test(phone);
    },

    isInteger: function(input) {
        return /^-?\d+$/.test(input);
    },

    isFloat: function(input) {
        return /^-?\d+(\.\d+)?$/.test(input);
    },

    isLengthBetween: function(input, min, max) {
        return input.length >= min && input.length <= max;
    },

    isValidDate: function(dateStr) {
        const date = new Date(dateStr);
        return !isNaN(date.getTime());
    },

    isStrongPassword: function(password) {
        return /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$/.test(password);
    },

    isInRange: function(number, min, max) {
        return number >= min && number <= max;
    },

    isValidURL: function(url) {
        return /^(https?:\/\/)?[\w.-]+\.\w{2,}(\/.*)?$/.test(url);
    },

    isAlphabetic: function(input) {
        return /^[A-Za-zÀ-ỹ\s]+$/.test(input);
    },

    // === STRING PROCESSING ===

    formatName: function(name) {
        return name
            .toLowerCase()
            .trim()
            .replace(/\s+/g, ' ')
            .split(' ')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' ');
    },

    removeSpecialChars: function(input) {
        return input.replace(/[^\w\s]/gi, '');
    },

    normalize: function(input) {
        return input.trim().toLowerCase();
    },

    // === AUTO FORM VALIDATION (alert từng lỗi ngay) ===

    validateFormInputs: function(formName) {
        const form = document.forms[formName];

        for (let i = 0; i < form.elements.length; i++) {
            const field = form.elements[i];
            const rule = field.getAttribute("data-validate");
            const label = field.getAttribute("data-label") || field.name || field.id || "Trường dữ liệu";

            if (!rule) continue;

            const value = field.value;

            switch (rule) {
                case "required":
                    if (this.isEmpty(value)) {
                        alert(`${label} không được để trống.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "email":
                    if (!this.isValidEmail(value)) {
                        alert(`${label} không hợp lệ.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "phone":
                    if (!this.isValidPhone(value)) {
                        alert(`${label} không hợp lệ.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "password":
                    if (!this.isStrongPassword(value)) {
                        alert(`${label} quá yếu. Cần ít nhất 8 ký tự, gồm chữ hoa, thường, số và ký tự đặc biệt.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "url":
                    if (!this.isValidURL(value)) {
                        alert(`${label} không hợp lệ.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "number":
                    if (!this.isFloat(value)) {
                        alert(`${label} phải là số.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "integer":
                    if (!this.isInteger(value)) {
                        alert(`${label} phải là số nguyên.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "date":
                    if (!this.isValidDate(value)) {
                        alert(`${label} không phải ngày hợp lệ.`);
                        field.focus();
                        return false;
                    }
                    break;

                case "name":
                    if (this.isEmpty(value)) {
                        alert(`${label} không được để trống.`);
                        field.focus();
                        return false;
                    } else if (!this.isAlphabetic(value)) {
                        alert(`${label} chỉ được chứa chữ cái.`);
                        field.focus();
                        return false;
                    } else {
                        field.value = this.formatName(value); // format tên
                    }
                    break;

                // Bạn có thể thêm các rule khác tại đây
            }
        }

        return true; // Tất cả hợp lệ
    }
};
