const layer_toast = document.getElementById("layer_toast");
const layer_toast_inner = document.getElementById("layer_toast_inner");

function deleting(event) {
    event.target.parentElement.parentElement.parentElement.remove();
    toastup();
    setTimeout(function(){
        layer_toast.classList.add('hide');
    },2500);
}

function toastup() {
    layer_toast.classList.remove('hide');
    layer_toast_inner.style.display = "block";
}