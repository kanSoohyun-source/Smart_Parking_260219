// 날짜 포맷팅 함수
function formatDateTime(dtStr) {
    if(!dtStr || dtStr === "null" || dtStr === "") return "-";
    return dtStr.replace('T', ' ').substring(0, 16);
}

function showReceipt() {
    // 1. 입력 필드에서 데이터 가져오기
    const carNum = document.getElementById("carNum").value;
    const totalTime = document.getElementById("totalParkingTime").value;
    const calculatedFee = document.getElementById("calculatedFee").value;
    const discountAmount = document.getElementById("discountAmount").value;
    const finalFee = document.getElementById("finalFee").value;

    // 2. 숨겨진 printArea(파란 영수증 디자인)에 데이터 채우기
    document.getElementById("p-carNum").innerText = carNum;
    document.getElementById("p-totalTime").innerText = totalTime;
    document.getElementById("p-calcFee").innerText = Number(calculatedFee).toLocaleString();
    document.getElementById("p-discount").innerText = Number(discountAmount).toLocaleString();
    document.getElementById("p-finalFee").innerText = Number(finalFee).toLocaleString() + "원";
    document.getElementById("p-finalFee-total").innerText = Number(finalFee).toLocaleString() + "원";

    // 3. 모달 바디에 텍스트가 아닌 '디자인된 HTML'을 삽입
    const receiptDesign = document.getElementById("printArea").innerHTML;
    document.getElementById("modalBody").innerHTML = receiptDesign; // innerText 아님!

    // 4. 모달 보이기
    document.getElementById("customModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("customModal").style.display = "none";
}

function handleConfirm() {
    // 1. 인쇄 여부 확인
    if (confirm("영수증을 인쇄하시겠습니까?")) {
        printReceipt();
    }

    // 2. 약간의 지연을 주어 브라우저가 submit을 정상적으로 인식하게 함
    setTimeout(function() {
        alert("정산이 완료되었습니다. 대시보드로 이동합니다.");
        const form = document.forms['payment'];
        if (form) {
            form.submit(); // 이제 컨트롤러의 doPost로 데이터가 날아감
        }
    }, 500);
}

// 인쇄 실행 함수
function printReceipt() {
    const printWindow = window.open('', '_blank', 'width=450,height=700');
    printWindow.document.write('<html><head><title>영수증 인쇄</title>');
    // 스타일을 유지하기 위해 폰트 설정 등 추가
    printWindow.document.write('<style>body { font-family: "Malgun Gothic"; }</style>');
    printWindow.document.write('</head><body>');
    printWindow.document.write(document.getElementById("printArea").innerHTML);
    printWindow.document.write('</body></html>');

    printWindow.document.close();
    printWindow.focus();

    setTimeout(function() {
        printWindow.print();
        printWindow.close();
    }, 500);
}