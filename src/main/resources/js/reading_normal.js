function confirmDelete(formElement) {
    var r = confirm("정말로 삭제하시겠습니까?");
    if (r == true) {
        formElement.submit();
    }
}