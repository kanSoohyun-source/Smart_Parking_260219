// 날짜 포맷팅 함수
function formatDateTime(dtStr) {
    if(!dtStr || dtStr === "null" || dtStr === "") return "-";
    return dtStr.replace('T', ' ').substring(0, 16);
}

function showReceipt() {
    const carNum = document.getElementById("carNum").value;
    const totalTime = document.getElementById("totalParkingTime").value;
    const calculatedFee = document.getElementById("calculatedFee").value;
    const discountAmount = document.getElementById("discountAmount").value;
    const finalFee = document.getElementById("finalFee").value;

    const receiptMsg =
        "========== [ 주차 정산 영수증 ] ==========\n\n" +
        "🚗 차량번호 : " + carNum + "\n" +
        "------------------------------------------\n" +
        "📅 입차시간 : " + formatDateTime(entryTime) + "\n" +
        "📅 출차시간 : " + formatDateTime(exitTime) + "\n" +
        "⌛ 주차시간 : " + totalTime + "\n" +
        "------------------------------------------\n" +
        "💰 할인전 금액 : " + Number(calculatedFee).toLocaleString() + "원\n" +
        "🎁 할인금액   : -" + Number(discountAmount).toLocaleString() + "원\n" +
        "💵 최종결제금액 : " + Number(finalFee).toLocaleString() + "원\n\n" +
        "정산하시겠습니까?\n(확인 시 출차 처리됩니다.)\n";

    // 모달 내용 채우고 보이기
    document.getElementById("modalBody").innerText = receiptMsg;
    document.getElementById("customModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("customModal").style.display = "none";
}

function handleConfirm() {
    if (confirm("영수증을 인쇄하시겠습니까?")) {
        printReceipt();
    }
    alert("정산이 완료되었습니다. 대시보드로 이동합니다.");
    document.forms['payment'].submit();
}

// 인쇄 실행 함수
function printReceipt() {
    document.getElementById("p-carNum").innerText = document.getElementById("carNum").value;
    document.getElementById("p-entryTime").innerText = formatDateTime(entryTime);
    document.getElementById("p-exitTime").innerText = formatDateTime(exitTime);
    document.getElementById("p-totalTime").innerText = document.getElementById("totalParkingTime").value;
    document.getElementById("p-calcFee").innerText = Number(document.getElementById("calculatedFee").value).toLocaleString();
    document.getElementById("p-discount").innerText = Number(document.getElementById("discountAmount").value).toLocaleString();
    document.getElementById("p-finalFee").innerText = Number(document.getElementById("finalFee").value).toLocaleString();

    const printWindow = window.open('', '_blank', 'width=400,height=600');
    printWindow.document.write('<html><head><title>영수증 인쇄</title></head><body>');
    printWindow.document.write(document.getElementById("printArea").innerHTML);
    printWindow.document.write('</body></html>');

    printWindow.document.close();
    printWindow.focus();

    setTimeout(function() {
        printWindow.print();
        printWindow.close();
    }, 250);
}