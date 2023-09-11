const image = document.getElementById("image");
const textdiv = document.getElementById("writing_body");
var imgnumber = 0;

function imageLoad(event) {
    if(imgnumber == 0) {
        const imagetag = document.createElement("img");
        imagetag.class = 'image'+imgnumber;
        textdiv.appendChild(imagetag);
        const reader = new FileReader();
        reader.onload = function(evt) {
            imagetag.src = evt.target.result;
        }
        reader.readAsDataURL(event.target.files[0]);
        var intimgnumber = parseInt(imgnumber);
        intimgnumber++;
        imgnumber = intimgnumber.toString();
    }
    else {
        var tmpnumber = parseInt(imgnumber);
        tmpnumber--;
        const image = document.getElementById("image"+tmpnumber.toString())
        textdiv.removeChild(image);
        const imagetag = document.createElement("img");
        textdiv.appendChild(imagetag);
        const reader = new FileReader();
        reader.onload = function(evt) {
            imagetag.src = evt.target.result;
        }
        reader.readAsDataURL(event.target.files[0]);
    }
    console.log(imgnumber);
}

function deleteimage() {
    var childimg = textdiv.getElementsByTagName('img');
    if(childimg.length < 1)  {
        image.value='';
        imgnumber = 0;
    }
}

function ChangeValue() {
    var select_str = document.getElementById('select_value');
    var value_text = select_str.options[select_str.selectedIndex].value;
    var contact_div = document.getElementById('price_contact');
    if(value_text == "market_board") {
        contact_div.style.display = "flex";
    }
    else {
        contact_div.style.display = "none";
    }
}

image.addEventListener("change",imageLoad);
textdiv.addEventListener("keyup",deleteimage);