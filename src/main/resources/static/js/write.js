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
    var boardKindInput = document.getElementById('boardKind');

    if(value_text == "market_board") {
        contact_div.style.display = "flex";
        boardKindInput.value = "MARKET";  // BoardKind enum 값 설정
    } else {
        contact_div.style.display = "none";

        switch(value_text) {
            case "issue_board": boardKindInput.value = "ISSUE"; break;
            case "tip_board": boardKindInput.value = "TIP"; break;
            case "report_board": boardKindInput.value = "REPORT"; break;
            case "qna_board": boardKindInput.value = "QNA"; break;
            case "free_board": boardKindInput.value = "FREE"; break;
            case "fresh_board": boardKindInput.value = "FRESH"; break;
            case "fossil_board": boardKindInput.value = "FOSSIL"; break;
            case "info_board": boardKindInput.value = "INFO"; break;
            case "career_board": boardKindInput.value = "CAREER"; break;
            default: boardKindInput.value = ""; // 또는 default값 설정
        }
    }
}

image.addEventListener("change",imageLoad);
textdiv.addEventListener("keyup",deleteimage);