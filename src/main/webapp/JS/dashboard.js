/*
document.addEventListener("DOMContentLoaded", function() {
    renderParkingSlots();
});

function renderParkingSlots() {
    const gridContainer = document.getElementById('parkingGrid');

    // 기존 내용을 초기화
    gridContainer.innerHTML = '';

    // 1. 임시로 주차된 차량들의 정보 객체 (원하시는 더미 데이터 설정)
    const occupiedData = {
        3: { carNum: '123가 1234', entryTime: '09:00' }, // A3번 자리
        7: { carNum: '345나 2334', entryTime: '14:55' }, // A7번 자리
        12: { carNum: '567다 8765', entryTime: '18:27' } // A12번 자리
    };

    let parkingSlots = [];

    // 2. 1번부터 20번까지 슬롯 데이터 생성
    for (let i = 1; i <= 20; i++) {
        let slot = {
            id: 'A' + i,
            carNum: null,   // 기본은 공차
            entryTime: null
        };

        // 현재 번호(i)가 더미 데이터(occupiedData)에 있다면 정보 덮어씌우기
        if (occupiedData[i]) {
            slot.carNum = occupiedData[i].carNum;
            slot.entryTime = occupiedData[i].entryTime;
        }

        parkingSlots.push(slot);
    }

    // 3. 화면에 HTML 렌더링
    parkingSlots.forEach(slot => {
        // 공차 여부 확인 (차량 번호가 없으면 공차)
        const isEmpty = slot.carNum === null;

        // 슬롯 요소(div) 생성
        const slotDiv = document.createElement('div');
        // CSS 스타일 적용을 위한 클래스 부여 (occupied 또는 empty)
        slotDiv.className = `slot-item ${isEmpty ? 'empty' : 'occupied'}`;

        // 내용 구성 (공차일 때와 입차일 때 보여주는 정보 다르게 설정)
        let innerHTML = `<div class="slot-title">${slot.id}</div>`;

        if (isEmpty) {
            innerHTML += `<div class="slot-status">공차</div>`;
        } else {
            innerHTML += `
                <div class="slot-status" style="color: #1976d2; font-weight: bold;">${slot.carNum}</div>
                <div class="slot-time">${slot.entryTime} 입차</div>
            `;
        }

        slotDiv.innerHTML = innerHTML;

        // [클릭 이벤트] 공차면 입차 페이지로, 차가 있으면 출차 페이지로 이동
        slotDiv.addEventListener('click', function() {
            if (isEmpty) {
                // 공차 -> 입차 페이지 (entry.jsp)
                location.href = `webapp/WEB-INF/view/entry/entry.jsp`;
            } else {
                // 주차 중 -> 출차 페이지 (exit.jsp)
                location.href = `webapp/WEB-INF/view/exit/exit.jsp`;
            }
        });

        gridContainer.appendChild(slotDiv);
    });
}*/
