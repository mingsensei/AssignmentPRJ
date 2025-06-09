
    function toggleEdit(isEdit) {
    document.getElementById('infoDisplay').style.display = isEdit ? 'none' : 'block';
    document.getElementById('infoEdit').style.display = isEdit ? 'block' : 'none';
}

function openTab(tabName, evt) {
        const tabcontents = document.querySelectorAll(".tabcontent");
        tabcontents.forEach(tc => tc.classList.remove("active"));

        const btns = document.querySelectorAll(".sidebar button");
        btns.forEach(btn => btn.classList.remove("active"));

        document.getElementById(tabName).classList.add("active");
        evt.currentTarget.classList.add("active");
    }