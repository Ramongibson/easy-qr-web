function generateQRCode() {
    validateForm();
    const url = document.getElementById("url").value;
    const backgroundColor = document.getElementById("backgroundColor").value;
    const dotsColor = document.getElementById("dotsColor").value;


    fetch(`/generateQRCode?url=${encodeURIComponent(url)}&backgroundColor=${backgroundColor}&dotsColor=${dotsColor}`, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            if (data.qrCode) {
                document.getElementById("qrCode").src = 'data:image/png;base64,' + data.qrCode;
            } else {
                document.getElementById("error-message").textContent = 'Failed to generate QR code.';
            }
        })
        .catch(error => {
            console.error('Error generating QR code:', error);
            document.getElementById("error-message").textContent = 'Failed to generate QR code.';
        });
}


async function saveQRCode() {
    var fileName = 'qr-code'

    if (fileName === "") {
        alert("Please enter a file name.");
        return;
    }

    var qrCodeSrc = document.querySelector('.img-fluid').getAttribute('src');

    try {
        const fileHandle = await window.showSaveFilePicker({
            suggestedName: fileName + ".png",
            types: [{
                description: 'PNG Files',
                accept: {
                    'image/png': ['.png']
                }
            }]
        });

        const writableStream = await fileHandle.createWritable();
        const response = await fetch(qrCodeSrc);
        await response.body.pipeTo(writableStream);
    } catch (error) {
        console.error("Error saving QR code:", error);
        alert("Error saving QR code. Please try again.");
    }
}


function validateForm() {
    var urlValue = document.getElementById("url").value;
    var errorMessage = document.getElementById("error-message");
    var urlPattern = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i;

    if (!urlPattern.test(urlValue)) {
        errorMessage.innerHTML = "Please enter a valid URL (e.g., https://www.google.com).";
        return false;
    } else {
        errorMessage.innerHTML = "";
        return true;
    }
}
